package com.example.BusBooking;

import com.example.BusBooking.Controller.BusController;
import com.example.BusBooking.Model.BookedTickets;
import com.example.BusBooking.Model.Buses;
import com.example.BusBooking.Objects.BookTickets;
import com.example.BusBooking.Objects.PassengerDetails;
import com.example.BusBooking.Objects.Search;
import com.example.BusBooking.Repository.BusRepository;
import com.example.BusBooking.Service.BusService;
import com.example.BusBooking.Service.CommonUsageMethods;
import com.example.BusBooking.Service.UserService;
import com.mysql.cj.protocol.a.UtilCalendarValueEncoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.criteria.CriteriaBuilder;
import java.awt.print.Book;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class BusRepositoryTests {

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private BusService busService;

    @Autowired
    private BusController busController;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonUsageMethods commonUsageMethods;

    @Test
    void checkAddBus(){
        Assertions.assertEquals(true, busService.addBus() != null);
    }

    @Test
    void checkFindBuses(){
        Search search = new Search();
        search.setStart("Bangalore");
        search.setEnd("Hyderabad");
        search.setDate("12-12-2022");
        Assertions.assertNotNull(busService.findBusLists(search));
    }



//  {bookedSeats: Array(1), date: '2022-12-12', busId: '7080', indexes: '0,8', id: '2', …}

    @Test
    void checkBookSeats()
    {
        BookTickets ticket = new BookTickets();

        List<String> seats = new ArrayList<String>();

        seats.add("23");

        ticket.setBookedSeats(seats);

        ticket.setBusId("7080");
        ticket.setDate("2022-12-12");
        ticket.setId(2);

        List<PassengerDetails> passengerDetails = new ArrayList<>();
        PassengerDetails passenger = new PassengerDetails();

        passenger.setName("Bachi");
        passenger.setAge("22");
        passenger.setGender("Male");
        passenger.setId("23");
        passengerDetails.add(passenger);


        ticket.setPassengerDetails(passengerDetails);

        ticket.setIndexes("0,8");

        Assertions.assertEquals(2, busController.bookTickets(ticket));
    }

    @Test
    void checkCancelBooking() throws ParseException {
        Assertions.assertTrue(userService.deleteTrip(157) != null);
    }

    @Test
    void checkCompletedTrips() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(commonUsageMethods.getTodayDate());
        try {
            Date date2 = sdf.parse(userService.getCompletedTrips(2).get(0).getDate());
            Assertions.assertTrue(date2.before(date1));
        }
        catch (IndexOutOfBoundsException e)
        {
            Assertions.assertTrue(userService.getCompletedTrips(2) != null);
        }

    }

    @Test
    void checkOnLiveTrips() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(commonUsageMethods.getTodayDate());


        try {
            BookedTickets ticket = userService.getOnLiveTrips(2).get(0);

            Date date2 = sdf.parse(ticket.getDate());

            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");


            Date currTime = sdf1.parse(commonUsageMethods.getCurrentTime());

            Date time1 = sdf1.parse(ticket.getSTime());

            Assertions.assertTrue((date1.equals(date2)) && (currTime.after(time1) || currTime.equals(time1)));
        }
        catch (IndexOutOfBoundsException e)
        {
            Assertions.assertTrue(userService.getOnLiveTrips(2) != null);
        }
    }

    @Test
    void checkUpcomingTrips() throws ParseException {

        try{
            BookedTickets ticket = userService.getUpcomingTrips(2).get(0);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(commonUsageMethods.getTodayDate());

            Date date2 = sdf.parse(ticket.getDate());

            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");


            Date currTime = sdf1.parse(commonUsageMethods.getCurrentTime());

            Date time1 = sdf1.parse(ticket.getSTime());

            Assertions.assertTrue(((date1.before(date2)) || (date1.equals(date2) && currTime.before(time1))));

        }
        catch (IndexOutOfBoundsException e){
            Assertions.assertTrue(userService.getUpcomingTrips(2) != null);
        }

    }




}
