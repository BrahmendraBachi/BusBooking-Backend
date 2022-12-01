package com.example.BusBooking;

import com.example.BusBooking.Model.Users;
import com.example.BusBooking.Repository.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
class UserRepositoryTests {

	@Autowired
	private UsersRepository usersRepository;

	@Test
	@Order(1)
	void addUserTest(){
		Users user = new Users();
		user.setUsername("Bachi");
		user.setPhoneNumber("6303621185");
		user.setEmailId("Bachi6362@gmail.com");
		user.setPassword("Bachi6362");
		usersRepository.save(user);
		Assertions.assertEquals(1, usersRepository.findByUserName("Bachi6362@gmail.com").size());
	}

	@Test
	@Order(2)
	void editUserTest(){
		Users user = usersRepository.findByUserName("Bachi6362@gmail.com").get(0);
		user.setPassword("Bachi@6362");
		usersRepository.save(user);
		Assertions.assertEquals("Bachi@6362",
				usersRepository.findByUserName("Bachi6362@gmail.com").get(0).getPassword());

	}

	@Test
	@Order(3)
	void getUserTest()
	{
		Assertions.assertEquals(1, usersRepository.findByUserName("Bachi6362@gmail.com").size());
	}

	@Test
	@Order(4)
	void deleteUserTest(){
		usersRepository.deleteById(29);
		Assertions.assertEquals(false, usersRepository.findById(29).isPresent());
	}
}
