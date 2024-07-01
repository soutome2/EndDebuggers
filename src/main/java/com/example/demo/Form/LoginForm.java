package com.example.demo.Form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginForm {
	
	@NotBlank(message = "利用者IDを入力して下さい")
	private String cid;
	
	@NotBlank(message = "パスワードを入力して下さい")
	private String password;
}
