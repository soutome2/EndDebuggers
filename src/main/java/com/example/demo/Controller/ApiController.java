package com.example.demo.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
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

import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.example.demo.Entity.Review;
import com.example.demo.Form.ReviewInputForm;
import com.example.demo.Repository.ReviewRepository;
import com.example.demo.Service.JsonConverterService;
import com.example.demo.Service.ReviewService;
import com.example.demo.Service.TextAnalyticsService;

import lombok.AllArgsConstructor;

/**
 * webAPI化に関わるコントローラクラス<br>
 * レストコントローラによる表示の為テンプレートhtmlの使用は不可。
 * @author seino
 *
 */
@AllArgsConstructor
@RestController
public class ApiController {
	private final JsonConverterService jsonConverterService;
	private final TextAnalyticsService textAnalyticsService;
	private final ReviewService reviewService;
	private final ReviewRepository reviewRepository;

	/**
	 * 感情分析のお試しページ
	 * @return 画面に結果を表示
	 * @author kachi
	 */
	@GetMapping("/Test")
	public String Test() {
		DocumentSentiment documentSentiment = textAnalyticsService.analyzeSentiment("めちゃくちゃいい人だった。これからも利用したい。");
		// Positive、Negative、Neutralのスコアを数値のみで取得
		double positiveScore = documentSentiment.getConfidenceScores().getPositive();
		double negativeScore = documentSentiment.getConfidenceScores().getNegative();
		double neutralScore = documentSentiment.getConfidenceScores().getNeutral();

		// 数値のみの文字列を作成して返す
		return String.format("Positive score: %.2f, Negative score: %.2f, Neutral score: %.2f",
				positiveScore, negativeScore, neutralScore);
	}

	/**
	 * 
	 * @return
	 * @author seino
	 */
	@GetMapping("/demochan")
	public String demochan() {
		String sentiment = textAnalyticsService.MaxRateSentiment(0.2, 0.4, 0.4);
		return sentiment;
	}

	/**
	 * JSONデータ取得用のパラメータ入力マニュアル
	 * @return 説明文表示
	 * @author kachi
	 */
	@GetMapping("/GetReviewManual")
	public String Manual() {
	    return "JSONデータの取得エンドポイント：/GetReviewJson<br>"
	            + "param:<br>"
	            + "担当者名 'ename' [田中太郎, 佐藤花子, 鈴木一郎, 高橋美咲, 中村健太]<br>"
	            + "評価 'star' [1, 2, 3, 4, 5]<br>"
	            + "感情 'sentiment' [positive,neutral, negative]<br>"
	            + "絞り込み開始日 'startDate' [yyyy-mm-dd]<br>"
	            + "絞り込み終了日 'endDate' [yyyy-mm-dd]<br>"
	            + "並び替え条件 'sortBy' [date, star]<br>"
	            + "昇降順 'order' [true, false]<br>";
	}


	/**
	 * JSONデータ取得用のパラメータ入力マニュアル
	 * @return 説明文表示
	 * @author kachi
	 */
	@GetMapping("/GetInsertReviewManual")
	public String InsertReviewManual() {
		return"<html><body>"
	            + "<p>レビュー書き込みのためのエンドポイント：/PostReview<br>"
	            + "パラメーター:<br>"
	            + "担当者名 'ename' [田中太郎, 佐藤花子, 鈴木一郎, 高橋美咲, 中村健太]<br>"
	            + "レビュータイトル 'title' 任意の文字列<br>"
	            + "レビュー本文 'comment' 任意の文字列<br>"
	            + "評価 'star' [1, 2, 3, 4, 5]<br>"
	            + "URLの例 http://aceconcierge.azurewebsites.net/PostReview?ename=田中太郎&title=いまいちだな&comment=ダメダメ&star=2</p>"
	            + "</body></html>";
				
	
	}

	/**
	 * 
	 * @return
	 * @author seino
	 */
	@GetMapping("/GetReviewJson")
	public String ReviewReturn(
			@RequestParam(value = "ename", required = false) String ename,
			@RequestParam(value = "star", required = false) String star,
			@RequestParam(value = "sentiment", required = false) String sentiment,
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
				System.out.println("starは数値型である必要があります");
				return "{}";
			}
		}

		// startDateの変換
		if (startDate != null) {
			try {
				parsedStartDate = LocalDate.parse(startDate);
			} catch (Exception e) {
				System.out.println("startDateは日付型である必要があります");
				return "{}";
			}
		}

		// endDateの変換
		if (endDate != null) {
			try {
				parsedEndDate = LocalDate.parse(endDate);
			} catch (Exception e) {
				System.out.println("endDateは日付型である必要があります");
				return "{}";
			}
		}

		// orderの変換
		if (order != null) {
			if (order.equalsIgnoreCase("true")) {
				parsedOrder = true;
			} else if (order.equalsIgnoreCase("false")) {
				parsedOrder = false;
			} else {
				System.out.println("orderはブール型である必要があります");
				return "{}";
			}
		}

		if (ename != null) {
			List<Review> list = reviewService.sortReviewList(ename, parsedStar, sentiment, parsedStartDate,
					parsedEndDate, sortBy, parsedOrder);

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

	/**
	 * 
	 * @return
	 * @author seino
	 */
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

	/**
	 * API経由Insertできるかのデモコントローラー
	 * @return
	 * @author seino
	 */
	@GetMapping("/DemoInsertReview")
	@CrossOrigin
	public String demoInsertReview() {
		String baseUrl = "https://aceconcierge.azurewebsites.net";
		String endpoint = "/PostReview";
		String apiUrl = jsonConverterService.MakeFilePathInsert(baseUrl, endpoint,
				"田中太郎", "いまいち", "owata", "2");
		ResponseEntity<String> responseEntity = new RestTemplate().getForEntity(
				apiUrl,
				String.class);
		return responseEntity.getBody();

	}

	/**
	 * GetでInsertのための情報を受け取りFormに詰め込み、実際に書き込むためのControllerに送信
	 * @return
	 * @author seino
	 */
	@GetMapping("/PostReview")
	public String postReview(@RequestParam(value = "ename", required = false) String ename,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "comment", required = false) String comment,
			@RequestParam(value = "star", required = false) Integer star) {

		// リクエストURLを定義
		String apiUrl = "https://aceconcierge.azurewebsites.net/InsertReview";
		//String apiUrl = "http://localhost:8080/InsertReview";
		String cid = "Gest";
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
				String.class);
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

	/**
	 * Formクラスで受け取りバリデーションチェックし書き込む
	 * @return
	 * @author seino
	 */
	@PostMapping("/InsertReview")
	public String InsertReview(@Validated @RequestBody ReviewInputForm reviewInputForm,
			BindingResult result) {
		System.out.println(result);
		
		reviewService.CheckEname(reviewInputForm, result) ;

			
		if (!result.hasErrors()) {
			Review review = reviewInputForm.getEntity();
			LocalDate currentDate = LocalDate.now();
			LocalTime currentTime = LocalTime.now();

			review.setReviewdate(currentDate);
			review.setReviewtime(currentTime);

			//感情分析の結果
			String text = review.getComment();
			DocumentSentiment documentSentiment = textAnalyticsService.analyzeSentiment(text);
			review.setSentiment(documentSentiment.getSentiment().toString());
			review.setPositiverate(documentSentiment.getConfidenceScores().getPositive());
			review.setNeutralrate(documentSentiment.getConfidenceScores().getNeutral());
			review.setNegativerate(documentSentiment.getConfidenceScores().getNegative());
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
