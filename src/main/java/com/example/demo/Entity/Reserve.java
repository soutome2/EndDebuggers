package com.example.demo.Entity;

import java.sql.Time;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data; 

@Entity
@Table(name = "t_reserve")
@Data

public class Reserve {

	@Id
	@Column(name = "reserveid")
	private Integer reserveid;
	
	@Column(name = "cid")
	private String cid;
	
	@Column(name = "cname")
	private String cname;
	
	@Column(name = "reservedate")
	private Date reservedate;
	
	@Column(name = "reservetime")
	private Time reservetime;
	
	@Column(name = "detail")
	private String detail;
}
