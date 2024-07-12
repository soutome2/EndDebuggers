package com.example.demo.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
	
	@CrossOrigin
	@GetMapping("/GetReview")
	public String getReview() {
		// Web API にGETリクエストを送信し、レスポンスを受け取る
		ResponseEntity<String> responseEntity = new RestTemplate().getForEntity(
				"https://aceconcierge.azurewebsites.net/GetReviewJson?ename=田中太郎",
				String.class);

		// レスポンスのボディを取得
		String responseBody = responseEntity.getBody();
		System.out.println("レスポンスボディ: " + responseBody);

		// 必要に応じてレスポンスを処理する
		return "OKかも";
	}
}
