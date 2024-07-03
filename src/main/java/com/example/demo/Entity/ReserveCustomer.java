package com.example.demo.Entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
@Entity
@Data
public class ReserveCustomer {
	
	@Column(name = "cid")
	private String cid;
	
	@Column(name = "cname")
	private String cname;
	
	@Column(name = "password")
	private String password;

	@Column(name = "tel")
	private String tel;
	
	@Column(name = "mailaddress")
	private String mailaddress;
	
	@Id
	@Column(name = "reserveid")
	private Integer reserveid;
	
	@Column(name = "ename")
	private String ename;
	
	@Column(name = "reservedate")
	private LocalDate reservedate;
	
	@Column(name = "reservetime")
	private LocalTime reservetime;
	
	@Column(name = "detail")
	private String detail;

}
