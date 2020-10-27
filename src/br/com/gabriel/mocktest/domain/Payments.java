package br.com.gabriel.mocktest.domain;

import java.util.Calendar;

public class Payments {

	private double amount;
	private Calendar date;

	public Payments(double amount, Calendar date) {
		this.amount = amount;
		this.date = date;
	}
	public double getAmount() {
		return amount;
	}
	public Calendar getDate() {
		return date;
	}
}
