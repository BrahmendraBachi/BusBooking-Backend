package com.example.BusBooking.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BusesNotFoundException extends RuntimeException{

    public BusesNotFoundException(String message){
        super(message);
    }

}
