package com.example.BusBooking.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookTickets {

    private List<String> bookedSeats;

    private List<PassengerDetails> passengerDetails;

    private String date;

    private String busId;

    private String indexes;

    private int id;



}
