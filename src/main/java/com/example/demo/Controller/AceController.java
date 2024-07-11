package com.example.demo.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	private final HttpSession session;

	@GetMapping("/")
	public String GetHome() {
		return "home";

	}

	@GetMapping("/Login")
	public String GetUserLogin(LoginForm loginForm) {
		return "login";

	}

	@GetMapping("/Reserve")
	public ModelAndView GetReserve(ReserveInputForm reserveInputForm, ModelAndView mv,
			ReviewInputForm reviewInputForm) {
		Object enameObject = session.getAttribute("ename");
		String enameString = null;
		if (enameObject == null) {
			mv.addObject("errorMessage", "Request method 'GET' is not supported");
			mv.setViewName("error");
			return mv;
		}

		enameString = enameObject.toString(); // toString()メソッドでStringに変換

		List<Reserve> reserveList = reserveRepository.findAllByEname(enameString);

		//表示する日数の幅と時間の幅のパラメーター
		int dateRange = 7;
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

			System.out.println(reserveDate);
			System.out.println(reserveTime);
			int daysDifference = (int) ChronoUnit.DAYS.between(startDate, reserveDate);
			int timeDifference = (int) ChronoUnit.HOURS.between(startTime, reserveTime);

			System.out.println("OK");
			System.out.println(startDate);
			System.out.println(reserveDate);
			System.out.println(daysDifference);

			System.out.println(timeDifference);

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
		List<Review> list = reviewRepository.findAIIByEname(ename);
		int endIndex = 5;
		if (endIndex >= list.size()) {
			endIndex = list.size();
		}
		List<Review> sublist = list.subList(0, endIndex);

		session.setAttribute("matrix", matrix);
		session.setAttribute("dateList", dateList);
		session.setAttribute("headDateList", headDateList);
		session.setAttribute("timeList", timeList);
		session.setAttribute("min", min);
		session.setAttribute("max", max);

		mv.addObject("reviewList", sublist);
		mv.addObject("reserveInputForm", reserveInputForm);
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
			List<Review> reviewlist = reviewRepository.findAIIByCid(loginForm.getCid());
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
		List<Review> list = new ArrayList<>();
		if (sortStar != null) {
			list = reviewRepository.findAIIByEnameAndStar(ename, sortStar);
		} else {
			list = reviewRepository.findAIIByEname(ename);
		}

		int startIndex = (page - 1) * 10; // 開始インデックスの計算
		int endIndex = startIndex + 9; // 終了インデックスの計算

		// インデックスがリストの範囲内に収まるように調整
		if (startIndex < 0) {
			startIndex = 0;
		}
		if (endIndex >= list.size()) {
			endIndex = list.size() - 1;
		}

		// サブリストを取得
		List<Review> sublist = list.subList(startIndex, endIndex + 1);

		List<Integer> pages = new ArrayList<>();

		// 範囲外の入力は空リストを返す
		if (page < 0 || page > list.size() / 10 + 1) {
			mv.setViewName("home");
			return mv;
		}

		// 前後2つの数字を含むリストを生成
		for (int i = page - 2; i <= page + 2; i++) {
			if (i >= 1 && i <= list.size() / 10 + 1) {
				pages.add(i);
			}
		}

		mv.addObject("reviewList", sublist);
		mv.addObject("pages", pages);
		mv.setViewName("review");
		return mv;
	}

	@Transactional
	@PostMapping("/ReviewDelete")
	public ModelAndView PostReviewDelete(@RequestParam("reviewid") Integer reviewid, ModelAndView mv) {
		Optional<Review> delete = reviewRepository.findById(reviewid);
		Review deleteReview = delete.get();
		reviewRepository.deleteById(reviewid);
		mv.addObject("deleteReview", deleteReview);
		mv.setViewName("reviewDelete");
		return mv;
	}

	@PostMapping("/ReviewAll")
	public ModelAndView PostReviewAll(@RequestParam("page") Integer page, RedirectAttributes redirectAttributes,
			ModelAndView mv) {
		session.removeAttribute("sortStar");
		session.setAttribute("sortEname", (String) session.getAttribute("ename"));
		redirectAttributes.addAttribute("page", page);
		mv.setViewName("redirect:/Review");
		return mv;
	}

	@PostMapping("/SortStar")
	public ModelAndView PostSortStar(@RequestParam("page") Integer page, @RequestParam("sortEname") String sortEname, @RequestParam("sortStar") Integer sortStar,
			RedirectAttributes redirectAttributes, ModelAndView mv) {
		
		session.setAttribute("sortEname", sortEname);
		
		if (sortStar == 0) {
			session.removeAttribute("sortStar");
		} else {
			session.setAttribute("sortStar", sortStar);
		}
		redirectAttributes.addAttribute("page", page);
		mv.setViewName("redirect:/Review");
		return mv;
	}

}
