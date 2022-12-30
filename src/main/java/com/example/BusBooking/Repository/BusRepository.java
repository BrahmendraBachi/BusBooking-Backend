package com.example.BusBooking.Repository;

import com.example.BusBooking.Model.Buses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Buses, Integer> {
    @Query(value = "SELECT * FROM BusApplication.buses WHERE busId = '6069'",
            nativeQuery=true
    )
    public List<Buses> findByTitle();

    @Query(value = "SELECT intStations FROM BusApplication.Buses WHERE busId = :busId",
           nativeQuery = true)
    public String findIntStationsByBusId(String busId);

    @Query(value = "SELECT times FROM BusApplication.Buses WHERE busId = :busId",
           nativeQuery = true)
    public String findTimesByBusId(String busId);

    @Query(value = "SELECT nextDay FROM BusApplication.Buses WHERE busId = :busId",
           nativeQuery = true)
    public int findNextDayByBusId(String busId);
}
