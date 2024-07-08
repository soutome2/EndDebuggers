package com.example.demo.Form;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.Entity.Customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerInputForm {

	@NotBlank(message = "利用者IDを入力して下さい")
	@Size(max = 8, message = "IDは8文字以内で入力して下さい")
	private String cid;

	@NotBlank(message = "パスワードを入力して下さい")
	@Size(max = 20, message = "設定可能なパスワードは20文字までです")
	private String password;

	@NotBlank(message = "名前を入力して下さい")
	@Size(max = 60, message = "設定可能な名前は60文字までです")
	private String cname;

	@NotBlank(message = "電話番号を入力して下さい")
	@Pattern(regexp = "^\\d{10,11}$", message = "電話番号は10桁または11桁の数字で入力してください")
	private String tel;

	@NotBlank(message = "メールアドレスを入力して下さい")
	@Email(message = "正しい形式のメールアドレスを入力してください")
	@Size(max = 254, message = "正しい形式のメールアドレスを入力してください")
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