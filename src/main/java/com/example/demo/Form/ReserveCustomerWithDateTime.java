package com.example.demo.Form;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.demo.Entity.ReserveCustomer;

public class ReserveCustomerWithDateTime {

	private ReserveCustomer reserveCustomer;

	private String datetime;

	public ReserveCustomerWithDateTime(ReserveCustomer reserveCustomer, String datetime) {
		this.reserveCustomer = reserveCustomer;
		this.datetime = datetime;
	}

	// Getter methods
	public ReserveCustomer getReserveCustomer() {
		return reserveCustomer;
	}

	public String getDatetime() {
		return datetime;
	}
	
	public String getCid() {
		if (reserveCustomer != null) {
			return reserveCustomer.getCid();
		}
		return null;
	}
	
	public String getCname() {
		if (reserveCustomer != null) {
			return reserveCustomer.getCname();
		}
		return null;
	}
	
	public String getPassword() {
		if (reserveCustomer != null) {
			return reserveCustomer.getPassword();
		}
		return null;
	}
	
	public String getTel() {
		if (reserveCustomer != null) {
			return reserveCustomer.getTel();
		}
		return null;
	}
	
	public String getMailaddress() {
		if (reserveCustomer != null) {
			return reserveCustomer.getMailaddress();
		}
		return null;
	}
	
	public Integer getReserveid() {
		if (reserveCustomer != null) {
			return reserveCustomer.getReserveid();
		}
		return null;
	}

	public String getEname() {
		if (reserveCustomer != null) {
			return reserveCustomer.getEname();
		}
		return null;
	}
	
	public LocalDate getReservedate() {
		if (reserveCustomer != null) {
			return reserveCustomer.getReservedate();
		}
		return null;
	}
	
	public LocalTime getReservetime() {
		if (reserveCustomer != null) {
			return reserveCustomer.getReservetime();
		}
		return null;
	}

	public String getDetail() {
		if (reserveCustomer != null) {
			return reserveCustomer.getDetail();
		}
		return null;
	}
}
