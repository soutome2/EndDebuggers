package com.example.demo.Form;

import com.example.demo.Entity.Review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * t_reviewテーブルへの入力用のフォーム
 * @author soutome
 *
 */
@Data
public class ReviewInputForm {

	@NotBlank(message = "登録済みの利用者IDを入力して下さい")
	private String cid;

	@NotNull(message = "担当者名が設定されていません")
	private String ename;

	@Size(max = 30, message = "タイトルは30文字までです")
	private String title;

	@Size(max = 100, message = "コメントは100文字以内にまとめてください")
	private String comment;

	@Max(value = 5, message = "範囲外の数値が入力されています")
	@Min(value = 0, message = "範囲外の数値が入力されています")
	@NotNull(message = "評価を入力して下さい")
	private Integer star;

	public Review getEntity() {
		Review review = new Review();
		review.setCid(cid);
		review.setEname(ename);
		review.setTitle(title);
		review.setComment(comment);
		review.setStar(star);

		return review;
	}

}
