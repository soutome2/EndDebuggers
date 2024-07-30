package com.example.demo.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Entity.Reserve;
import com.example.demo.Entity.Review;
import com.example.demo.Form.ReserveInputForm;
import com.example.demo.Form.ReviewInputForm;
import com.example.demo.Repository.ReserveRepository;
import com.example.demo.Repository.ReviewRepository;
import com.example.demo.Service.CustomerService;
import com.example.demo.Service.ReserveService;
import com.example.demo.Service.ReviewService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class ReserveController {
	private final ReserveRepository reserveRepository;
	private final ReviewRepository reviewRepository;
	private final CustomerService customerService;
	private final ReserveService reserveService;
	private final ReviewService reviewService;
	private final HttpSession session;

	@CrossOrigin
	@GetMapping("/Reserve")
	public ModelAndView GetReserve(ReserveInputForm reserveInputForm, ModelAndView mv,
			ReviewInputForm reviewInputForm) {
		Object enameObject = session.getAttribute("ename");
		String enameString = null;
		if (enameObject == null) {
			mv.addObject("errorMessage", "不正なアクセスです。");
			mv.setViewName("error");
			return mv;
		}

		enameString = enameObject.toString(); // toString()メソッドでStringに変換

		List<Reserve> reserveList = reserveRepository.findAllByEname(enameString);

		//表示する日数の幅と時間の幅のパラメーター
		int dateRange = 34;
		int timeRange = 9;

		//開始終了日時　開始時刻
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = startDate.plusDays(dateRange);
		LocalTime startTime = LocalTime.of(10, 0);
		LocalTime currentTime = LocalTime.now();
		//現在時刻と開始時刻の差を取得
		long minutesDifference = ChronoUnit.MINUTES.between(startTime, currentTime);
		double hoursDifference = minutesDifference / 60.0;
		//matrix:予約があるかどうかを判断するための2次元配列 (0:予約なし,1予約あり) date,timeList:htmlで日付けと時間をひょうじするためのリストdateRange,timeRangeで期間調整
		List<List<Integer>> matrix = new ArrayList<>();
		List<LocalDate> dateList = new ArrayList<>();
		List<String> headDateList = new ArrayList<>();
		List<LocalTime> timeList = new ArrayList<>();

		//今日からdaterange分の日付けリスト 2024/07/03～2024/07/10
		for (int i = 0; i < dateRange + 1; i++) {
			LocalDate currentDate = startDate.plusDays(i);
			dateList.add(currentDate);

			// DateTimeFormatter を使って指定された形式でフォーマットする
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d(E)", Locale.JAPAN);
			String formattedDate = currentDate.format(formatter);
			headDateList.add(formattedDate);

		}

		//10時から19時までの時間リスト
		for (int j = 0; j < timeRange + 1; j++) {
			LocalTime time = startTime.plusHours(j);
			timeList.add(time);
		}

	
		for (int i = 0; i < timeRange + 1; i++) {
			List<Integer> row = new ArrayList<>(dateRange + 1); // 列数を指定してArrayListを生成

			// 各列を0で初期化
			for (int j = 0; j < dateRange + 1; j++) {
				if(j==0&&i<hoursDifference) {
					row.add(1);
					continue;
				}
				row.add(0);
			}

			// 行を二重リストに追加
			matrix.add(row);
		}
		for (Reserve i : reserveList) {

			LocalDate reserveDate = i.getReservedate();
			LocalTime reserveTime = i.getReservetime();

			int daysDifference = (int) ChronoUnit.DAYS.between(startDate, reserveDate);
			int timeDifference = (int) ChronoUnit.HOURS.between(startTime, reserveTime);
			//reservdateが開始日より前or設定した期間以でなければ1追加しないでループ飛ばす
			if (daysDifference < 0 || dateRange < daysDifference) {
				continue;
			}
			//reservdateが開始時刻より前or設定した期間内でなければ1追加しないでループ飛ばす
			if (timeDifference < 0 || timeRange < timeDifference) {
				continue;
			}

			matrix.get(timeDifference).set(daysDifference, 1);

		}

		//日数処理
		String min = startDate.format(DateTimeFormatter.ISO_DATE);
		String max = endDate.format(DateTimeFormatter.ISO_DATE);
		
		//sessionenameの取得
		String ename = (String) session.getAttribute("ename");

		/* 	//reviewリストの作成 API化
		
		//APIURLのためのリクエストパラメータを指定
		String sortby="date";
		String order="false";

	
　　　　//%sに変数埋め込まれる　ename:sessionから sortby:date order:falese 降順
		String base = "https://aceconcierge.azurewebsites.net/GetReviewJson?ename=%s"
				+ "&sortby=%s"
				+ "&order=%s";

		String apiUrl= String.format(base, ename,sortby,order);
		System.out.println(apiUrl);

		ResponseEntity<String> responseEntity = new RestTemplate().getForEntity(
				apiUrl,
				String.class);
		String responseBody = responseEntity.getBody();
		List<Review> list2 = jsonConverterService.JsonToEntity(responseBody);
		System.out.println("エンテティ");
		System.out.println(list2);
		System.out.println("レスポンスボディ: " + responseBody);
		List<Review> filteredList = reviewService.getFilteredReview(list2);
		List<Review> sublist = reviewService.getSubReview(filteredList, 0, 5);

		List<Integer> sentimentSumList = reviewService.CountSentiment(list2);
		*/
		
		//reviewリストの作成 通常
		List<Review> list = reviewRepository.findAIIByEnameOrderByReviewdateDescReviewtimeDesc(ename);
		reviewService.getAverage(list);
		List<Review> filteredList = reviewService.getFilteredReview(list);
		
		// 5件のサブリストを取得
		List<Review> sublist = reviewService.getSubReview(filteredList, 0, 5);

		List<Integer> sentimentSumList = reviewService.CountSentiment(filteredList);

		session.setAttribute("matrix", matrix);
		session.setAttribute("dateList", dateList);
		session.setAttribute("headDateList", headDateList);
		session.setAttribute("timeList", timeList);
		session.setAttribute("min", min);
		session.setAttribute("max", max);

		mv.addObject("reviewList", sublist);
		mv.addObject("reserveInputForm", reserveInputForm);
		mv.addObject("sentimentSumList", sentimentSumList);

		mv.setViewName("reserve");

		return mv;
	}

	@GetMapping("/ReserveComplete")
	public String PostReserveComplete(@ModelAttribute ReserveInputForm reserveInputForm) {
		Reserve reserve = reserveInputForm.getEntity();
		session.setAttribute("cid", reserveInputForm.getCid());
		session.setAttribute("password", reserveInputForm.getPassword());

		reserveRepository.saveAndFlush(reserve);
		return "complete";
	}

	@PostMapping("/setEname")
	public String PostReserve(@RequestParam("ename") String ename) {
		String furigana = null;
		String referral = null;
		if ("田中太郎".equals(ename)) {
			furigana = "(タナカ タロウ)";
			referral = "はじめまして、田中太郎と申します。\r\n"
					+ "不動産業界で30年以上の経験を持ち、土地や住宅の売買、賃貸、管理に精通しています。\r\n"
					+ "お客様のニーズに合わせた最適な不動産戦略を提案いたしますので、\r\n"
					+ "どんな些細なご相談でもお気軽にお声掛けください。";
		} else if ("佐藤花子".equals(ename)) {
			furigana = "(サトウ ハナコ)";
			referral = "はじめまして、佐藤花子です。\r\n"
					+ "				建築士の資格を持ち、20年以上のリフォーム業界で経験を積んでいます。\r\n"
					+ "				住宅の修繕やリフォームの設計から施工まで、幅広く対応しています。\r\n"
					+ "				特に省エネ改修やバリアフリーリフォームに精通しており、\r\n"
					+ "				お客様のニーズに合わせた提案を心がけています。ご相談はいつでもお気軽にどうぞ。";
		} else if ("鈴木一郎".equals(ename)) {
			furigana = "(スズキ イチロウ)";
			referral = "はじめまして、鈴木一郎です。\r\n"
					+ "				介護福祉士として25年のキャリアを持ち、\r\n"
					+ "				特に認知症ケアや在宅介護の支援に力を注いでいます。\r\n"
					+ "                利用者とその家族に寄り添ったサポートを提供し、地域社会に貢献しています。\r\n"
					+ "                どうぞお気軽にご相談ください。";
		} else if ("高橋美咲".equals(ename)) {
			furigana = "(タカハシ ミサキ)";
			referral = "はじめまして、高橋美咲です。\r\n"
					+ "				ファイナンシャルプランナー資格を持ち、終活や相続に特化した法的・\r\n"
					+ "				財務的なアドバイスを提供しています。遺言書作成や相続税対策、財産分割など、\r\n"
					+ "				クライアントのニーズに合わせたプランを提案し、実行をサポートしています。\r\n"
					+ "				安心と満足を最優先に考え、真摯に対応させていただきます。\r\n"
					+ "				ご相談やお悩みがございましたら、お気軽にご連絡ください。";
		} else if ("中村健太".equals(ename)) {
			furigana = "(ナカムラ ケンタ)";
			referral = "はじめまして、中村健太です。\r\n"
					+ "				保険業界で20年以上の経験があり、特に自動車保険や生命保険に詳しいです。\r\n"
					+ "				クライアントのライフスタイルに合わせた最適な保険商品の提案や見直しを行って\r\n"
					+ "				います。資産運用やリスク管理についても幅広くサポートします。\r\n"
					+ "				ご相談やお問い合わせがございましたら、お気軽にどうぞ。";
		}
		session.setAttribute("ename", ename);
		session.setAttribute("furigana", furigana);
		session.setAttribute("referral", referral);
		return "redirect:/Reserve";

	}

	@PostMapping("/ReserveInput")
	public ModelAndView PostReserveInput(@RequestParam LocalDate date, @RequestParam LocalTime time,
			ReserveInputForm reserveInputForm,
			ModelAndView mv) {

		reserveInputForm.setReservedate(date);
		reserveInputForm.setReservetime(time);
		mv.addObject("reserveInputForm", reserveInputForm);
		mv.setViewName("reserveInput");
		return mv;

	}

	@PostMapping("/ReserveError")
	public String PostReserveError(@ModelAttribute @Validated ReserveInputForm reserveInputForm,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		customerService.getByCid(reserveInputForm, result);
		reserveService.getByDateTime(reserveInputForm, result);

		if (!result.hasErrors()) {
			redirectAttributes.addFlashAttribute("reserveInputForm", reserveInputForm);
			return "redirect:/ReserveComplete";
		} else {
			return "reserveInput";
		}

	}

	@Transactional
	@PostMapping("/CancelComplete")
	public ModelAndView PostReserveComplete(@RequestParam("reserveid") Integer reserveid, ModelAndView mv) {
		Reserve deleteReserve = reserveRepository.findByReserveid(reserveid);
		reserveRepository.deleteByReserveid(reserveid);
		mv.addObject("deleteReserve", deleteReserve);
		mv.setViewName("cancelComplete");
		return mv;
	}

	@PostMapping("/Complete")
	public ModelAndView PostComplete(@ModelAttribute @Validated ReserveInputForm reserveInputForm, BindingResult result,
			RedirectAttributes redirectAttributes,
			ModelAndView mv) {
		customerService.getByCid(reserveInputForm, result);

		if (!result.hasErrors()) {
			Reserve reserve = reserveInputForm.getEntity();
			reserveRepository.saveAndFlush(reserve);
			mv.addObject("reserveInputForm", reserveInputForm);
			mv.setViewName("complete");
			return mv;
		} else {
			mv.setViewName("reserve");
			return mv;
		}

	}

}
