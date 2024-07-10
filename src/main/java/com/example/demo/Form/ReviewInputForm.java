package com.example.demo.Form;

import com.example.demo.Entity.Review;

import lombok.Data;

@Data
public class ReviewInputForm {

	
	private String cid;
	private String cname;
	private String title;
	private String comment;
	private Integer star;
	

	public Review getEntity() {
		Review review = new Review();
		review.setCid(cid);
		review.setCname(cname);
		review.setTitle(title);
		review.setComment(comment);
		review.setStar(star);

		return review;
	}

}
