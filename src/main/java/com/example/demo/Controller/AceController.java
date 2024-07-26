package com.example.demo.Controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class AceController {
	private final HttpSession session;

	@CrossOrigin
	@GetMapping("/")
	public String GetHome() {
		LocalDate date = LocalDate.now();
		session.setAttribute("now", date);
		return "home";

	}

}
