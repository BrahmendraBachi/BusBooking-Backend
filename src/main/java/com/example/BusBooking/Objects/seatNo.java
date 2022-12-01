package com.example.BusBooking.Objects;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
@Getter
@Setter
public class seatNo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private List<String> seat;

    public seatNo()
    {
        super();
    }


}
