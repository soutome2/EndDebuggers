package com.example.demo.Service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.demo.Entity.Reserve;
import com.example.demo.Form.ReserveInputForm;
import com.example.demo.Repository.ReserveRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ReserveService {

	private final ReserveRepository reserveRepository;

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
					result.getObjectName(), "reservedate", "この時間の予約は締め切られました"));
			return;
		}
		
		return;

	}

}
