package com.example.demo.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Review;
import com.example.demo.Repository.ReviewRepository;
import com.example.demo.Service.JsonConverterService;

import lombok.AllArgsConstructor;
@AllArgsConstructor
@RestController
public class AceController2 {
	private final JsonConverterService jsonConverterService;
	private final ReviewRepository reviewRepository;
	@GetMapping("/GetReview")
	public String ReviewReturn(@RequestParam("ename") String ename ) {
		List<Review> list = reviewRepository.findAIIByEname(ename);
		String json=jsonConverterService.getReviewToJson(list);
		return json;
	}

}
