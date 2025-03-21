package com.gamenest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import com.gamenest.util.DataSeeder;

@SuppressWarnings("unused")
@EnableAsync
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }   

    @Bean
    CommandLineRunner init(DataSeeder dataSeeder) {
        return args -> {
            dataSeeder.seedDatabase(0);
        };
    }

}