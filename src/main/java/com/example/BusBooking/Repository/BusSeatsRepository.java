package com.example.BusBooking.Repository;

import com.example.BusBooking.Model.BusSeats;
import com.example.BusBooking.Model.Buses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusSeatsRepository extends JpaRepository<BusSeats, Integer> {

    @Query(value = "Select seats from bus.busseats where busId = :busId and date = :date",
            nativeQuery = true
    )
    String findSeatsByBusId(String busId, String date);

    @Query(value = "Select busId, seats, date, id, edate from bus.busseats where busId = :busId and date = :date",
            nativeQuery = true
    )
    BusSeats findBusSeatsById(String busId, String date);
}
