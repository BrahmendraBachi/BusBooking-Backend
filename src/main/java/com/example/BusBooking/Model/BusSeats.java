package com.example.BusBooking.Model;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "busSeats")
public class BusSeats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "busId")
    private String busId;

    @Column(name = "date")
    private String date;


    @Column(name = "seats")
    private String seats;

    @Column(name = "edate")
    private String edate;

    public BusSeats()
    {
        super();
    }

}
