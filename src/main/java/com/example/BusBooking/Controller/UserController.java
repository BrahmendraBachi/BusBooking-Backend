package com.example.BusBooking.Controller;

import com.example.BusBooking.Model.Users;
import com.example.BusBooking.Objects.login;
import com.example.BusBooking.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    // SLF4G implementation and Logback library is used
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/add-user")
    public Users addUser(@RequestBody Users user)
    {
        logger.trace("addUser is Triggered");

        return userService.addUser(user);
    }

    @PostMapping("/login")
    public Users login(@RequestBody login data)
    {
        logger.trace("user logged in");
        return userService.checkUser(data);
    }

    @GetMapping("/get-user-by-id/{id}")
    public Users getUserById(@PathVariable int id)
    {
        logger.trace("getUserById is Triggered");
        return userService.getUserById(id);
    }

    @GetMapping("/check-user-by-emailId/{emailId}")
    public String checkUserByEmailId(@PathVariable String emailId)
    {
        logger.trace("checkUSerByEmailId is Triggered");
        return userService.checkUserByEmailId(emailId);
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestBody login newDetails)
    {
        logger.trace("changePassword is triggered");
        return userService.changePassword(newDetails);
    }
}
