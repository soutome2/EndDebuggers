package com.example.demo.Entity;

import java.sql.Time;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data

public class ReserveCustomer {
	
	@Id
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
	
	@Column(name = "reserveid")
	private Integer reserveid;
	
	@Column(name = "ename")
	private String ename;
	
	@Column(name = "reservedate")
	private Date reservedate;
	
	@Column(name = "reservetime")
	private Time reservetime;
	
	@Column(name = "detail")
	private String detail;

}
