package com.gamenest.util;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import com.gamenest.model.*;
import com.gamenest.repository.RoleRepository;
import com.gamenest.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public void seedDatabase(int max) {
		Faker faker = new Faker(new Locale("en-US"));

		Instant start = Instant.now();

		if (roleRepository.findByName("ROLE_ADMIN").isPresent()) {
			log.info("Data already seeded! skipping seeder...");
			return;
		}

		// Create roles
		Role adminRole = roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
		Role userRole = roleRepository.save(Role.builder().name("ROLE_USER").build());

		log.info("Seeding started...");

		// Create admin user and employee
		userRepository.save(User.builder()
				.email("admin@youcode.ma")
				.username("admin")
				.password(passwordEncoder.encode("password"))
				.role(adminRole)
				.build());

		log.info("Seeding completed. Created {} records", max);

		long timeTaken = (Instant.now().toEpochMilli() - start.toEpochMilli()) / 1000;

		log.info("Seeding complete : time taken " + timeTaken + " s");

	}

	// private String generatePhoneNumber() {
	// Random random = new Random();
	// StringBuilder phoneNumber = new StringBuilder("+");

	// // Add country code (e.g., +232)
	// phoneNumber.append(random.nextInt(900) + 100);

	// // Add 9-digit phone number
	// for (int i = 0; i < 9; i++) {
	// phoneNumber.append(random.nextInt(10));
	// }

	// return phoneNumber.toString();
	// }

}
