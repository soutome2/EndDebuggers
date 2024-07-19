package com.example.demo.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.Review;
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

}
