package com.example.BusBooking.Service;

import com.example.BusBooking.Controller.UserController;
import com.example.BusBooking.Exception.BusesNotFoundException;
import com.example.BusBooking.Exception.MethodNotExecutedException;
import com.example.BusBooking.Exception.QueryExecutionError;
import com.example.BusBooking.Model.BookedTickets;
import com.example.BusBooking.Model.BusSeats;
import com.example.BusBooking.Model.Buses;
import com.example.BusBooking.Objects.*;
import com.example.BusBooking.Repository.BookedSeatsRepository;
import com.example.BusBooking.Repository.BusRepository;
import com.example.BusBooking.Repository.BusSeatsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class BusService {

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private BusSeatsRepository busSeatsRepository;

    @Autowired
    private BookedSeatsRepository bookedSeatsRepository;

    @Autowired
    private CommonUsageMethods commonUsageMethods;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    public List<BusLists> findBusLists(Search search) {

        try {

            search.setStart(search.getStart().toLowerCase());
            search.setEnd(search.getEnd().toLowerCase());

            List<Buses> dum = busRepository.findAll().stream().filter(
                    bus -> (
                            new ArrayList<String>(Arrays.asList(bus.getIntStations().split(","))).indexOf(
                                    search.getStart()
                            ) < new ArrayList<String>(Arrays.asList(bus.getIntStations().split(","))).indexOf(
                                    search.getEnd()))).toList();

            dum.forEach(bus -> {
                System.out.println(bus.getBusId());
            });
            try
            {
                logger.info((dum.size() == 1 ? "1 Bus found" : dum.size() + " Buses found"));
                return findBuses(dum, search);
            }
            catch (Exception e)
            {
                logger.error(e.getMessage());
                throw new MethodNotExecutedException("Error has occured in ***findBuses*** method");
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            logger.warn("Buses not found between " + search.getStart() + " to " + search.getEnd());
            throw new BusesNotFoundException("Buses are not available in the route");
        }

    }

    public List<BusLists> findBuses(List<Buses> allBuses, Search search) {

        List<BusLists> busLists = new ArrayList<>();

        for (Buses bus : allBuses)
        {
            List<String> allStations = new ArrayList<String>(Arrays.asList(
                    bus.getIntStations().split(",")));

            int startIndex = allStations.indexOf(search.getStart());
            int endIndex = allStations.indexOf(search.getEnd());

            String modifiedDate = commonUsageMethods.getTodayDate();

            if (modifiedDate.equals(search.getDate())) {
                String getCurrentTime = commonUsageMethods.getCurrentTime();
                if (getCurrentTime.compareTo(bus.getStartTime()) >= 0) {
                    continue;
                } else {
                    String seat = busSeatsRepository.findSeatsByBusId(bus.getBusId(), search.getDate());
                    if (seat == null) {
                        addNewBusDate(bus, search, allStations);
                    }
                }
            } else {
                String seat = busSeatsRepository.findSeatsByBusId(bus.getBusId(), search.getDate());
                if (seat == null) {
                    addNewBusDate(bus, search, allStations);
                }
            }

            String seats = busSeatsRepository.findSeatsByBusId(bus.getBusId(), search.getDate());

            List<String> SeatVacancyAndSeatNo = countNoOfSeats(seats, startIndex, endIndex, allStations.size());


            addBusToSearchList(busLists, bus, SeatVacancyAndSeatNo, startIndex, endIndex, search);
        }
        return busLists;
    }


    private void addBusToSearchList(List<BusLists> busLists, Buses bus,
                                    List<String> SeatVacancyAndSeatNo,
                                    int startIndex, int endIndex, Search search) {

        int noOfSeatsVacant = Integer.parseInt(SeatVacancyAndSeatNo.get(0));

        String seatVacancy = SeatVacancyAndSeatNo.get(1);
        BusLists busfound = new BusLists();

        busfound.setBuses(bus);
        busfound.setNoOfSeats(noOfSeatsVacant);
        String times = bus.getTimes();

        List<String> list = new ArrayList<String>(Arrays.asList(times.split(",")));

        String costs = bus.getCost();
        List<String> list1 = new ArrayList<String>(Arrays.asList(costs.split(",")));
        int cost = 0;
        int cost1 = Integer.parseInt(list1.get(startIndex));
        int cost2 = Integer.parseInt(list1.get(endIndex));
        cost = cost2 - cost1;
        busfound.setCost(cost);
        busfound.setBTime(list.get(startIndex));
        busfound.setDTime(list.get(endIndex));
        busfound.setVacancySeats(seatVacancy);
        busfound.setJourneyDate(search.getDate());
        busfound.setIndexes(startIndex + "," + endIndex);
        busLists.add(busfound);

    }


    private void addNewBusDate(Buses bus, Search search, List<String> allStations) {

        System.out.println("addNewBus is Triggered");

        BusSeats newBus = new BusSeats();

        newBus.setBusId(bus.getBusId());

        newBus.setDate(search.getDate());

        String s = "";

        int length = allStations.size();

        // Building Seats String
        for (int i = 0; i < (25 * length); i++)
        {
            s = s + "1";
        }

        newBus.setSeats(s);
        busSeatsRepository.save(newBus);

    }

    private List<String> countNoOfSeats(String seats, int startIndex, int endIndex, int e) {

        int count = 0;
        List<seatNo> allSeats = new ArrayList<>();
        int iterator = 1;
        String seatVacancy = "";
        int s = 0;
        int d = e;

        while (iterator <= 25) {

            String seatsforId = seats.substring(s, e);
            s = e;
            e = e + d;
            List<String> list = new ArrayList<String>(Arrays.asList(seatsforId.split("")));

            logger.info("Id :" + iterator + " Seats:" + list);

            seatNo seatsById = new seatNo();

            seatsById.setId(iterator);

            seatsById.setSeat(list);

            allSeats.add(seatsById);

            iterator++;

        }

        for (seatNo seat : allSeats) {
            boolean isPresent = false;
            for (int i = startIndex; i < endIndex; i++) {
                if (seat.getSeat().get(i).equals("0")) {
                    isPresent = true;
                    break;
                }
            }
            if (!isPresent) {
                seatVacancy = seatVacancy + seat.getId() + ",";
                count++;
            }
        }
        List<String> dummy = new ArrayList<>();
        dummy.add(String.valueOf(count));
        dummy.add(seatVacancy);
        logger.info(count == 1 ? "1 Seat is available" : count + " Seats are available");
        return dummy;
    }

    public Integer bookTickets(BookTickets tickets) {
        try
        {
            AddTickets(tickets);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            throw new MethodNotExecutedException("AddTicket Method is not executed with some internal server error");
        }


        String busId = tickets.getBusId();

        String date = tickets.getDate();

        String intStations;
        try{
            intStations = busRepository.findIntStationsByBusId(busId);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new QueryExecutionError("Query *findIntStationsByBusId* has an error in BusRepository");
        }


        List<String> ticketIds = tickets.getBookedSeats();

        String indexes = tickets.getIndexes();


        String seats;
        try{
            seats = busSeatsRepository.findSeatsByBusId(busId, date);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new QueryExecutionError("Query *findSeatsByBusId* has an error in the BusSeatsRepository");
        }



        List<String> allSeats = new ArrayList<String>(Arrays.asList(seats.split("")));


        List<String> indexValues = new ArrayList<String>(Arrays.asList(indexes.split(",")));



        int startIndex = Integer.parseInt(indexValues.get(0));
        int endIndex = Integer.parseInt(indexValues.get(1));


        try {
            bookSeats(ticketIds, busId, date, startIndex, endIndex, allSeats, intStations);
        }

        catch (Exception e) {
            logger.error(e.getMessage());
            throw new MethodNotExecutedException("bookSeats method is not executed successfully with Internal error");
        }

        return tickets.getId();

    }

    private void AddTickets(BookTickets tickets) {

        String allTimes;

        List<PassengerDetails> allPassengers = tickets.getPassengerDetails();
        String busId = tickets.getBusId();
        String intStations;
        try{
            intStations = busRepository.findIntStationsByBusId(busId);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new QueryExecutionError("Query *findIntStationsByBusId* has an error in the BusRepository");
        }
        List<String> stations = new ArrayList<String>(Arrays.asList(intStations.split(",")));
        List<String> indexes = new ArrayList<String>(Arrays.asList(tickets.getIndexes().split(",")));
        try{
            allTimes = busRepository.findTimesByBusId(busId);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new QueryExecutionError("Query *findBusTimesByBusId* has an error in the BusRepository");
        }

        List<String> times = new ArrayList<String>(Arrays.asList(allTimes.split(",")));
        int sIndex = Integer.parseInt(indexes.get(0));
        int eIndex = Integer.parseInt(indexes.get(1));


        for (int i = 0; i < allPassengers.size(); i++)
        {

            BookedTickets bookTickets = new BookedTickets();
            bookTickets.setBusId(busId);
            bookTickets.setDate(tickets.getDate());
            bookTickets.setEPlace(stations.get(eIndex));
            bookTickets.setETime(times.get(eIndex));
            bookTickets.setName(allPassengers.get(i).getName());
            bookTickets.setSPlace(stations.get(sIndex));
            bookTickets.setSTime(times.get(sIndex));
            bookTickets.setSeatNo(allPassengers.get(i).getId());
            bookTickets.setUserId(tickets.getId());
            bookTickets.setGender(allPassengers.get(i).getGender());
            bookedSeatsRepository.save(bookTickets);

        }
    }

    private void bookSeats(List<String> ticketIds, String busId, String date, int startIndex, int endIndex, List<String> allSeats, String intStations) {
        int n = new ArrayList<String>(Arrays.asList(intStations.split(","))).size();

        for (String ticketId : ticketIds) {
            int start = Integer.parseInt(ticketId);
            start = ((start - 1) * n) + startIndex;
            System.out.println(start);
            int end = start + endIndex;
            for (int i = start; i < end; i++) {
                allSeats.set(i, "0");
            }
        }
        BusSeats busSeats;
        String seatsString = String.join("", allSeats);
        try {
            busSeats = busSeatsRepository.findBusSeatsById(busId, date);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new QueryExecutionError("Query *findBusSeatsById* has an error in the BusSeatsRepository");
        }

        busSeats.setSeats(seatsString);

        logger.info("Seats are updated");

        busSeatsRepository.save(busSeats);
    }

    public Buses addBus()
    {
        String intStations = "bangalore,chikkaballapur,bagepally,penugonda,anantapur,gutty,dhone,jedcharla,hyderabad";
        String times = "01:30,02:30,03:30,04:30,05:30,07:00,08:30,10:00,10:45";
        String cost = "0,200,400,550,750,830,950,1050,1150";
        Buses bus = new Buses();
        bus.setBusId("1432");
        bus.setBusName("Vikram Travels");
        bus.setStartPlace("bangalore");
        bus.setEndPlace("hyderabad");
        bus.setStartTime("01:30");
        bus.setIntStations(intStations);
        bus.setTimes(times);
        bus.setCost(cost);
        bus.setNextDay(0);

        try
        {
            busRepository.save(bus);
        }

        catch (Exception e)
        {
            logger.trace(e.toString());
            throw new MethodNotExecutedException("Some Internal has occured in the addBus method");
        }

        return bus;
    }

    public Buses addBus1()
    {
        String intStations = "ongole,singaray konda,kavali,nellore,buchireddypalem,sangam,nellorepalem bypass,badvel,mydkur,proddutur,jammalamadugu,kolimigundla,tadipatri,anantapur,sri krishnadevaraya,bathalapalli,dharmavaram,kothacheruvu,puttaparthi";
        String times = "15:00,15:40,16:30,18:30,18:35,19:05,19:35,21:40,22:25,23:05,23:25,00:16,00:25,01:25,01:45,02:15,02:35,03:35,5:15";
        String cost = "0,50,100,150,200,250,300,350,400,450,500,550,600,650,700,750,800,850,900";
        Buses bus = new Buses();
        bus.setBusId("6069");
        bus.setBusName("Ongole - Putapurty Super Luxury");
        bus.setStartPlace("ongole");
        bus.setEndPlace("puttaparti");
        bus.setStartTime("14:55");
        bus.setIntStations(intStations);
        bus.setTimes(times);
        bus.setCost(cost);
        bus.setNextDay(1);
        try
        {
            busRepository.save(bus);
        }

        catch (Exception e)
        {
            logger.trace(e.toString());
            throw new MethodNotExecutedException("Some Internal has occured in the addBus method");
        }

        return bus;
    }


    public Buses addBus2() {

        String intStations = "anantapur,nagasamudram gate,penukonda depot,bagepalli,chikkaballapura,brindavan hotel,devanahalli,bangalore";
        String times = "09:20,10:10,11:10,11:35,13:10,13:20,13:40,14:20";
        String cost = "0,40,80,120,160,200,240,280";
        Buses bus = new Buses();
        bus.setBusId("6701");
        bus.setBusName("Anantapur-Banglore Kempegowda");
        bus.setStartPlace("anantapur");
        bus.setEndPlace("banglore");
        bus.setStartTime("09:20");
        bus.setIntStations(intStations);
        bus.setTimes(times);
        bus.setCost(cost);
        bus.setNextDay(0);
        try
        {
            busRepository.save(bus);
        }

        catch (Exception e)
        {
            logger.trace(e.toString());
            throw new MethodNotExecutedException("Some Internal has occured in the addBus method");
        }

        return bus;

    }

    public Buses addBus3(){
        String intStations = "bangalore,chikkaballapur,bagepally,penugonda,anantapur,gutty,dhone,jedcharla,hyderabad";
        String times = "17:40,18:30,20:00,21:00,22:10,23:20,00:30,01:50,03:35";
        String cost = "0,200,400,550,750,830,950,1050,1150";
        Buses bus = new Buses();
        bus.setBusId("7080");
        bus.setBusName("Banglore - Hyderabad (Garuda)");
        bus.setStartPlace("bengalore");
        bus.setEndPlace("hyderabad");
        bus.setStartTime("17:40");
        bus.setIntStations(intStations);
        bus.setTimes(times);
        bus.setCost(cost);
        bus.setNextDay(0);
        try
        {
            busRepository.save(bus);
        }

        catch (Exception e)
        {
            logger.trace(e.toString());
            throw new MethodNotExecutedException("Some Internal has occured in the addBus method");
        }

        return bus;
    }

    public Buses addBus4(){
        String intStations = "bangalore,coimbatore";
        String times = "22:45,6:00";
        String cost = "0,1400";
        Buses bus = new Buses();
        bus.setBusId("4494");
        bus.setBusName("Jabbar Sleepers");
        bus.setStartPlace("bengalore");
        bus.setEndPlace("coimbatore");
        bus.setStartTime("22:45");
        bus.setIntStations(intStations);
        bus.setTimes(times);
        bus.setCost(cost);
        bus.setNextDay(0);
        try
        {
            busRepository.save(bus);
        }

        catch (Exception e)
        {
            logger.trace(e.toString());
            throw new MethodNotExecutedException("Some Internal has occured in the addBus method");
        }

        return bus;
    }
}
