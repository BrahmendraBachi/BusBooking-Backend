package com.example.BusBooking.Repository;


import com.example.BusBooking.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    @Query(value = "Select * from bus.users where emailId = :username",
            nativeQuery = true
    )
    List<Users> findByUserName(String username);

    @Query(value = "Select * from bus.users u where u.emailId = :username and u.password = :password",
    nativeQuery = true)
    List<Users> isUserCredentialsPresent(String username, String password);
}
