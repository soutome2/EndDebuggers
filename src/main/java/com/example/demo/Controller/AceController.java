package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AceController {
	
	@GetMapping("/")
	public String GetHome(){
		return "home";
		
	}
	
	@GetMapping("/Login")
	public String GetUserLogin(){
		return "login";
		
	}
	


}

