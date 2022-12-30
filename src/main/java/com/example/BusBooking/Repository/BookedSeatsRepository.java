package com.example.BusBooking.Repository;


import com.example.BusBooking.Model.BookedTickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookedSeatsRepository extends JpaRepository<BookedTickets, Integer> {

    @Query(value = "SELECT * from BusApplication.BookedTickets WHERE userId = :id",
         nativeQuery = true)
    List<BookedTickets> findByUserId(@SuppressWarnings("unused") int id);

    @Query(value = "select * from BusApplication.BookedTickets \n" +
            "where userId = :id \n" +
            "and date < :todayDate ORDER BY DATE DESC",
    nativeQuery = true)
    List<BookedTickets> getCompletedTrips(int id, String todayDate);

    @Query(value = "select * from BusApplication.BookedTickets where userId = :id and date = :todayDate and sTime < :currentTime",
    nativeQuery = true)
    List<BookedTickets> TripsOnLive(int id, String todayDate, String currentTime);

    @Query(value = "Select * from BusApplication.BookedTickets where userId = :id and" +
            "(date > :todayDate or (date = :todayDate and sTime > :currentTime)) ORDER BY DATE, sTIME ASC",
    nativeQuery = true)
    List<BookedTickets> getUpcomingTrips(int id, String todayDate, String currentTime);
}
