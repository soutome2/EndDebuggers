package com.example.demo.Controller;

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

import com.example.demo.Entity.Customer;
import com.example.demo.Entity.Reserve;
import com.example.demo.Entity.ReserveCustomer;
import com.example.demo.Form.CustomerInputForm;
import com.example.demo.Form.LoginForm;
import com.example.demo.Form.ReserveInputForm;
import com.example.demo.Repository.CustomerRepository;
import com.example.demo.Repository.ReserveCustomerRepository;
import com.example.demo.Repository.ReserveRepository;
import com.example.demo.Service.CustomerService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class AceController {
	private final CustomerRepository customerRepository;
	private final ReserveRepository reserveRepository;
	private final ReserveCustomerRepository reserveCustomerRepository;
	private final CustomerService customerService;
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
	public String GetReserve(ReserveInputForm reserveInputForm) {
		return "reserveInput";
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
			session.setAttribute("cname", customer.getCname());
			List<ReserveCustomer> list = reserveCustomerRepository.findAIIBycustomerId(customer.getCid());
			mv.addObject("reserveList", list);
			mv.setViewName("customer");
			return mv;
		} else {
			mv.setViewName("login");
			return mv;
		}
	}

	@PostMapping("/Complete")
	public ModelAndView PostCustomer(@ModelAttribute @Validated ReserveInputForm reserveInputForm, BindingResult result, RedirectAttributes redirectAttributes,
			ModelAndView mv) {
		customerService.getByCid(reserveInputForm, result);

		if (!result.hasErrors()) {
			Reserve reserve = reserveInputForm.getEntity();
			reserveRepository.saveAndFlush(reserve);
			mv.setViewName("complete");
			return mv;
		} else {
			mv.setViewName("reserveInput");
			return mv;
		}

	}

	@PostMapping("/setCustomer")
	public String PostComplete(@ModelAttribute CustomerInputForm customerInputForm, BindingResult result) {
		if (!result.hasErrors()) {
			String cname = customerInputForm.getCname();
			session.setAttribute("cid", cname);
			Customer customer = customerInputForm.getEntity();
			customerRepository.saveAndFlush(customer);
			return "redirect:/Reserve";
		} else {
			return "customerInput";
		}
	}

	@PostMapping("/setEname")
	public String PostReserve(@RequestParam("ename") String ename) {
		session.setAttribute("ename", ename);

		return "redirect:/Reserve";

	}

}
