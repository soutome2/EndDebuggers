package com.example.demo.Form;

import 	java.sql.Time;
import java.util.Date;

import lombok.Data;
@Data
public class OInputForm {

	 private Integer reserveid;
	 private String  cid ;
	 private String ename;
	 private Date reservedate; 
	 private Time reservetime;
	 private String detail;
}
