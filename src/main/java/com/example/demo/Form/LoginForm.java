package com.example.demo.Form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ログイン認証用のフォーム
 * @author soutome
 *
 */
@Data
public class LoginForm {
	
	@NotBlank(message = "利用者IDを入力して下さい")
	private String cid;
	
	private String password;
}
