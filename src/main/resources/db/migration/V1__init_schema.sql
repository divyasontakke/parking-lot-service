CREATE TABLE parking_slots (
    id           BIGSERIAL PRIMARY KEY,
    slot_number  VARCHAR(20) NOT NULL UNIQUE,
    occupied     BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE TABLE bookings (
    id             BIGSERIAL PRIMARY KEY,
    vehicle_number VARCHAR(15)   NOT NULL,
    slot_id        BIGINT        NOT NULL REFERENCES parking_slots (id),
    entry_time     TIMESTAMP     NOT NULL,
    exit_time      TIMESTAMP,
    amount         NUMERIC(10,2),
    payment_status VARCHAR(10)   NOT NULL DEFAULT 'PENDING',
    CONSTRAINT chk_exit_after_entry CHECK (exit_time IS NULL OR exit_time >= entry_time),
    CONSTRAINT chk_payment_status   CHECK (payment_status IN ('PENDING', 'PAID'))
);

-- Database-level guarantees against double booking, even under concurrent requests:
CREATE UNIQUE INDEX uq_bookings_active_slot    ON bookings (slot_id)        WHERE exit_time IS NULL;
CREATE UNIQUE INDEX uq_bookings_active_vehicle ON bookings (vehicle_number) WHERE exit_time IS NULL;
CREATE INDEX idx_bookings_exit_time ON bookings (exit_time);
