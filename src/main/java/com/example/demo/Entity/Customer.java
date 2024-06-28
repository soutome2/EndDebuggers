package com.example.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "m_customer")
@Data
public class Customer {
	
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
	

}
