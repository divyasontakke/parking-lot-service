package com.example.parking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.time.Clock;
import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /** Injectable clock so time-dependent logic is deterministic in tests. */
    @Bean
    public Clock clock(@Value("${parking.timezone:Asia/Kolkata}") String timezone) {
        return Clock.system(ZoneId.of(timezone));
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("Parking Lot Service API")
                .version("1.0.0")
                .description("Slot availability, booking, checkout, fare calculation and revenue reporting. "
                        + "Pricing: Rs.50 per 3 hours or part thereof."));
    }
}
