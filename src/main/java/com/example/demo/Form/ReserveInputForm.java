package com.example.demo.Form;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.demo.Entity.Reserve;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReserveInputForm {

	@NotBlank(message = "登録済みの利用者IDを入力して下さい")
	private String cid;

	private String ename;

	private String password;

	@NotNull(message = "日付が未入力です")
	private LocalDate reservedate;

	@NotNull(message = "時間が未入力です")
	private LocalTime reservetime;

	private String detail;

	public Reserve getEntity() {
		Reserve reserve = new Reserve();
		reserve.setCid(cid);
		reserve.setEname(ename);
		reserve.setReservedate(reservedate);
		reserve.setReservetime(reservetime);
		reserve.setDetail(detail);

		return reserve;
	}

}