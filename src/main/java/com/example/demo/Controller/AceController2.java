package com.example.demo.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.Entity.Review;
import com.example.demo.Form.ReviewInputForm;
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
		return "JSONデータの取得URL：/GetReviewJson		\n"
				+ "param:\n"
				+ "担当者名 String ename [田中太郎, 佐藤花子, 鈴木一郎, 高橋美咲, 中村健太]\n"
				+ "評価 Integer star [1, 2, 3, 4, 5]\n"
				+ "絞り込み開始日 LocalDate startDate [yyyy-mm-dd]\n"
				+ "絞り込み終了日 LocalDate endDate [yyyy-mm-dd]\n"
				+ "並び替え条件 String sortBy [date, star]\n"
				+ "順序 boolean order [true, false]\n";
	}

	@GetMapping("/GetReviewJson")
	public ResponseEntity<?> ReviewReturn(
			@RequestParam(value = "ename", required = false) String ename,
			@RequestParam(value = "star", required = false) String star,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "sortBy", required = false) String sortBy,
			@RequestParam(value = "order", required = false) String order) {

		// 変換後のパラメータを格納する変数
		Integer parsedStar = null;
		LocalDate parsedStartDate = null;
		LocalDate parsedEndDate = null;
		Boolean parsedOrder = true;

		// starの変換
		if (star != null) {
			try {
				parsedStar = Integer.parseInt(star);
			} catch (NumberFormatException e) {
				return ResponseEntity.badRequest().body("starパラメータが整数である必要があります");
			}
		}

		// startDateの変換
		if (startDate != null) {
			try {
				parsedStartDate = LocalDate.parse(startDate);
			} catch (Exception e) {
				return ResponseEntity.badRequest().body("startDateパラメータが日付の形式である必要があります");
			}
		}

		// endDateの変換
		if (endDate != null) {
			try {
				parsedEndDate = LocalDate.parse(endDate);
			} catch (Exception e) {
				return ResponseEntity.badRequest().body("endDateパラメータが日付の形式である必要があります");
			}
		}

		// orderの変換
		if (order != null) {
			if (order.equalsIgnoreCase("true")) {
				parsedOrder = true;
			} else if (order.equalsIgnoreCase("false")) {
				parsedOrder = false;
			} else {
				return ResponseEntity.badRequest().body("orderパラメータがブール値である必要があります");
			}
		}

		if (ename != null) {
			List<Review> list = new ArrayList<>();

			if (startDate != null && endDate != null) {
				if ("star".equals(sortBy)) {
					list = parsedOrder
							? reviewRepository.findByEnameAndReviewdateGroupOrderByStar(ename, parsedStartDate,
									parsedEndDate, parsedStar)
							: reviewRepository.findByEnameAndReviewdateGroupOrderByStarDesc(ename, parsedStartDate,
									parsedEndDate, parsedStar);
				} else if ("time".equals(sortBy)) {
					list = parsedOrder
							? reviewRepository.findByEnameAndReviewdateGroupOrderByReviewdateAscReviewtimeAsc(ename,
									parsedStartDate, parsedEndDate, parsedStar)
							: reviewRepository.findByEnameAndReviewdateGroupOrderByReviewdateDescReviewtimeDesc(ename,
									parsedStartDate, parsedEndDate, parsedStar);
				} else {
					list = parsedOrder
							? reviewRepository.findByEnameAndReviewdateGroup(ename, parsedStartDate, parsedEndDate,
									parsedStar)
							: reviewRepository.findByEnameAndReviewdateGroupOrderByReviewdateDescReviewtimeDesc(ename,
									parsedStartDate, parsedEndDate, parsedStar);
				}
			} else if (startDate != null) {
				if ("star".equals(sortBy)) {
					list = parsedOrder
							? reviewRepository.findByEnameAndStartDateOrderByStar(ename, parsedStartDate, parsedStar)
							: reviewRepository.findByEnameAndStartDateOrderByStarDesc(ename, parsedStartDate,
									parsedStar);
				} else if ("time".equals(sortBy)) {
					list = parsedOrder
							? reviewRepository.findByEnameAndStartDateOrderByReviewdateAscReviewtimeAsc(ename,
									parsedStartDate, parsedStar)
							: reviewRepository.findByEnameAndStartDateOrderByReviewdateDescReviewtimeDesc(ename,
									parsedStartDate, parsedStar);
				} else {
					list = parsedOrder ? reviewRepository.findByEnameAndStartDate(ename, parsedStartDate, parsedStar)
							: reviewRepository.findByEnameAndStartDateOrderByReviewdateDescReviewtimeDesc(ename,
									parsedStartDate, parsedStar);
				}
			} else if (endDate != null) {
				if ("star".equals(sortBy)) {
					list = parsedOrder
							? reviewRepository.findByEnameAndEndDateOrderByStar(ename, parsedEndDate, parsedStar)
							: reviewRepository.findByEnameAndEndDateOrderByStarDesc(ename, parsedEndDate, parsedStar);
				} else if ("time".equals(sortBy)) {
					list = parsedOrder
							? reviewRepository.findByEnameAndEndDateOrderByReviewdateAscReviewtimeAsc(ename,
									parsedEndDate, parsedStar)
							: reviewRepository.findByEnameAndEndDateOrderByReviewdateDescReviewtimeDesc(ename,
									parsedEndDate, parsedStar);
				} else {
					list = parsedOrder ? reviewRepository.findByEnameAndEndDate(ename, parsedEndDate, parsedStar)
							: reviewRepository.findByEnameAndEndDateOrderByReviewdateDescReviewtimeDesc(ename,
									parsedEndDate, parsedStar);
				}
			} else if (star != null) {
				if ("star".equals(sortBy)) {
					list = parsedOrder ? reviewRepository.findAIIByEnameAndStarOrderByStar(ename, parsedStar)
							: reviewRepository.findAIIByEnameAndStarOrderByStarDesc(ename, parsedStar);
				} else if ("time".equals(sortBy)) {
					list = parsedOrder
							? reviewRepository.findAIIByEnameAndStarOrderByReviewdateAscReviewtimeAsc(ename, parsedStar)
							: reviewRepository.findAIIByEnameAndStarOrderByReviewdateDescReviewtimeDesc(ename,
									parsedStar);
				} else {
					list = parsedOrder ? reviewRepository.findAIIByEnameAndStar(ename, parsedStar)
							: reviewRepository.findAIIByEnameAndStarOrderByReviewdateDescReviewtimeDesc(ename,
									parsedStar);
				}
			} else {
				if ("star".equals(sortBy)) {
					list = parsedOrder ? reviewRepository.findAIIByEnameOrderByStar(ename)
							: reviewRepository.findAIIByEnameOrderByStarDesc(ename);
				} else if ("time".equals(sortBy)) {
					list = parsedOrder ? reviewRepository.findAIIByEnameOrderByReviewdateAscReviewtimeAsc(ename)
							: reviewRepository.findAIIByEnameOrderByReviewdateDescReviewtimeDesc(ename);
				} else {
					list = parsedOrder ? reviewRepository.findAIIByEname(ename)
							: reviewRepository.findAIIByEnameOrderByReviewdateDescReviewtimeDesc(ename);
				}
			}

			if (list != null && !list.isEmpty()) {
				String json = jsonConverterService.EntityToJson(list);
				return ResponseEntity.ok().body(json);

			} else {
				// エンティティが見つからなかった場合の処理 enameでコンシェルジュの名前以外指定
				System.out.println("entityがnullまたは空");
				return ResponseEntity.ok().body("{}"); // 空のJSONオブジェクトを返す例
			}
		} else

		{
			// enameがしていされなかった
			System.out.println("enameの指定なし");
			return ResponseEntity.ok("enameを入力して下さい"); // enameがnullの場合も空のJSONオブジェクトを返す例
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
	
	@GetMapping("/PostReview")
	public String postReview(@RequestParam(value = "cid", required = false) String cid,@RequestParam(value = "ename", required = false) String ename,
			@RequestParam(value = "title", required = false) String title,@RequestParam(value = "comment", required = false) String comment,@RequestParam(value = "star", required = false) Integer star) {
		
		// リクエストURLを定義
		String apiUrl="http://localhost:8080/InsertReview";
		RestTemplate restTemplate = new RestTemplate();

		// リクエストヘッダーを準備（オプション）
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// 送信するフォームデータを準備
		ReviewInputForm reviewInputForm = new ReviewInputForm();
		System.out.println(cid);
		reviewInputForm.setCid(cid);
		reviewInputForm.setEname(ename);
		reviewInputForm.setTitle(title);
		reviewInputForm.setComment(comment);
		reviewInputForm.setStar(star);


		// HTTPリクエストエンティティを作成
		HttpEntity<ReviewInputForm> requestEntity = new HttpEntity<>(reviewInputForm, headers);

		// HTTPメソッドとしてPOSTを使用してリクエストを送信
		ResponseEntity<String> responseEntity = restTemplate.exchange(
		    apiUrl,
		    HttpMethod.POST,
		    requestEntity,
		    String.class
		);
		System.out.println(responseEntity.getBody());
		HttpStatusCode statusCode = responseEntity.getStatusCode();
		if (statusCode == HttpStatus.CREATED || statusCode == HttpStatus.OK) {
		    // 通信が成功した場合の処理
		    return responseEntity.getBody();
		} else {
		    // 通信が失敗した場合の処理
		    // 例えば、エラーメッセージをログに出力するなど
		    System.out.println("Request failed with status code: " + statusCode);
		    return "Failed to process request";
		}
		
	}
	
	
	@PostMapping("/InsertReview")
	public String InsertReview(@Validated @RequestBody ReviewInputForm reviewInputForm,
			BindingResult result
			) {
		System.out.println(result);
		if (!result.hasErrors()) {
			Review review = reviewInputForm.getEntity();
			LocalDate currentDate = LocalDate.now();
			LocalTime currentTime = LocalTime.now();
			
			review.setReviewdate(currentDate);
			review.setReviewtime(currentTime);
			System.out.println(review);

			reviewRepository.saveAndFlush(review);

		
			return "Review created successfully.";
		} else {

			  StringBuilder errorMessage = new StringBuilder();
	            for (FieldError error : result.getFieldErrors()) {
	                errorMessage.append(error.getField())
	                             .append(": ")
	                             .append(error.getDefaultMessage())
	                             .append("; ");
	            }
	            return "Failed to create review. Errors: " + errorMessage.toString();
		}

	}

	
}
