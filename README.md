# Parking Lot Service

Spring Boot 3 / Java 17 / PostgreSQL / Spring Data JPA / Flyway REST API.

## Run
```bash
docker compose up -d      # PostgreSQL on :5432 (db/user/password: parking)
mvn spring-boot:run       # Flyway creates the schema and seeds 20 slots (A-01..A-10, B-01..B-10)
mvn test                  # fee calculation unit tests
```
Swagger UI: http://localhost:8080/swagger-ui.html, OpenAPI JSON: /v3/api-docs  
Postman: import `postman/parking-lot.postman_collection.json`.  
Env overrides: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `PARKING_TIMEZONE` (default Asia/Kolkata).

## Endpoints
| Method | Path | Purpose |
|---|---|---|
| GET | `/api/v1/slots/available` | Real-time availability (counts + free slots) |
| GET | `/api/v1/slots`, `/api/v1/slots/{id}` | All slots / one slot |
| POST | `/api/v1/bookings` | Allocate slot on entry `{vehicleNumber, slotId, entryTime?}` -> 201 |
| GET | `/api/v1/bookings/active` | All occupied slots |
| GET | `/api/v1/bookings/{id}` | One booking |
| GET | `/api/v1/bookings/{id}/fare?at=` | Fare accrued so far (final fare if already checked out) |
| POST | `/api/v1/bookings/{id}/checkout` | Exit `{exitTime?}`; bills, marks PAID, frees slot |
| GET | `/api/v1/reports/revenue?date=yyyy-MM-dd` | Revenue + completed count for a day |

## Pricing
Rs.50 per 3 hours or part thereof, minimum one block: 2h15m -> 50, 3h00m -> 50, 3h05m -> 100.
Computed at second precision in `FeeCalculator`.

## Errors
Every error has the same JSON shape: `{timestamp, status, error, message, path, details[]}`

| Status | When |
|---|---|
| 400 | bean validation, bad vehicle number / id / date, malformed JSON, exit before entry, future timestamps |
| 404 | slot or booking not found, unknown route |
| 409 | slot occupied, vehicle already parked, booking already checked out, concurrent unique-index collision |

## Design notes
- Layers: controller -> service -> repository -> model, with DTO records at the API boundary.
- Concurrency: `SELECT ... FOR UPDATE` on slot (booking) and booking (checkout) rows, backed by partial unique
  indexes (`WHERE exit_time IS NULL`), so double-booking is impossible even if the app-level check is bypassed.
- Schema owned by Flyway; Hibernate runs with `ddl-auto: validate`. Standalone copy in `db/schema.sql`.
- Time: injected `Clock` in the configured zone; timestamps are ISO-8601 `LocalDateTime`.
- Revenue is attributed to the exit date. Checkout marks the booking PAID (payment assumed collected at the gate).
- Vehicle numbers are normalised (upper-case, spaces/hyphens stripped) before storage and comparison.
"# parking-lot-service" 
