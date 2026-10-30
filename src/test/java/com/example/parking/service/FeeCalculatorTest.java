package com.example.parking.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.parking.exception.InvalidTimeException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FeeCalculatorTest {

    private final FeeCalculator calculator = new FeeCalculator();
    private final LocalDateTime entry = LocalDateTime.of(2026, 10, 29, 9, 0, 0);

    private BigDecimal fee(long minutes) {
        return calculator.calculate(entry, entry.plusMinutes(minutes));
    }

    @ParameterizedTest(name = "{0} minutes -> Rs.{1}")
    @CsvSource({
            "0,50",      // zero duration still costs one block
            "1,50",
            "135,50",    // 2h15m (spec example)
            "179,50",
            "180,50",    // exactly 3h is one block
            "181,100",   // one minute over
            "185,100",   // 3h05m (spec example)
            "360,100",   // exactly 6h
            "361,150",
            "540,150",
            "541,200",
            "1440,400",  // 24h
            "1441,450"
    })
    @DisplayName("charges Rs.50 per 3 hours or fraction thereof")
    void feeBoundaries(long minutes, int expected) {
        assertEquals(0, new BigDecimal(expected).compareTo(fee(minutes)));
    }

    @Test
    @DisplayName("one second past a block boundary starts a new block")
    void secondPrecision() {
        assertEquals(0, new BigDecimal("50").compareTo(calculator.calculate(entry, entry.plusHours(3))));
        assertEquals(0, new BigDecimal("100").compareTo(calculator.calculate(entry, entry.plusHours(3).plusSeconds(1))));
    }

    @Test
    @DisplayName("sub-second remainder is rounded up")
    void nanoRemainder() {
        assertEquals(2, calculator.billableBlocks(entry, entry.plusHours(3).plusNanos(1)));
    }

    @Test
    @DisplayName("stay crossing midnight is billed on total elapsed time")
    void crossesMidnight() {
        LocalDateTime lateEntry = LocalDateTime.of(2026, 10, 29, 23, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 10, 30, 7, 30); // 8h30m -> 3 blocks
        assertEquals(0, new BigDecimal("150").compareTo(calculator.calculate(lateEntry, exit)));
    }

    @Test
    @DisplayName("multi-day stay: 72h = 24 blocks")
    void multiDay() {
        assertEquals(0, new BigDecimal("1200").compareTo(calculator.calculate(entry, entry.plusDays(3))));
    }

    @Test
    @DisplayName("exit equal to entry is billed one block")
    void sameInstant() {
        assertEquals(1, calculator.billableBlocks(entry, entry));
    }

    @Test
    @DisplayName("exit before entry is rejected")
    void exitBeforeEntry() {
        assertThrows(InvalidTimeException.class, () -> calculator.calculate(entry, entry.minusSeconds(1)));
    }

    @Test
    @DisplayName("null timestamps are rejected")
    void nulls() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(null, entry));
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(entry, null));
    }
}
