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

import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.example.demo.Entity.Customer;
import com.example.demo.Entity.Reserve;
import com.example.demo.Entity.ReserveCustomer;
import com.example.demo.Entity.Review;
import com.example.demo.Form.CustomerInputForm;
import com.example.demo.Form.LoginForm;
import com.example.demo.Form.ReserveInputForm;
import com.example.demo.Form.ReviewInputForm;
import com.example.demo.Repository.CustomerRepository;
import com.example.demo.Repository.ReserveCustomerRepository;
import com.example.demo.Repository.ReserveRepository;
import com.example.demo.Repository.ReviewRepository;
import com.example.demo.Service.CustomerService;
import com.example.demo.Service.ReserveService;
import com.example.demo.Service.ReviewService;
import com.example.demo.Service.TextAnalyticsService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class AceController {
	private final CustomerRepository customerRepository;
	private final ReserveRepository reserveRepository;
	private final ReserveCustomerRepository reserveCustomerRepository;
	private final ReviewRepository reviewRepository;
	private final CustomerService customerService;
	private final ReserveService reserveService;
	private final ReviewService reviewService;
	private final HttpSession session;
	private final TextAnalyticsService textAnalyticsService;

	@GetMapping("/")
	public String GetHome() {
		LocalDate date = LocalDate.now();
		session.setAttribute("now", date);
		return "home";

	}

	@GetMapping("/Login")
	public String GetUserLogin(LoginForm loginForm) {
		return "login";

	}

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
		int dateRange = 13;
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

		//reviewリスト作成
		String ename = (String) session.getAttribute("ename");
		/*
		//API化
		
		String baseUrl = "https://aceconcierge.azurewebsites.net";
		String endpoint = "/GetReviewJson";
		String queryename = "?ename=%s".formatted(ename);
		
		String apiUrl = jsonConverterService.MakeFilePath(baseUrl, endpoint, queryename);
		//String apiUrl = jsonConverterService.MakeFilePath(baseUrl,endpoint);
		ResponseEntity<String> responseEntity = new RestTemplate().getForEntity(
				apiUrl,
				String.class);
		
		// レスポンスのボディを取得
		String responseBody = responseEntity.getBody();
		List<Review> list = jsonConverterService.JsonToEntity(responseBody);
		*/
		//reviewリストの作成
		System.out.println("Ok1");
		List<Review> list = reviewRepository.findAIIByEnameOrderByReviewdateDescReviewtimeDesc(ename);
		System.out.println("Ok2");
		reviewService.getAverage(ename);
		System.out.println("Ok3");
		List<Review> filteredList = reviewService.getFilteredReview(list);
		System.out.println("Ok4");

		// 5件のサブリストを取得
		List<Review> sublist = reviewService.getSubReview(filteredList, 0, 5);
		
		List<Integer> sentimentSumList=reviewService.CountSentiment(sublist);
		
		for (Integer i:sentimentSumList) {
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
		
		mv.setViewName("reserveInput");

		return mv;
	}

	@GetMapping("/Register")
	public String showCustomerForm(CustomerInputForm customerInputForm) {
		return "customerInput";
	}

	@PostMapping("/Customer")
	public ModelAndView showCustomerList(@ModelAttribute @Validated LoginForm loginForm,
			BindingResult result, ModelAndView mv) {

		Customer customer = customerService.getByCid(loginForm, result);

		if (!result.hasErrors()) {
			session.setAttribute("cid", loginForm.getCid());
			session.setAttribute("password", loginForm.getPassword());
			session.setAttribute("cname", customer.getCname());
			List<ReserveCustomer> list = reserveCustomerRepository.findAIIBycustomerId(loginForm.getCid());
			System.out.println(list);
			List<Review> reviewlist = reviewRepository
					.findAIIByCidOrderByReviewdateDescReviewtimeDesc(loginForm.getCid());
			mv.addObject("reserveList", list);
			mv.addObject("reviewList", reviewlist);
			mv.setViewName("customer");
			return mv;
		} else {
			mv.setViewName("login");
			return mv;
		}
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
			mv.setViewName("reserveInput");
			return mv;
		}

	}

	@PostMapping("/setCustomer")
	public String PostSetCustomer(@ModelAttribute @Validated CustomerInputForm customerInputForm,
			BindingResult result) {

		customerService.setCustomer(customerInputForm, result);

		if (!result.hasErrors()) {
			Customer customer = customerInputForm.getEntity();
			session.setAttribute("cid", customer.getCid());
			session.setAttribute("password", customerInputForm.getPassword());
			session.setAttribute("cname", customer.getCname());
			customerRepository.saveAndFlush(customer);
			return "userRegistrationComplete";
		} else {
			return "customerInput";
		}
	}

	@PostMapping("/setEname")
	public String PostReserve(@RequestParam("ename") String ename, @RequestParam("furigana") String furigana,
			@RequestParam("referral") String referral) {
		session.setAttribute("ename", ename);
		session.setAttribute("furigana", furigana);
		session.setAttribute("referral", referral);
		return "redirect:/Reserve";

	}

	@PostMapping("/Reservetime")
	public ModelAndView PostReserveTime(@RequestParam LocalDate date, @RequestParam LocalTime time,
			ReserveInputForm reserveInputForm,
			ModelAndView mv) {

		reserveInputForm.setReservedate(date);
		reserveInputForm.setReservetime(time);
		mv.addObject("reserveInputForm", reserveInputForm);
		mv.setViewName("reservetime");
		return mv;

	}

	@PostMapping("/ReserveError")
	public ModelAndView PostReserveTime(@ModelAttribute @Validated ReserveInputForm reserveInputForm,
			BindingResult result,
			RedirectAttributes redirectAttributes,
			ModelAndView mv) {
		customerService.getByCid(reserveInputForm, result);
		reserveService.getByDateTime(reserveInputForm, result);

		if (!result.hasErrors()) {
			Reserve reserve = reserveInputForm.getEntity();
			reserveRepository.saveAndFlush(reserve);
			redirectAttributes.addFlashAttribute("reserveInputForm", reserveInputForm);
			mv.setViewName("redirect:/ReserveComplete");
			return mv;
		} else {

			mv.addObject("reserveInputForm", reserveInputForm);
			mv.setViewName("reservetime");
			return mv;
		}

	}

	@GetMapping("/ReserveComplete")
	public ModelAndView PostReserveComplete(@ModelAttribute ReserveInputForm reserveInputForm, ModelAndView mv) {
		mv.addObject("reserveInputForm", reserveInputForm);
		mv.setViewName("complete");
		return mv;
	}

	@GetMapping("/ReviewInput")
	public ModelAndView GetReview(ReviewInputForm reviewInputForm, ModelAndView mv) {

		mv.setViewName("reviewInput");

		return mv;

	}

	@PostMapping("/ReviewError")
	public ModelAndView ReviewError(@ModelAttribute @Validated ReviewInputForm reviewInputForm,
			BindingResult result,
			RedirectAttributes redirectAttributes,
			ModelAndView mv) {

		if (!result.hasErrors()) {
			Review review = reviewInputForm.getEntity();
			LocalDate currentDate = LocalDate.now();
			LocalTime currentTime = LocalTime.now();
			review.setReviewdate(currentDate);
			review.setReviewtime(currentTime);

			//感情分析の結果
			String text = review.getComment();
			DocumentSentiment documentSentiment = textAnalyticsService.analyzeSentiment(text);
			review.setSentiment(documentSentiment.getSentiment().toString());
			review.setPositiverate(documentSentiment.getConfidenceScores().getPositive());
			review.setNeutralrate(documentSentiment.getConfidenceScores().getNeutral());
			review.setNegativerate(documentSentiment.getConfidenceScores().getNegative());

			reviewRepository.saveAndFlush(review);

			redirectAttributes.addFlashAttribute("reviewInputForm", reviewInputForm);
			mv.setViewName("redirect:/ReviewComplete");
			return mv;
		} else {

			mv.addObject("reviewInputForm", reviewInputForm);
			mv.setViewName("reviewInput");
			return mv;
		}

	}

	@GetMapping("/ReviewComplete")
	public ModelAndView ReviewComplete(@ModelAttribute ReviewInputForm reviewInputForm, ModelAndView mv) {
		mv.addObject("reviewInputForm", reviewInputForm);
		mv.setViewName("reviewComplete");
		return mv;
	}

	@Transactional
	@PostMapping("/cancelComplete")
	public ModelAndView PostReserveComplete(@RequestParam("reserveid") Integer reserveid, ModelAndView mv) {
		Reserve deleteReserve = reserveRepository.findByReserveid(reserveid);
		reserveRepository.deleteByReserveid(reserveid);
		mv.addObject("deleteReserve", deleteReserve);
		mv.setViewName("cancelComplete");
		return mv;
	}

	@PostMapping("/Logout")
	public String PostLogout() {
		session.removeAttribute("cid");
		session.removeAttribute("password");
		return "redirect:/";
	}

	@GetMapping("/Review")
	public ModelAndView PostReview(@RequestParam("page") Integer page, ModelAndView mv) {
		String ename = (String) session.getAttribute("sortEname");
		Integer sortStar = (Integer) session.getAttribute("sortStar");
		String sortSentiment = (String) session.getAttribute("sortSentiment");
		LocalDate startDate = (LocalDate) session.getAttribute("startDate");
		LocalDate endDate = (LocalDate) session.getAttribute("endDate");
		String sortBy = (String) session.getAttribute("sortBy");
		boolean sortOrder = (boolean) session.getAttribute("sortOrder");

		List<Review> list = new ArrayList<>();
		//評価絞り込み
		if (startDate != null && endDate != null) {
			if ("star".equals(sortBy)) {
				list = sortOrder
						? reviewRepository.findByEnameAndReviewdateGroupOrderByStar(ename, startDate, endDate, sortStar,
								sortSentiment)
						: reviewRepository.findByEnameAndReviewdateGroupOrderByStarDesc(ename, startDate, endDate,
								sortStar, sortSentiment);
			} else if ("time".equals(sortBy)) {
				list = sortOrder
						? reviewRepository.findByEnameAndReviewdateGroupOrderByReviewdateAscReviewtimeAsc(ename,
								startDate, endDate, sortStar, sortSentiment)
						: reviewRepository.findByEnameAndReviewdateGroupOrderByReviewdateDescReviewtimeDesc(ename,
								startDate, endDate, sortStar, sortSentiment);
			} else {
				list = sortOrder
						? reviewRepository.findByEnameAndReviewdateGroup(ename, startDate, endDate, sortStar,
								sortSentiment)
						: reviewRepository.findByEnameAndReviewdateGroupOrderByReviewdateDescReviewtimeDesc(ename,
								startDate, endDate, sortStar, sortSentiment);
			}
		} else if (startDate != null) {
			if ("star".equals(sortBy)) {
				list = sortOrder
						? reviewRepository.findByEnameAndStartDateOrderByStar(ename, startDate, sortStar, sortSentiment)
						: reviewRepository.findByEnameAndStartDateOrderByStarDesc(ename, startDate, sortStar,
								sortSentiment);
			} else if ("time".equals(sortBy)) {
				list = sortOrder
						? reviewRepository.findByEnameAndStartDateOrderByReviewdateAscReviewtimeAsc(ename, startDate,
								sortStar, sortSentiment)
						: reviewRepository.findByEnameAndStartDateOrderByReviewdateDescReviewtimeDesc(ename, startDate,
								sortStar, sortSentiment);
			} else {
				list = sortOrder ? reviewRepository.findByEnameAndStartDate(ename, startDate, sortStar, sortSentiment)
						: reviewRepository.findByEnameAndStartDateOrderByReviewdateDescReviewtimeDesc(ename, startDate,
								sortStar, sortSentiment);
			}
		} else if (endDate != null) {
			if ("star".equals(sortBy)) {
				list = sortOrder
						? reviewRepository.findByEnameAndEndDateOrderByStar(ename, endDate, sortStar, sortSentiment)
						: reviewRepository.findByEnameAndEndDateOrderByStarDesc(ename, endDate, sortStar,
								sortSentiment);
			} else if ("time".equals(sortBy)) {
				list = sortOrder
						? reviewRepository.findByEnameAndEndDateOrderByReviewdateAscReviewtimeAsc(ename, endDate,
								sortStar, sortSentiment)
						: reviewRepository.findByEnameAndEndDateOrderByReviewdateDescReviewtimeDesc(ename, endDate,
								sortStar, sortSentiment);
			} else {
				list = sortOrder ? reviewRepository.findByEnameAndEndDate(ename, endDate, sortStar, sortSentiment)
						: reviewRepository.findByEnameAndEndDateOrderByReviewdateDescReviewtimeDesc(ename, endDate,
								sortStar, sortSentiment);
			}
		} else {
			if ("star".equals(sortBy)) {
				list = sortOrder ? reviewRepository.findByEnameOrderByStar(ename, sortStar, sortSentiment)
						: reviewRepository.findByEnameOrderByStarDesc(ename, sortStar, sortSentiment);
			} else if ("time".equals(sortBy)) {
				list = sortOrder
						? reviewRepository.findByEnameOrderByReviewdateAscReviewtimeAsc(ename, sortStar, sortSentiment)
						: reviewRepository.findByEnameOrderByReviewdateDescReviewtimeDesc(ename, sortStar,
								sortSentiment);
			} else {
				list = sortOrder ? reviewRepository.findByEname(ename, sortStar, sortSentiment)
						: reviewRepository.findByEnameOrderByReviewdateDescReviewtimeDesc(ename, sortStar,
								sortSentiment);
			}
		}

		//平均値計算
		reviewService.getAverage(ename);

		//空タイトルを除くfilter処理
		List<Review> filteredList = reviewService.getFilteredReview(list);

		int startIndex = (page - 1) * 10; // 開始インデックスの計算
		int endIndex = startIndex + 10; // 終了インデックスの計算

		// サブリストを取得
		List<Review> sublist = reviewService.getSubReview(filteredList, startIndex, endIndex);

		List<Integer> pages = new ArrayList<>();

		// 範囲外の入力は空リストを返す
		if (page < 0 || page > filteredList.size() / 10 + 1) {
			mv.setViewName("home");
			return mv;
		}

		// 前後2つの数字を含むリストを生成
		for (int i = page - 2; i <= page + 2; i++) {
			if (i >= 1 && i <= filteredList.size() / 10 + 1) {
				pages.add(i);
			}
		}

		session.setAttribute("page", page);

		mv.addObject("reviewList", sublist);
		mv.addObject("pages", pages);
		mv.setViewName("review");
		return mv;
	}

	@Transactional
	@PostMapping("/ReviewDelete")
	public ModelAndView PostReviewDelete(@RequestParam("reviewid") Integer reviewid, ModelAndView mv) {

		System.out.println(reviewid);
		reviewRepository.deleteById(reviewid);
		List<ReserveCustomer> list = reserveCustomerRepository
				.findAIIBycustomerId((String) session.getAttribute("cid"));
		System.out.println(list);
		List<Review> reviewlist = reviewRepository
				.findAIIByCidOrderByReviewdateDescReviewtimeDesc((String) session.getAttribute("cid"));
		mv.addObject("reserveList", list);
		mv.addObject("reviewList", reviewlist);
		mv.setViewName("customer");

		return mv;
	}

	@PostMapping("/ReviewAll")
	public ModelAndView PostReviewAll(@RequestParam("page") Integer page, RedirectAttributes redirectAttributes,
			ModelAndView mv) {
		session.removeAttribute("sortStar");
		session.removeAttribute("sortSentiment");
		session.removeAttribute("startDate");
		session.removeAttribute("endDate");
		session.removeAttribute("sortBy");
		session.setAttribute("sortOrder", false);
		session.setAttribute("sortEname", (String) session.getAttribute("ename"));
		redirectAttributes.addAttribute("page", page);
		mv.setViewName("redirect:/Review");
		return mv;
	}

	@PostMapping("/SortStar")
	public ModelAndView PostSortStar(@RequestParam("page") Integer page, @RequestParam("sortEname") String sortEname,
			@RequestParam("sortStar") Integer sortStar,
			@RequestParam(value = "sortSentiment", required = false) String sortSentiment,
			@RequestParam(value = "startDate", required = false) LocalDate startDate,
			@RequestParam(value = "endDate", required = false) LocalDate endDate, @RequestParam("sortBy") String sortBy,
			@RequestParam("sortOrder") boolean sortOrder,
			RedirectAttributes redirectAttributes, ModelAndView mv) {

		session.setAttribute("sortEname", sortEname);

		if (sortStar == 0) {
			session.removeAttribute("sortStar");
		} else {
			session.setAttribute("sortStar", sortStar);
		}

		if (sortSentiment == "") {
			session.removeAttribute("sortSentiment");
		} else {
			session.setAttribute("sortSentiment", sortSentiment);
		}

		if (startDate == null) {
			session.removeAttribute("startDate");
		} else {
			session.setAttribute("startDate", startDate);
		}

		if (endDate == null) {
			session.removeAttribute("endDate");
		} else {
			session.setAttribute("endDate", endDate);
		}

		if (sortBy == "") {
			session.removeAttribute("sortBy");
		} else {
			session.setAttribute("sortBy", sortBy);
		}

		session.setAttribute("sortOrder", sortOrder);

		redirectAttributes.addAttribute("page", page);
		mv.setViewName("redirect:/Review");
		return mv;
	}

}
