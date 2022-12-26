package com.example.BusBooking.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BookedTickets")
public class BookedTickets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "userId")
    private int userId;

    @Column(name = "busId")
    private String busId;

    @Column(name = "seatNo")
    private String seatNo;

    @Column(name = "date")
    private String date;

    @Column(name = "sTime")
    private String sTime;

    @Column(name = "eTime")
    private String eTime;
    @Column(name = "name")
    private String name;

    @Column(name = "sPlace")
    private String sPlace;

    @Column(name = "ePlace")
    private String ePlace;

    @Column(name = "gender")
    private String gender;


}
