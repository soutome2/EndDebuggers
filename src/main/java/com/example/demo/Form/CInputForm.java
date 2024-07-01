package com.example.demo.Form;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.Entity.Customer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CInputForm{
	@NotBlank(message = "利用者IDを入力して下さい")
	private String cid;
	
	@NotBlank(message = "利用者IDを入力して下さい")
	private String password;
	
	@NotBlank(message = "利用者IDを入力して下さい")
	private String cname;
	
	@NotBlank(message = "利用者IDを入力して下さい")
	private String tel;
	
	@NotBlank(message = "利用者IDを入力して下さい")
	private String mailaddress;
	
	public Customer getEntity() {
		Customer customer = new Customer();
		customer.setCid(cid);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		customer.setPassword(encoder.encode(password));
		customer.setCname(cname);
		customer.setTel(tel);
		customer.setMailaddress(mailaddress);
		
		return customer;
	}
}
