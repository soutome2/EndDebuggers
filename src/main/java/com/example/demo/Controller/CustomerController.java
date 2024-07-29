package com.example.demo.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Entity.Customer;
import com.example.demo.Entity.ReserveCustomer;
import com.example.demo.Entity.Review;
import com.example.demo.Form.CustomerInputForm;
import com.example.demo.Form.LoginForm;
import com.example.demo.Form.ReserveCustomerWithDateTime;
import com.example.demo.Repository.CustomerRepository;
import com.example.demo.Repository.ReserveCustomerRepository;
import com.example.demo.Repository.ReviewRepository;
import com.example.demo.Service.CustomerService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

/**
 * 顧客関連のコントローラクラス
 * @author kachi
 *
 */
@AllArgsConstructor
@Controller
public class CustomerController {
	private final CustomerRepository customerRepository;
	private final ReserveCustomerRepository reserveCustomerRepository;
	private final ReviewRepository reviewRepository;
	private final CustomerService customerService;
	private final HttpSession session;

	/**
	 * ログイン入力ページ
	 * @param loginForm 空のログインフォーム
	 * @return login.html
	 */
	@GetMapping("/Login")
	public String GetUserLogin(LoginForm loginForm) {
		return "login";

	}

	/**
	 * 顧客情報入力ページ
	 * @param customerInputForm 空のカスタマーフォーム
	 * @return customer.html
	 */
	@GetMapping("/CustomerInput")
	public String showCustomerForm(CustomerInputForm customerInputForm) {
		return "customerInput";
	}

	/**
	 * 顧客マイページ
	 * @param loginForm ログイン認証用ログインフォーム
	 * @param result エラー表示の保管庫
	 * @param mv ModelAndView
	 * @return customer.html
	 */
	@PostMapping("/Customer")
	public ModelAndView showCustomerList(@ModelAttribute @Validated LoginForm loginForm,
			BindingResult result, ModelAndView mv) {

		Customer customer = customerService.getByCid(loginForm, result);

		if (!result.hasErrors()) {
			session.setAttribute("cid", loginForm.getCid());
			session.setAttribute("password", loginForm.getPassword());
			session.setAttribute("cname", customer.getCname());
			List<ReserveCustomer> list = reserveCustomerRepository.findAIIBycustomerId(loginForm.getCid());
			List<Review> reviewlist = reviewRepository
					.findAIIByCidOrderByReviewdateDescReviewtimeDesc(loginForm.getCid());
			List<ReserveCustomerWithDateTime> combinedList = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				// LocalDateとLocalTimeをLocalDateTimeに変換
				LocalDateTime dateTime = LocalDateTime.of(list.get(i).getReservedate(), list.get(i).getReservetime());
				// 希望のフォーマットを指定
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/dd HH:mm");
				// フォーマットして文字列に変換
				combinedList.add(new ReserveCustomerWithDateTime(list.get(i), dateTime.format(formatter)));
			}
			mv.addObject("reserveList", combinedList);
			mv.addObject("reviewList", reviewlist);
			mv.setViewName("customer");
			return mv;
		} else {
			mv.setViewName("login");
			return mv;
		}
	}

	/**
	 * 顧客情報登録完了ページ
	 * @param customerInputForm 顧客フォーム
	 * @return customerComplete
	 */
	@GetMapping("/CustomerComplete")
	public String GetCustomerComplete(@ModelAttribute CustomerInputForm customerInputForm) {
		Customer customer = customerInputForm.getEntity();
		session.setAttribute("cid", customer.getCid());
		session.setAttribute("password", customerInputForm.getPassword());
		session.setAttribute("cname", customer.getCname());
		customerRepository.saveAndFlush(customer);
		return "customerComplete";
	}
	
	/**
	 * 顧客情報登録時の処理
	 * @param customerInputForm 顧客フォーム
	 * @return customerComplete　エラーがある場合は顧客登録ページにリダイレクト
	 * @return redirect:/CustomerComplete　エラーがない場合顧客登録完了画面
	 */
	@PostMapping("/CustomerError")
	public String PostCustomerError(@ModelAttribute @Validated CustomerInputForm customerInputForm,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		customerService.setCustomer(customerInputForm, result);

		if (!result.hasErrors()) {
			redirectAttributes.addFlashAttribute("customerInputForm", customerInputForm);
			return "redirect:/CustomerComplete";
		} else {
			return "customerInput";
		}
	}
	
	/**
	 * ログアウト時の処理
	 * @return redirect:/ ホームページにリダイレクト
	 */
	@PostMapping("/Logout")
	public String PostLogout() {
		session.removeAttribute("cid");
		session.removeAttribute("password");
		return "redirect:/";
	}

}
