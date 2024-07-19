package com.example.demo.Service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.demo.Entity.Reserve;
import com.example.demo.Form.ReserveInputForm;
import com.example.demo.Repository.ReserveRepository;

import lombok.AllArgsConstructor;

/**
 * このクラスは予約関連のメソッドを用意するクラスです。<br>
 * エラー処理や複数回使用される処理を保存するのに適しています。
 * @author kachi
 */
@AllArgsConstructor
@Service
public class ReserveService {

	private final ReserveRepository reserveRepository;

	/**
	 * 予約の重複に関わるエラー処理です。
	 * @param reserveInputForm データベースに追加予定の入力予約
	 * @param result エラー表示の保管庫
	 * @author kachi
	 */
	public void getByDateTime(ReserveInputForm reserveInputForm, BindingResult result) {
		
		Optional<Reserve> optCid = reserveRepository.findByReservedateAndReservetimeAndCid(reserveInputForm.getReservedate(), reserveInputForm.getReservetime(), reserveInputForm.getCid());
		
		if (!optCid.isEmpty()) {
			result.addError(new FieldError(
					result.getObjectName(), "reservedate", "あなたはこの時間に別の予約が入っています"));
			return;
		}
		
		Optional<Reserve> optEname = reserveRepository.findByReservedateAndReservetimeAndEname(reserveInputForm.getReservedate(), reserveInputForm.getReservetime(), reserveInputForm.getEname());
		
		if (!optEname.isEmpty()) {
			result.addError(new FieldError(
					result.getObjectName(), "reservedate", "別の利用者と予約が重複しました"));
			return;
		}
		
		return;

	}

}
