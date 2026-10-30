package com.example.parking.service;

import com.example.parking.exception.InvalidTimeException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

/**
 * Pure pricing engine: Rs.50 for every 3 hours or part thereof, minimum one block.
 * 0h00m -> 50, 2h15m -> 50, 3h00m -> 50, 3h01m -> 100, 3h05m -> 100, 6h01m -> 150.
 * Stateless and framework-free so it is trivially unit-testable.
 */
@Component
public class FeeCalculator {

    public static final BigDecimal RATE_PER_BLOCK = new BigDecimal("50.00");
    public static final long BLOCK_SECONDS = Duration.ofHours(3).getSeconds();

    public long billableBlocks(LocalDateTime entry, LocalDateTime exit) {
        if (entry == null || exit == null) {
            throw new IllegalArgumentException("entry and exit times must not be null");
        }
        if (exit.isBefore(entry)) {
            throw new InvalidTimeException("Exit time " + exit + " cannot be before entry time " + entry);
        }
        Duration d = Duration.between(entry, exit);
        long seconds = d.getSeconds() + (d.getNano() > 0 ? 1 : 0);   // any fraction rounds up
        long blocks = (seconds + BLOCK_SECONDS - 1) / BLOCK_SECONDS;  // ceiling division
        return Math.max(1, blocks);                                   // minimum one block
    }

    public BigDecimal calculate(LocalDateTime entry, LocalDateTime exit) {
        return RATE_PER_BLOCK.multiply(BigDecimal.valueOf(billableBlocks(entry, exit)));
    }
}
