package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AceController {
	
	@GetMapping("/AceService")
	public String GetAceServiceHome(){
		return "home";
		
	}
	
	@GetMapping("/AceServiceUserLogin")
	public String GetUserLogin(){
		return "login";
		
	}
	


}

