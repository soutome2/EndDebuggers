package com.example.demo.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Entity.Review;
import com.example.demo.Form.ReviewInputForm;
import com.example.demo.Repository.ReviewRepository;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

/**
 * このクラスはレビュー関連のメソッドを用意するクラスです。<br>
 * エラー処理や複数回使用される処理を保存するのに適しています。
 * @author kachi
 */
@AllArgsConstructor
@Service
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final HttpSession session;

	/**
	 * 
	 * @param reviewList
	 * @return maxRateList
	 * @author seino
	 */
	public List<String> GetMaxRateList(List<Review> reviewList) {

		//3つの感情のうちスコアが最大の感情
		String maxRateSentiment;
		//3つの感情のうちスコアが最大の感情の確率
		String maxRate;

		//3つの感情のうちスコアが最大の感情の確率リスト　reviewListと対応
		List<String> maxRateList = new ArrayList<>();

		//sentimentがnullならなしにした。全く関係ない文字列が入っている場合エラー。それ以外は確率値を文字列に

		for (Review review : reviewList) {

			maxRateSentiment = review.getSentiment();
			if (maxRateSentiment == null) {
				maxRateList.add("なし");

			} else {
				if (maxRateSentiment.equals("positive")) {
					maxRate = String.valueOf((int) (100 * review.getPositiverate()));
					maxRateList.add("ポジティブ:" + maxRate + "%");

				} else if (maxRateSentiment.equals("neutral")) {
					maxRate = String.valueOf((int) (100 * review.getNeutralrate()));
					maxRateList.add("ノーマル:" + maxRate + "%");

				} else if (maxRateSentiment.equals("negative")) {
					maxRate = String.valueOf((int) (100 * review.getNegativerate()));
					maxRateList.add("ネガティブ:" + maxRate + "%");

				} else {
					maxRate = String.valueOf("エラー");
					maxRateList.add(maxRate);
				}
			}

		}

		//今までの条件式を満たさない場合はneutral
		return maxRateList;

	}

	/**
	 * 評価の平均値を計算するメソッドです。
	 * @return sessionのaverageStarsに平均値を保存。
	 * @param ename 担当者の名前
	 * @author kachi
	 */
	public void getAverage(String ename) {

		List<Review> list = reviewRepository.findAIIByEnameOrderByReviewdateDescReviewtimeDesc(ename);

		//平均の計算
		if (list.isEmpty()) {
			// リストが空の場合の処理
			System.out.println("リストが空です。");
		} else {
			// 平均値を計算する
			double totalStars = 0.0;

			// リスト内の各要素からgetStar()を呼び出し、合計を計算する
			for (Review review : list) {
				totalStars += review.getStar();
			}

			// 平均を計算する
			double averageStars = Math.round(totalStars / list.size() * 10.0) / 10.0;

			session.setAttribute("averageStars", averageStars);
		}

	}

	/**
	 * タイトルの記述がないレビューを省いたリストを生成するメソッドです。<br>
	 * 平均値計算はこのメソッドの前に使用してください。
	 * @param list フィルターをかけたいリスト
	 * @return タイトルなしを除いたリスト
	 * @author kachi
	 */
	public List<Review> getFilteredReview(List<Review> list) {

		List<Review> filteredList = new ArrayList<>();

		// 元のリストからgetTitle()が空でない要素だけを抽出して新しいリストを作成する
		for (Review review : list) {
			if (review.getTitle() != null && !review.getTitle().isEmpty()) {
				filteredList.add(review);
			}
		}

		return filteredList;
	}

	/**
	 * リストを開始値と終了値の範囲に基づいてサブリストとして返すメソッドです。
	 * @param list 分割したいリスト
	 * @param startIndex 開始値
	 * @param endIndex 終了値
	 * @return サブリスト
	 * @author kachi
	 */
	public List<Review> getSubReview(List<Review> list, int startIndex, int endIndex) {

		// インデックスがリストの範囲内に収まるように調整
		if (startIndex < 0) {
			startIndex = 0;
		}
		if (endIndex >= list.size()) {
			endIndex = list.size();
		}

		// サブリストを取得
		List<Review> sublist = list.subList(startIndex, endIndex);

		return sublist;

	}

	/**
	 * 
	 * @param review
	 * @return
	 * @author seino
	 */
	public List<Integer> CountSentiment(List<Review> review) {
		//0:positive 1:neutral 2:negativeの合計全て0で初期化
		List<Integer> sentimentSumList = new ArrayList<>(Collections.nCopies(3, 0));
		for (Review i : review) {
			if (i.getSentiment() != null) {
				String sentiment = i.getSentiment();
				if (sentiment.equals("positive")) {
					sentimentSumList.set(0, sentimentSumList.get(0) + 1);
				} else if (sentiment.equals("neutral")) {
					sentimentSumList.set(1, sentimentSumList.get(1) + 1);
				} else if (sentiment.equals("negative")) {
					sentimentSumList.set(2, sentimentSumList.get(2) + 1);
				} else {

				}
			}

		}
		return sentimentSumList;
	}

	/**
	 * ソート機能用の様々な分岐を格納したメソッド。
	 * @param ename 担当者名
	 * @param star 評価
	 * @param sentiment 感情分析
	 * @param startDate 開始日
	 * @param endDate 終了日
	 * @param sortBy 並び替え
	 * @param order 昇順降順
	 * @return 条件に合ったソート結果
	 * @author kachi
	 */
	public List<Review> sortReviewList(
			@RequestParam(value = "ename", required = false) String ename,
			@RequestParam(value = "star", required = false) Integer star,
			@RequestParam(value = "sentiment", required = false) String sentiment,
			@RequestParam(value = "startDate", required = false) LocalDate startDate,
			@RequestParam(value = "endDate", required = false) LocalDate endDate,
			@RequestParam(value = "sortBy", required = false) String sortBy,
			@RequestParam(value = "order", required = false) boolean order) {

		List<Review> list = new ArrayList<>();
		//評価絞り込み
		if (startDate != null && endDate != null) {
			if ("star".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameAndReviewdateGroupOrderByStar(ename, startDate, endDate, star,
								sentiment)
						: reviewRepository.findByEnameAndReviewdateGroupOrderByStarDesc(ename, startDate, endDate,
								star, sentiment);
			} else if ("time".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameAndReviewdateGroupOrderByReviewdateAscReviewtimeAsc(ename,
								startDate, endDate, star, sentiment)
						: reviewRepository.findByEnameAndReviewdateGroupOrderByReviewdateDescReviewtimeDesc(ename,
								startDate, endDate, star, sentiment);
			} else if ("positive".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameAndReviewdateGroupOrderByPositiveAsc(ename,
								startDate, endDate, star, sentiment)
						: reviewRepository.findByEnameAndReviewdateGroupOrderByPositiveDesc(ename,
								startDate, endDate, star, sentiment);
			} else if ("negative".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameAndReviewdateGroupOrderByNegativeAsc(ename,
								startDate, endDate, star, sentiment)
						: reviewRepository.findByEnameAndReviewdateGroupOrderByNegativeDesc(ename,
								startDate, endDate, star, sentiment);
			} else if ("neutral".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameAndReviewdateGroupOrderByNeutralAsc(ename,
								startDate, endDate, star, sentiment)
						: reviewRepository.findByEnameAndReviewdateGroupOrderByNeutralDesc(ename,
								startDate, endDate, star, sentiment);
			} else {
				list = order
						? reviewRepository.findByEnameAndReviewdateGroup(ename, startDate, endDate, star,
								sentiment)
						: reviewRepository.findByEnameAndReviewdateGroupOrderByReviewdateDescReviewtimeDesc(ename,
								startDate, endDate, star, sentiment);
			}
		} else if (startDate != null) {
			if ("star".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameAndStartDateOrderByStar(ename, startDate, star, sentiment)
						: reviewRepository.findByEnameAndStartDateOrderByStarDesc(ename, startDate, star,
								sentiment);
			} else if ("time".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameAndStartDateOrderByReviewdateAscReviewtimeAsc(ename, startDate,
								star, sentiment)
						: reviewRepository.findByEnameAndStartDateOrderByReviewdateDescReviewtimeDesc(ename, startDate,
								star, sentiment);
			} else if ("positive".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameAndStartDateOrderByPositiveAsc(ename, startDate,
								star, sentiment)
						: reviewRepository.findByEnameAndStartDateOrderByPositiveDesc(ename, startDate,
								star, sentiment);
			} else if ("negative".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameAndStartDateOrderByNegativeAsc(ename, startDate,
								star, sentiment)
						: reviewRepository.findByEnameAndStartDateOrderByNegativeDesc(ename, startDate,
								star, sentiment);
			} else if ("neutral".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameAndStartDateOrderByNeutralAsc(ename, startDate,
								star, sentiment)
						: reviewRepository.findByEnameAndStartDateOrderByNeutralDesc(ename, startDate,
								star, sentiment);
			} else {
				list = order ? reviewRepository.findByEnameAndStartDate(ename, startDate, star, sentiment)
						: reviewRepository.findByEnameAndStartDateOrderByReviewdateDescReviewtimeDesc(ename, startDate,
								star, sentiment);
			}
		} else if (endDate != null) {
			if ("star".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameAndEndDateOrderByStar(ename, endDate, star, sentiment)
						: reviewRepository.findByEnameAndEndDateOrderByStarDesc(ename, endDate, star,
								sentiment);
			} else if ("time".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameAndEndDateOrderByReviewdateAscReviewtimeAsc(ename, endDate,
								star, sentiment)
						: reviewRepository.findByEnameAndEndDateOrderByReviewdateDescReviewtimeDesc(ename, endDate,
								star, sentiment);
			} else if ("positive".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameAndEndDateOrderByPositiveAsc(ename, endDate,
								star, sentiment)
						: reviewRepository.findByEnameAndEndDateOrderByPositiveDesc(ename, endDate,
								star, sentiment);
			} else if ("negative".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameAndEndDateOrderByNegativeAsc(ename, endDate,
								star, sentiment)
						: reviewRepository.findByEnameAndEndDateOrderByNegativeDesc(ename, endDate,
								star, sentiment);
			} else if ("neutral".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameAndEndDateOrderByNeutralAsc(ename, endDate,
								star, sentiment)
						: reviewRepository.findByEnameAndEndDateOrderByNeutralDesc(ename, endDate,
								star, sentiment);
			} else {
				list = order ? reviewRepository.findByEnameAndEndDate(ename, endDate, star, sentiment)
						: reviewRepository.findByEnameAndEndDateOrderByReviewdateDescReviewtimeDesc(ename, endDate,
								star, sentiment);
			}
		} else {
			if ("star".equals(sortBy)) {
				list = order ? reviewRepository.findByEnameOrderByStar(ename, star, sentiment)
						: reviewRepository.findByEnameOrderByStarDesc(ename, star, sentiment);
			} else if ("time".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameOrderByReviewdateAscReviewtimeAsc(ename, star, sentiment)
						: reviewRepository.findByEnameOrderByReviewdateDescReviewtimeDesc(ename, star,
								sentiment);
			} else if ("positive".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameOrderByPositiveAsc(ename, star, sentiment)
						: reviewRepository.findByEnameOrderByPositiveDesc(ename, star,
								sentiment);
			} else if ("negative".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameOrderByNegativeAsc(ename, star, sentiment)
						: reviewRepository.findByEnameOrderByNegativeDesc(ename, star,
								sentiment);
			} else if ("neutral".equals(sortBy)) {
				list = order
						? reviewRepository.findByEnameOrderByNeutralAsc(ename, star, sentiment)
						: reviewRepository.findByEnameOrderByNeutralDesc(ename, star,
								sentiment);
			} else {
				list = order ? reviewRepository.findByEname(ename, star, sentiment)
						: reviewRepository.findByEnameOrderByReviewdateDescReviewtimeDesc(ename, star,
								sentiment);
			}
		}
		return list;
	}
	
	/**
	 * API経由でのename存在判定
	 * @param customerInputForm 新規登録の入力フォーム
	 * @param result エラー表示の保管庫
	 * @author seino
	 */
	public void CheckEname(ReviewInputForm reviewInputForm, BindingResult result) {
		
		System.out.println("?");
		List<String> enameList = Arrays.asList("田中太郎", "佐藤花子", "鈴木一郎", "高橋美咲", "中村健太");
		String ename = reviewInputForm.getEname();

		if (enameList.contains(ename)) {
			System.out.println("指定されたenameがリストに存在します");

		} else {
			System.out.println("指定されたenameはリストに存在しません");
			result.addError(new FieldError(
					result.getObjectName(), "ename", "そのコンシェルジュは存在しません"));
		}
		return;

	}

}




