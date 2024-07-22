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
import com.example.demo.Service.JsonConverterService;
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
	private final JsonConverterService jsonConverterService;

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
			LocalTime currentTime = startTime.plusHours(j);
			timeList.add(currentTime);
		}

		// 結果の出力（確認用）
		System.out.println("Dates:");
		for (LocalDate date : dateList) {
			System.out.println(date);
		}

		System.out.println("\nTimes:");
		for (LocalTime time : timeList) {
			System.out.println(time);
		}

		for (int i = 0; i < timeRange + 1; i++) {
			List<Integer> row = new ArrayList<>(dateRange + 1); // 列数を指定してArrayListを生成

			// 各列を0で初期化
			for (int j = 0; j < dateRange + 1; j++) {
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

			if (daysDifference < 0 || dateRange < daysDifference) {
				continue;
			}

			if (timeDifference < 0 || timeRange < timeDifference) {
				continue;
			}

			matrix.get(timeDifference).set(daysDifference, 1);

		}

		// 二重リストの内容を出力（確認用）
		for (List<Integer> row : matrix) {
			System.out.println(row);

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
		System.out.println("Ok1");
		List<Review> list = reviewRepository.findAIIByEnameOrderByReviewdateDescReviewtimeDesc(ename);
		System.out.println("Ok2");
		reviewService.getAverage(ename);
		System.out.println("Ok3");
		List<Review> filteredList = reviewService.getFilteredReview(list);
		System.out.println("Ok4");
		

		// 5件のサブリストを取得
		List<Review> sublist = reviewService.getSubReview(filteredList, 0, 5);

		List<Integer> sentimentSumList = reviewService.CountSentiment(list);
		

		for (Integer i : sentimentSumList) {
			System.out.println(i);
		}

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
		reserveRepository.saveAndFlush(reserve);
		return "complete";
	}

	@PostMapping("/setEname")
	public String PostReserve(@RequestParam("ename") String ename, @RequestParam("furigana") String furigana,
			@RequestParam("referral") String referral) {
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
