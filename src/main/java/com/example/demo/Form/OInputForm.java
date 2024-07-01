package com.example.demo.Form;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.demo.Entity.Reserve;

import lombok.Data;
@Data
public class OInputForm {

	 private String  cid ;
	 private String  ename ;
	 private String password; 
	 private LocalDate reservedate;  
	 private LocalTime reservetime;
	 private String detail;
	 
	 public Reserve getEntity() {
			Reserve reserve=new Reserve();
			reserve.setCid(cid);
			reserve.setEname(ename);
			reserve.setReservedate(reservedate);
			reserve.setReservetime(reservetime);
			reserve.setDetail(detail);
			
			return reserve;
	 }
}
