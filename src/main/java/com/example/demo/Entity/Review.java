package com.example.demo.Entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "t_review")
@Data

public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reviewid")
	private Integer reviewid;
	
	@Column(name = "cid")
	private String cid;
	
	@Column(name = "ename")
	private String ename;
	
	@Column(name = "reviewdate")
	private LocalDate reviewdate;
	
	@Column(name = "reviewtime")
	private LocalTime reviewtime;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "comment")
	private String comment;
	
	@Column(name = "star")
	private Integer star;

}
