package com.example.BusBooking.Objects;


import com.example.BusBooking.Model.Buses;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
@Getter
@Setter
public class BusLists {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Buses buses;

    private String vacancySeats;

    private String bTime;

    private String dTime;

    private int noOfSeats;

    private String journeyDate;

    private String indexes;

    private int userId;

    private int cost;

    public BusLists()
    {
        super();
    }

}
