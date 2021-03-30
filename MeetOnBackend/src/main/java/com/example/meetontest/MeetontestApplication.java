package com.example.meetontest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MeetontestApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeetontestApplication.class, args);

    }

}