package com.example.BusBooking.Service;

import com.example.BusBooking.Controller.UserController;
import com.example.BusBooking.Exception.DuplicateUserException;
import com.example.BusBooking.Exception.MethodNotExecutedException;
import com.example.BusBooking.Exception.QueryExecutionError;
import com.example.BusBooking.Exception.ResourceNotFoundException;
import com.example.BusBooking.Model.BookedTickets;
import com.example.BusBooking.Model.BusSeats;
import com.example.BusBooking.Model.Users;
import com.example.BusBooking.Objects.login;
import com.example.BusBooking.Repository.BookedSeatsRepository;
import com.example.BusBooking.Repository.BusRepository;
import com.example.BusBooking.Repository.BusSeatsRepository;
import com.example.BusBooking.Repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BookedSeatsRepository bookedSeatsRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private BusSeatsRepository busSeatsRepository;

    @Autowired
    private CommonUsageMethods commonUsageMethods;

    private Logger logger = LoggerFactory.getLogger(UserController.class);


    public Users addUser(Users user) {
        logger.trace("addUser method is triggered");

        if(usersRepository.findByUserName(user.getEmailId()).size() == 0)
        {
            usersRepository.save(user);
        }
        else{
            logger.error("User already exists");
            throw new DuplicateUserException("User with emailId already exist. Try with some other emailId");
        }
        return user;
    }

    public Users checkUser(login data) {
        String username = data.getUsername();
        try {
            return usersRepository.isUserCredentialsPresent(data.getUsername(), data.getPassword()).get(0);
        }
        catch (IndexOutOfBoundsException e)
        {
            logger.error("User Not Found");
            throw new ResourceNotFoundException("User Credentials not found");
        }
    }

    public Users getUserById(int id) {
        try {
            return usersRepository.findById(id).get();
        }

        catch (Exception e)
        {
            logger.error("User with id" + id + "not found");
            throw new ResourceNotFoundException("UserId does not Exist");
        }
    }

    public List<BookedTickets> getCompletedTrips(int id){
        try {
            bookedSeatsRepository.findByUserId(id);
        }
        catch (NullPointerException e)
        {
            logger.trace(e.toString());
            throw new ResourceNotFoundException("Data with userId " + id + " not present");
        }

        Date today = new Date();
        String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(today);

        try {
            return bookedSeatsRepository.getCompletedTrips(id, commonUsageMethods.getTodayDate());
        }
        catch (Exception e)
        {
            logger.trace(e.toString());
            throw new QueryExecutionError("Query for completedTrips is not executed successfully in the method *** getCompletedTrips ***");
        }
    }

    public List<BookedTickets> getOnLiveTrips(int id){
        try{
            bookedSeatsRepository.findByUserId(id);
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            throw new ResourceNotFoundException("Data with userId " + id + " not present");
        }

        try{
            return bookedSeatsRepository.TripsOnLive(id, commonUsageMethods.getTodayDate(), commonUsageMethods.getCurrentTime());
        }
        catch (Exception e){
            logger.error(e.toString());
            throw new QueryExecutionError("Query for TripsOnLive is not executed successfully in the method *** getONLIveTrips ***");
        }
    }

    public List<BookedTickets> getUpcomingTrips(int id){

        try{
            bookedSeatsRepository.findByUserId(id);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            throw new ResourceNotFoundException("Data with userId " + id + " not present");
        }

        try {
            return bookedSeatsRepository.getUpcomingTrips(id, commonUsageMethods.getTodayDate(), commonUsageMethods.getCurrentTime());
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            throw new QueryExecutionError("Query for upComingTrips is not executed successfully in the method *** getUpcomingTrips ***");
        }


    }

    public List<BookedTickets> deleteTrip(int id) throws ParseException {

        BookedTickets ticket = bookedSeatsRepository.findById(id).get();

        System.out.println(ticket.getName());

        if(ticket != null)
        {
            try
            {
                CancelBooking(ticket);
            }

            catch (Exception e)
            {
                logger.error(e.getMessage());
                logger.error("Error occurred in CancelBooking method");
                throw new MethodNotExecutedException("Some Internal Error has occurred while Cancelling the ticket");
            }

            int userId = ticket.getUserId();

            bookedSeatsRepository.deleteById(id);

            return getUpcomingTrips(userId);
        }
        logger.error("Ticket id is not present");
        throw new ResourceNotFoundException("Ticked with id " + id + " is not found");
    }

    private void CancelBooking(BookedTickets ticket) {
        String seatNo = ticket.getSeatNo();
        BusSeats busSeats = new BusSeats();
        try {
            busSeats = busSeatsRepository.findBusSeatsById(ticket.getBusId(),ticket.getDate());
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new QueryExecutionError("Query *findBusSeatsById* has an error in the BusSeatsRepository");
        }

        String seats = busSeats.getSeats();
        String intStations;
        try{
            intStations = busRepository.findIntStationsByBusId(ticket.getBusId());
        }
        catch (Exception e){

            logger.error(e.getMessage());
            throw new QueryExecutionError("Query *findIntStationsByBusId* has an error in the BusRepository");

        }
        List<String> stations = new ArrayList<String>(Arrays.asList(intStations.split(",")));
        int len = stations.size();
        List<String> allSeats = new ArrayList<String>(Arrays.asList(seats.split("")));
        int s = ((Integer.parseInt(seatNo)-1)*len)+stations.indexOf(ticket.getSPlace());
        int e = s + stations.indexOf(ticket.getEPlace());
        System.out.println("Seat no:"+seatNo+" s:"+s);
        for(int i=s;i<e;i++)
        {
            allSeats.set(i,"1");
        }
        String seatsString = String.join("", allSeats);
        busSeats.setSeats(seatsString);
        busSeatsRepository.save(busSeats);
    }

    public String checkUserByEmailId(String emailId)
    {
        System.out.println(emailId);
        List<Users> user;
        try {
            user = usersRepository.findByUserName(emailId);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new QueryExecutionError("Query *findByUserName* has an error in the userRepository");
        }
        if(user.size()>0)
        {
            return "success";
        }
        else{
            throw new ResourceNotFoundException("User EmailId does not exists");
        }
    }

    public String changePassword(login newDetails) {
        try{
            Users user = usersRepository.findByUserName(newDetails.getUsername()).get(0);
            user.setPassword(newDetails.getPassword());
            usersRepository.save(user);
            return "Success";
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            throw new ResourceNotFoundException("User EmailId not found");
        }
    }

}
