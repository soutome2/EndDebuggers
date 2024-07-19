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

/**
 * t_reserveテーブルのエンティティ
 * @author soutome
 *
 */
@Entity
@Table(name = "t_reserve")
@Data
public class Reserve {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reserveid")
	private Integer reserveid;

	@Column(name = "cid")
	private String cid;

	@Column(name = "ename")
	private String ename;

	@Column(name = "reservedate")
	private LocalDate reservedate;

	@Column(name = "reservetime")
	private LocalTime reservetime;

	@Column(name = "detail")
	private String detail;



}
