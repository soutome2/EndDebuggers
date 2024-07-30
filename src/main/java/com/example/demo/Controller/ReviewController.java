package com.example.demo.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.example.demo.Entity.ReserveCustomer;
import com.example.demo.Entity.Review;
import com.example.demo.Form.ReserveCustomerWithDateTime;
import com.example.demo.Form.ReviewInputForm;
import com.example.demo.Repository.ReserveCustomerRepository;
import com.example.demo.Repository.ReviewRepository;
import com.example.demo.Service.ReviewService;
import com.example.demo.Service.TextAnalyticsService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class ReviewController {
	private final ReserveCustomerRepository reserveCustomerRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewService reviewService;
	private final HttpSession session;
	private final TextAnalyticsService textAnalyticsService;

	@GetMapping("/ReviewInput")
	public ModelAndView GetReview(ReviewInputForm reviewInputForm, ModelAndView mv) {

		mv.setViewName("reviewInput");

		return mv;

	}

	@GetMapping("/ReviewComplete")
	public ModelAndView ReviewComplete(@ModelAttribute ReviewInputForm reviewInputForm, ModelAndView mv) {
		mv.addObject("reviewInputForm", reviewInputForm);
		mv.setViewName("reviewComplete");
		return mv;
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

		List<Review> list = reviewService.sortReviewList(ename, sortStar, sortSentiment, startDate, endDate, sortBy,
				sortOrder);

		//平均値計算
		reviewService.getAverage(list);

		//空タイトルを除くfilter処理
		List<Review> filteredList = reviewService.getFilteredReview(list);

		int startIndex = (page - 1) * 10; // 開始インデックスの計算
		int endIndex = startIndex + 10; // 終了インデックスの計算

		// サブリストを取得
		List<Review> sublist = reviewService.getSubReview(filteredList, startIndex, endIndex);

		List<Integer> sentimentSumList = reviewService.CountSentiment(filteredList);

		List<Integer> pages = new ArrayList<>();

		List<String> maxRateList = new ArrayList<>();

		maxRateList = reviewService.GetMaxRateList(sublist);

		

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
		mv.addObject("sentimentSumList", sentimentSumList);
		mv.addObject("maxRateList", maxRateList);
		mv.setViewName("review");
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
	
	@PostMapping("/ReviewConcierge")
	public ModelAndView PostReviewConcierge(@RequestParam("ename") String ename, RedirectAttributes redirectAttributes,
			ModelAndView mv) {
		session.removeAttribute("sortStar");
		session.removeAttribute("sortSentiment");
		session.removeAttribute("startDate");
		session.removeAttribute("endDate");
		session.removeAttribute("sortBy");
		session.setAttribute("sortOrder", false);
		session.setAttribute("sortEname", ename);
		redirectAttributes.addAttribute("page", 1);
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
			String sentiment="";
			if (text != "") {
				DocumentSentiment documentSentiment = textAnalyticsService.analyzeSentiment(text);
				review.setPositiverate(documentSentiment.getConfidenceScores().getPositive());
				review.setNeutralrate(documentSentiment.getConfidenceScores().getNeutral());
				review.setNegativerate(documentSentiment.getConfidenceScores().getNegative());

				Double positiveRate = documentSentiment.getConfidenceScores().getPositive();
				Double neutralRate = documentSentiment.getConfidenceScores().getNeutral();
				Double negativeRate = documentSentiment.getConfidenceScores().getNegative();

				sentiment = textAnalyticsService.MaxRateSentiment(positiveRate, neutralRate, negativeRate);

				review.setSentiment(sentiment);
			}
			reviewRepository.saveAndFlush(review);

			redirectAttributes.addFlashAttribute("reviewInputForm", reviewInputForm);
			redirectAttributes.addFlashAttribute("sentiment",sentiment);
			mv.setViewName("redirect:/ReviewComplete");
			return mv;
		} else {

			mv.addObject("reviewInputForm", reviewInputForm);
			mv.setViewName("reviewInput");
			return mv;
		}

	}

	@Transactional
	@PostMapping("/ReviewDelete")
	public ModelAndView PostReviewDelete(@RequestParam("reviewid") Integer reviewid, ModelAndView mv) {

		reviewRepository.deleteById(reviewid);
		List<ReserveCustomer> list = reserveCustomerRepository
				.findAIIBycustomerId((String) session.getAttribute("cid"));
		List<ReserveCustomerWithDateTime> combinedList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			// LocalDateとLocalTimeをLocalDateTimeに変換
			LocalDateTime dateTime = LocalDateTime.of(list.get(i).getReservedate(), list.get(i).getReservetime());
			// 希望のフォーマットを指定
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/dd HH:mm");
			// フォーマットして文字列に変換
			combinedList.add(new ReserveCustomerWithDateTime(list.get(i), dateTime.format(formatter)));
		}
		List<Review> reviewlist = reviewRepository
				.findAIIByCidOrderByReviewdateDescReviewtimeDesc((String) session.getAttribute("cid"));
		mv.addObject("reserveList", combinedList);
		mv.addObject("reviewList", reviewlist);
		mv.setViewName("customer");

		return mv;
	}

}
