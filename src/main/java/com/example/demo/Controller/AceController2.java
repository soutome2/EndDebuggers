package com.example.demo.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
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
	
	@GetMapping("/GetReviewManual")
	public String Manual() {
		return
				"JSONデータの取得URL：/GetReviewJson		\n"
				+ "param:\n"
				+ "担当者名 String ename [田中太郎, 佐藤花子, 鈴木一郎, 高橋美咲, 中村健太]\n"
				+ "評価 Integer star [1, 2, 3, 4, 5]\n"
				+ "絞り込み開始日 LocalDate startDate [yyyy-mm-dd]\n"
				+ "絞り込み終了日 LocalDate endDate [yyyy-mm-dd]\n"
				+ "並び替え条件 String sortBy [date, star]\n"
				+ "順序 boolean order [true, false]\n";
	}

	@GetMapping("/GetReviewJson")
	public String ReviewReturn(@RequestParam(value = "ename", required = false) String ename,
			@RequestParam(value = "star", required = false) Integer star,
			@RequestParam(value = "startDate", required = false) LocalDate startDate,
			@RequestParam(value = "endDate", required = false) LocalDate endDate,
			@RequestParam(value = "sortBy", required = false) String sortBy,
			@RequestParam(value = "order", required = false) boolean order) {
		if (ename != null) {
			List<Review> list = new ArrayList<>();

			if (startDate != null && endDate != null) {
			    if ("star".equals(sortBy)) {
			        list = order ?
			                reviewRepository.findByEnameAndReviewdateGroupOrderByStar(ename, startDate, endDate, star) :
			                reviewRepository.findByEnameAndReviewdateGroupOrderByStarDesc(ename, startDate, endDate, star);
			    } else if ("time".equals(sortBy)) {
			        list = order ?
			                reviewRepository.findByEnameAndReviewdateGroupOrderByReviewdateAscReviewtimeAsc(ename, startDate, endDate, star) :
			                reviewRepository.findByEnameAndReviewdateGroupOrderByReviewdateDescReviewtimeDesc(ename, startDate, endDate, star);
			    } else {
			        list = order ?
			                reviewRepository.findByEnameAndReviewdateGroup(ename, startDate, endDate, star) :
			                reviewRepository.findByEnameAndReviewdateGroupOrderByReviewdateDescReviewtimeDesc(ename, startDate, endDate, star);
			    }
			} else if (startDate != null) {
			    if ("star".equals(sortBy)) {
			        list = order ?
			                reviewRepository.findByEnameAndStartDateOrderByStar(ename, startDate, star) :
			                reviewRepository.findByEnameAndStartDateOrderByStarDesc(ename, startDate, star);
			    } else if ("time".equals(sortBy)) {
			        list = order ?
			                reviewRepository.findByEnameAndStartDateOrderByReviewdateAscReviewtimeAsc(ename, startDate, star) :
			                reviewRepository.findByEnameAndStartDateOrderByReviewdateDescReviewtimeDesc(ename, startDate, star);
			    } else {
			        list = order ?
			                reviewRepository.findByEnameAndStartDate(ename, startDate, star) :
			                reviewRepository.findByEnameAndStartDateOrderByReviewdateDescReviewtimeDesc(ename, startDate, star);
			    }
			} else if (endDate != null) {
			    if ("star".equals(sortBy)) {
			        list = order ?
			                reviewRepository.findByEnameAndEndDateOrderByStar(ename, endDate, star) :
			                reviewRepository.findByEnameAndEndDateOrderByStarDesc(ename, endDate, star);
			    } else if ("time".equals(sortBy)) {
			        list = order ?
			                reviewRepository.findByEnameAndEndDateOrderByReviewdateAscReviewtimeAsc(ename, endDate, star) :
			                reviewRepository.findByEnameAndEndDateOrderByReviewdateDescReviewtimeDesc(ename, endDate, star);
			    } else {
			        list = order ?
			                reviewRepository.findByEnameAndEndDate(ename, endDate, star) :
			                reviewRepository.findByEnameAndEndDateOrderByReviewdateDescReviewtimeDesc(ename, endDate, star);
			    }
			} else if (star != null) {
			    if ("star".equals(sortBy)) {
			        list = order ?
			                reviewRepository.findAIIByEnameAndStarOrderByStar(ename, star) :
			                reviewRepository.findAIIByEnameAndStarOrderByStarDesc(ename, star);
			    } else if ("time".equals(sortBy)) {
			        list = order ?
			                reviewRepository.findAIIByEnameAndStarOrderByReviewdateAscReviewtimeAsc(ename, star) :
			                reviewRepository.findAIIByEnameAndStarOrderByReviewdateDescReviewtimeDesc(ename, star);
			    } else {
			        list = order ?
			                reviewRepository.findAIIByEnameAndStar(ename, star) :
			                reviewRepository.findAIIByEnameAndStarOrderByReviewdateDescReviewtimeDesc(ename, star);
			    }
			} else {
			    if ("star".equals(sortBy)) {
			        list = order ?
			                reviewRepository.findAIIByEnameOrderByStar(ename) :
			                reviewRepository.findAIIByEnameOrderByStarDesc(ename);
			    } else if ("time".equals(sortBy)) {
			        list = order ?
			                reviewRepository.findAIIByEnameOrderByReviewdateAscReviewtimeAsc(ename) :
			                reviewRepository.findAIIByEnameOrderByReviewdateDescReviewtimeDesc(ename);
			    } else {
			        list = order ?
			                reviewRepository.findAIIByEname(ename) :
			                reviewRepository.findAIIByEnameOrderByReviewdateDesc(ename);
			    }
			}

			if (list != null && !list.isEmpty()) {
				String json = jsonConverterService.EntityToJson(list);
				return json;

			} else {
				// エンティティが見つからなかった場合の処理 enameでコンシェルジュの名前以外指定
				System.out.println("entityがnullまたは空");
				return "{}"; // 空のJSONオブジェクトを返す例
			}
		} else

		{
			// enameがしていされなかった
			System.out.println("enameの指定なし");
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
		List<Review> list = jsonConverterService.JsonToEntity(responseBody);
		System.out.println("エンテティ");
		System.out.println(list);
		System.out.println("レスポンスボディ: " + responseBody);

		// 必要に応じてレスポンスを処理する
		return "OKかも";
	}
}
