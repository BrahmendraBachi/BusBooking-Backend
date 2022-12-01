package com.example.BusBooking.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerDetails {

    private String id;

    private String name;

    private String age;

    private String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public PassengerDetails()
    {
        super();
    }

}
