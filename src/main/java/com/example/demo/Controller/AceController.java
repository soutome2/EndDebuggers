package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class AceController {
	private final HttpSession session;
	
	@GetMapping("/")
	public String GetHome(){
		return "home";
		
	}
	
	@GetMapping("/Login")
	public String GetUserLogin(){
		return "login";
		
	}
	
	@PostMapping("/Reserve")
	public String PostReserve(@RequestParam("ename")  String ename){
		session.setAttribute("ename",ename);
		
		return "reserveInput";
		
	}
	
	


}

