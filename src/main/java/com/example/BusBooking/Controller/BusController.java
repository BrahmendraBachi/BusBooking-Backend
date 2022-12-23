package com.example.BusBooking.Controller;


import com.example.BusBooking.Exception.MethodNotExecutedException;
import com.example.BusBooking.Exception.ResourceNotFoundException;
import com.example.BusBooking.Model.BookedTickets;
import com.example.BusBooking.Model.BusSeats;
import com.example.BusBooking.Model.Buses;
import com.example.BusBooking.Objects.BookTickets;
import com.example.BusBooking.Objects.BusLists;
import com.example.BusBooking.Objects.Search;
import com.example.BusBooking.Repository.BusRepository;
import com.example.BusBooking.Repository.BusSeatsRepository;
import com.example.BusBooking.Service.BusService;
import com.example.BusBooking.Service.UserService;
import com.fasterxml.jackson.databind.JsonSerializer;
import org.aspectj.apache.bcel.classfile.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin("*")
public class BusController {

    @Autowired
    private BusService busService;

    @Autowired
    private BusSeatsRepository busSeatsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BusRepository busRepository;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    public void print(){
        System.out.println("print is executed");
    }


    @GetMapping("/setBus")
    public Buses addBus() {



        return busService.addBus();
//        System.out.println("Set Bus is triggered");
//        String intStations = "bangalore,chikkaballapur,bagepally,penugonda,anantapur,gutty,dhone,jedcharla,hyderabad";
//        String times = "01:30,02:30,03:30,04:30,05:30,07:00,08:30,10:00,10:45";
//        String cost = "0,200,400,550,750,830,950,1050,1150";
//        Buses bus = new Buses();
//        bus.setBusId("1432");
//        bus.setBusName("Vikram Travels");
//        bus.setStartPlace("bangalore");
//        bus.setEndPlace("hyderabad");
//        bus.setStartTime("01:30");
//        bus.setIntStations(intStations);
//        bus.setTimes(times);
//        bus.setCost(cost);
//        bus.setNextDay(0);
//        try
//        {
//            busRepository.save(bus);
//        }
//        catch (Exception e)
//        {
//            logger.trace(e.toString());
//            throw new MethodNotExecutedException("Some Internal has occured in the addBus method");
//        }
//        return bus;
    }


    @PostMapping("/getBuses")
    public List<BusLists> getBuses(@RequestBody Search search)
    {
        logger.trace("getBuses is Triggered");
        return busService.findBusLists(search);
    }

    @PostMapping("/bookTickets")
    public Integer bookTickets(@RequestBody BookTickets data)
    {
        logger.trace("BookTickets is triggered");
        return busService.bookTickets(data);
    }




//    @PostMapping("/addSeats")
//    public String addBusDeats(@RequestBody BusSeats busSeats)
//    {
//        String s = "";
//        for(int i=0; i<(25*19); i++)
//        {
//            s = s + "1";
//        }
//        busSeats.setSeats(s);
//        busSeatsRepository.save(busSeats);
//        return "Success";
//    }

//    @GetMapping("/setData")
//    public String setData1()
//    {
//        BusSeats busSeats = busSeatsRepository.findById(1).get();
//        String s = "";
//        for(int i=0; i<(25*19); i++)
//        {
//            s = s + "1";
//        }
//        busSeats.setSeats(s);
//        busSeatsRepository.save(busSeats);
//        return "Success";
//    }

//    @GetMapping("/setMoney")
//    public String setMoney()
//    {
//        Buses bus = busRepository.findById(1).get();
//        String s = "0,50,100,150,200,250,300,350,400,450,500,550,600,650,700,750,800,850,900";
//        bus.setCost(s);
//        busRepository.save(bus);
//        return "success";
//    }

    @GetMapping("/get-completed-trips/{id}")
    public List<BookedTickets> getCompletedTrips(@PathVariable int id) throws ParseException {

        logger.trace("get-completed-trips by Id " + id + " is triggered");
        return userService.getCompletedTrips(id);
    }

    @GetMapping("/get-onLive-trips/{id}")
    public List<BookedTickets> getOnLive(@PathVariable int id) throws ParseException {

        logger.trace("get-onLive-trips by Id " + id + " is triggered");

        return userService.getOnLiveTrips(id);
    }

    @GetMapping("/get-upcoming-trips/{id}")
    public List<BookedTickets> getUpcomingTrips(@PathVariable int id) throws ParseException {

        logger.trace("get-upcoming-trips by Id " + id + "  triggered");
        return userService.getUpcomingTrips(id);
    }

    @DeleteMapping("/cancel-booking/{id}")
    public List<BookedTickets> deleteBookedTickets(@PathVariable int id) throws ParseException {

        logger.trace("Cancel-Booking is triggered");
        try{
            return userService.deleteTrip(id);
        }
        catch (Exception e)
        {
            logger.trace(e.toString());
            throw new MethodNotExecutedException("Cancel Booking has not executed successfully");
        }

    }
}
