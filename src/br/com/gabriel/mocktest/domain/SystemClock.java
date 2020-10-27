package br.com.gabriel.mocktest.domain;

import java.util.Calendar;

public class SystemClock implements Clock {

    @Override
    public Calendar today() {
        return Calendar.getInstance();
    }

}
