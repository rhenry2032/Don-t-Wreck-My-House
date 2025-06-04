package learn.mastery.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {
    @Test
    void shouldCreateReservationWithValidFields() {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Shelba");

        Host host = new Host();
        host.setId("abc-123");

        Reservation reservation = new Reservation();
        reservation.setId(13);
        reservation.setGuest(guest);
        reservation.setHost(host);
        reservation.setStartDate(LocalDate.of(2025, 7, 31));
        reservation.setEndDate(LocalDate.of(2025, 8, 5));
        reservation.setTotal(new BigDecimal("2090.00"));

        assertEquals(13, reservation.getId());
        assertEquals("Shelba", reservation.getGuest().getFirstName());
        assertEquals("abc-123", reservation.getHost().getId());
        assertEquals(new BigDecimal("2090.00"), reservation.getTotal());
    }

}