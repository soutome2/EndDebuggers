package com.example.demo.Controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class AceController {
	private final HttpSession session;

	@GetMapping("/")
	public String GetHome() {
		LocalDate date = LocalDate.now();
		session.setAttribute("now", date);
		System.out.println("俺の思いを受け止めて");
		return "home";

	}

}
