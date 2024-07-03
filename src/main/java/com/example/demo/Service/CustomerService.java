package com.example.demo.Service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.demo.Entity.Customer;
import com.example.demo.Form.CustomerInputForm;
import com.example.demo.Form.LoginForm;
import com.example.demo.Form.ReserveInputForm;
import com.example.demo.Repository.CustomerRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CustomerService {

	private final CustomerRepository customerRepository;

	public Customer getByCid(LoginForm loginForm, BindingResult result) {

		if (loginForm.getCid().trim().isEmpty() || loginForm.getCid().trim().equals(" ")) {
			return null;
		}

		Optional<Customer> opt = customerRepository.findById(loginForm.getCid());

		if (opt.isEmpty()) {
			result.addError(new FieldError(
					result.getObjectName(), "cid", "存在しない利用者IDです"));
			return null;
		}

		if (loginForm.getPassword().trim().isEmpty() || loginForm.getPassword().trim().equals(" ")) {
			result.addError(new FieldError(
					result.getObjectName(), "password", "パスワードを入力して下さい"));
			return null;
		}

		String rawPassword = loginForm.getPassword(); // ユーザーが入力した平文のパスワード

		// データベースから取得したハッシュ化されたパスワード
		Customer check = opt.get();
		String storedPassword = check.getPassword();

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		// パスワードを比較して認証する
		if (!encoder.matches(rawPassword, storedPassword)) {
			result.addError(new FieldError(
					result.getObjectName(), "password", "パスワードが合致しません"));
			return null;
		}
		return check;

	}

	public Customer getByCid(ReserveInputForm reserveInputForm, BindingResult result) {

		String cid = reserveInputForm.getCid();
		if (cid == null || cid.trim().isEmpty() || cid.trim().equals(" ")) {
			return null;
		}

		Optional<Customer> opt = customerRepository.findById(reserveInputForm.getCid());

		if (opt.isEmpty()) {
			result.addError(new FieldError(
					result.getObjectName(), "cid", "存在しない利用者IDです"));
			return null;
		}

		if (reserveInputForm.getPassword().trim().isEmpty() || reserveInputForm.getPassword().trim().equals(" ")) {
			result.addError(new FieldError(
					result.getObjectName(), "password", "パスワードを入力して下さい"));
			return null;
		}

		String rawPassword = reserveInputForm.getPassword(); // ユーザーが入力した平文のパスワード

		// データベースから取得したハッシュ化されたパスワード
		Customer check = opt.get();
		String storedPassword = check.getPassword();

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		// パスワードを比較して認証する
		if (!encoder.matches(rawPassword, storedPassword)) {
			result.addError(new FieldError(
					result.getObjectName(), "password", "パスワードが合致しません"));
			return null;
		}
		return check;

	}
	
	public void setCustomer(CustomerInputForm customerInputForm, BindingResult result) {

		if (customerInputForm.getCid().trim().isEmpty() || customerInputForm.getCid().trim().equals(" ")) {
			return;
		}

		Optional<Customer> opt = customerRepository.findById(customerInputForm.getCid());

		if (!opt.isEmpty()) {
			result.addError(new FieldError(
					result.getObjectName(), "cid", "そのIDは使用されています"));
			return;
		}
		
	}

}
