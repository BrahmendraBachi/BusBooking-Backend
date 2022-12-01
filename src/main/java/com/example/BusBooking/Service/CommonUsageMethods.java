package com.example.BusBooking.Service;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@Service
public class CommonUsageMethods {

    public String getTodayDate()
    {
        Date today = new Date();
        return new SimpleDateFormat("yyyy-MM-dd").format(today);
    }

    public String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        return new SimpleDateFormat("HH:mm").format(c.getTime());
    }

}
