package com.example.demo.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.Entity.Customer;
import com.example.demo.Entity.Reserve;
import com.example.demo.Entity.ReserveCustomer;
import com.example.demo.Form.CInputForm;
import com.example.demo.Form.LoginForm;
import com.example.demo.Form.OInputForm;
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
	public String GetReserve(Model model) {
		model.addAttribute("oInputForm", new OInputForm());
		return "reserveInput";
	}

	@GetMapping("/Register")
	public String showCustomerForm(Model model) {
		model.addAttribute("cInputForm", new CInputForm());
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
	public String PostCustomer(@ModelAttribute @Validated OInputForm oInputForm, BindingResult result,
			ModelAndView mv) {
		LoginForm loginForm = new LoginForm();
		loginForm.setCid(oInputForm.getCid());
		loginForm.setPassword(oInputForm.getPassword());
		customerService.getByCid(loginForm, result);

		if (!result.hasErrors()) {
			Reserve reserve = new Reserve();
			reserve = oInputForm.getEntity();
			reserveRepository.saveAndFlush(reserve);
			return "complete";
		} else {
			return "redirect:/Reserve";
		}

	}

	@PostMapping("/setCustomer")
	public String PostComplete(@ModelAttribute CInputForm cInputForm, BindingResult result) {
		if (!result.hasErrors()) {
			String cname = cInputForm.getCname();
			session.setAttribute("cid", cname);
			Customer customer = cInputForm.getEntity();
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
