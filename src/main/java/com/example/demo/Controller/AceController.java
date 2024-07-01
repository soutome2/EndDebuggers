package com.example.demo.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.Entity.ReserveCustomer;
import com.example.demo.Form.LoginForm;
import com.example.demo.Repository.ReserveCustomerRepository;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class AceController {
	private final ReserveCustomerRepository reserveCustomerRepository;
	private final HttpSession session;
	
	@GetMapping("/")
	public String GetHome(){
		return "home";
		
	}
	
	@GetMapping("/Login")
	public String GetUserLogin(){
		return "login";
		
	}
	
	@PostMapping("/customer")
	public ModelAndView showCostomerList(@ModelAttribute LoginForm loginForm,
			ModelAndView mv) {
		
		String cid = loginForm.getCid();
		List<ReserveCustomer> list = reserveCustomerRepository.findAIIBycustomerId(cid);
		mv.addObject("reserveList", list);
		mv.addObject("cname", "佐々木希");
		mv.setViewName("customer");
		return mv;
	}
	
	@PostMapping("/Reserve")
	public String PostReserve(@RequestParam("ename")  String ename){
		session.setAttribute("ename",ename);
		
		return "reserveInput";
		
	}

}

