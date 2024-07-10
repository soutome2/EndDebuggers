package com.example.demo.Form;

import com.example.demo.Entity.Review;

import lombok.Data;

@Data
public class ReviewInputForm {

	
	private String cid;
	private String ename;
	private String title;
	private String comment;
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
