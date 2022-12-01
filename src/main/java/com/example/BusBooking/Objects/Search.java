package com.example.BusBooking.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Search {

    private String start;

    private String end;

    private String date;

    public Search()
    {
        super();
    }

}
