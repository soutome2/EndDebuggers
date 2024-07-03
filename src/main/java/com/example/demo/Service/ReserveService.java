package com.example.demo.Service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.demo.Form.ReserveInputForm;
import com.example.demo.Repository.ReserveRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ReserveService {

	private final ReserveRepository reserveRepository;

	public void getByCid(ReserveInputForm reserveInputForm, BindingResult result) {

		LocalDate reservedate = reserveInputForm.getReservedate();
		System.out.println(reservedate);
		return;

	}

}
