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
		
		Optional<Reserve> opt = reserveRepository.findByReservedateAndReservetime(reserveInputForm.getReservedate(), reserveInputForm.getReservetime());

		if (!opt.isEmpty()) {
			result.addError(new FieldError(
					result.getObjectName(), "reservedate", "予約がいっぱいです"));
			return;
		}
		
		return;

	}

}
