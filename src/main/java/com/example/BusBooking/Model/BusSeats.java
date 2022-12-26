package com.example.BusBooking.Model;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
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


}
