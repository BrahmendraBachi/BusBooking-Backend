package com.example.BusBooking.Model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Buses")
public class Buses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "busId")
    private String busId;

    @Column(name = "busName")
    private String busName;

    @Column(name = "startPlace")
    private String startPlace;

    @Column(name = "endPlace")
    private String endPlace;

    @Column(name = "startTime")
    private String startTime;

    @Column(name = "intStations")
    private String intStations;

    @Column(name = "times")
    private String times;

    @Column(name = "nextDay")
    private int nextDay;

    @Column(name = "cost")
    private String cost;

}
