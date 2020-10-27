package br.com.gabriel.mocktest.domain;

import java.util.Calendar;

public class Payment {

    private final double amount;
    private final Calendar date;

    public Payment(double amount, Calendar date) {
        this.amount = amount;
        this.date = date;
    }

    public Calendar getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }
}
