package com.example.parking;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParkingApplication {

    public static void main(String[] args) {
        // Align JVM default zone with the business zone so @PastOrPresent and Clock agree.
        TimeZone.setDefault(TimeZone.getTimeZone(
                System.getenv().getOrDefault("PARKING_TIMEZONE", "Asia/Kolkata")));
        SpringApplication.run(ParkingApplication.class, args);
    }
}
