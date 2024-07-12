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

	@GetMapping("/GetReviewJson")
	public String ReviewReturn(@RequestParam(value = "ename", required = false) String ename) {
		if (ename != null) {
			List<Review> list = reviewRepository.findAIIByEname(ename);

			if (list != null && !list.isEmpty()) {
				String json = jsonConverterService.getReviewToJson(list);
				return json;
			} else {
				// エンティティが見つからなかった場合の処理
				return "{}"; // 空のJSONオブジェクトを返す例
			}
		} else {
			return "{}"; // enameがnullの場合も空のJSONオブジェクトを返す例
		}
	}
}
