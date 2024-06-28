package com.example.demo.Form;

import 	java.sql.Time;
import java.util.Date;

import lombok.Data;
@Data
public class OInputForm {

	 Integer reserveid;
	 String  cid ;
	 String ename;
	 Date reservedate; 
	 Time reservetime;
	 String detail;
}
