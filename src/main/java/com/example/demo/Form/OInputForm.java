package com.example.demo.Form;

import 	java.sql.Time;
import java.util.Date;

import com.example.demo.Entity.Reserve;

import lombok.Data;
@Data
public class OInputForm {

	 private String  cid ;
	 private String password; 
	 private Date  reservedate; 
	 private Time  reservetime;
	 private String detail;
	 
	 public Reserve getEntity() {
			Reserve reserve=new Reserve();
			reserve.setCid(cid);
			reserve.setReservedate(reservedate);
			reserve.setReservetime(reservetime);
			reserve.setDetail(detail);
			
			return reserve;
	 }
}
