package learn.mastery.domain;

import learn.mastery.data.DataException;
import learn.mastery.data.ReservationRepositoryDouble;
import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    ReservationService service;
    ReservationRepositoryDouble repo;
    Host host;
    Guest guest;

    @BeforeEach
    void setup() {
        repo = new ReservationRepositoryDouble();
        service = new ReservationService(repo);

        guest = new Guest(1, "John", "Doe", "john@example.com", "1234567890", "AZ");

        host = new Host("abc-123", "Henry", "host@example.com", "5555555555",
                "123 Main St", "Phoenix", "AZ", "85001",
                new BigDecimal("100.00"), new BigDecimal("150.00"));
    }

    // Add tests
    @Test
    void shouldAddValidReservation() throws DataException {
        Reservation res = new Reservation();
        res.setGuest(guest);
        res.setHost(host);
        res.setStartDate(LocalDate.now().plusDays(5));
        res.setEndDate(LocalDate.now().plusDays(8)); // 3 nights


        Result<Reservation> result = service.addReservation(res);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(3, result.getPayload().getTotal()
                .divide(new BigDecimal("100"), RoundingMode.HALF_UP).intValue());
    }

    @Test
    void shouldNotAddReservationWithNullGuest() throws DataException {
        Reservation res = new Reservation();
        res.setHost(host);
        res.setStartDate(LocalDate.now().plusDays(5));
        res.setEndDate(LocalDate.now().plusDays(7));

        Result<Reservation> result = service.addReservation(res);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Guest is required."));
    }

    @Test
    void shouldNotAddReservationWithOverlap() throws DataException {
        Reservation existing = new Reservation();
        existing.setId(1);
        existing.setGuest(guest);
        existing.setHost(host);
        existing.setStartDate(LocalDate.now().plusDays(5));
        existing.setEndDate(LocalDate.now().plusDays(8));
        existing.setTotal(new BigDecimal("450.00"));

        repo.addExisting(existing);

        Reservation newRes = new Reservation();
        newRes.setGuest(guest);
        newRes.setHost(host);
        newRes.setStartDate(LocalDate.now().plusDays(7));
        newRes.setEndDate(LocalDate.now().plusDays(9));

        Result<Reservation> result = service.addReservation(newRes);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Reservation dates overlap with an existing reservation."));
    }

    @Test
    void shouldNotAddIfStartDateIsInPast() throws DataException {
        Reservation res = new Reservation();
        res.setGuest(guest);
        res.setHost(host);
        res.setStartDate(LocalDate.now().minusDays(1));
        res.setEndDate(LocalDate.now().plusDays(1));

        Result<Reservation> result = service.addReservation(res);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Start date must be in the future."));
    }

    @Test
    void shouldNotAddIfEndDateBeforeStartDate() throws DataException {
        Reservation res = new Reservation();
        res.setGuest(guest);
        res.setHost(host);
        res.setStartDate(LocalDate.now().plusDays(10));
        res.setEndDate(LocalDate.now().plusDays(8));

        Result<Reservation> result = service.addReservation(res);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("End date must be after start date."));
    }

    // Update tests
    @Test
    void shouldUpdateValidReservation() throws DataException {
        Reservation original = new Reservation();
        original.setId(1);
        original.setGuest(guest);
        original.setHost(host);
        original.setStartDate(LocalDate.now().plusDays(10));
        original.setEndDate(LocalDate.now().plusDays(12));
        original.setTotal(new BigDecimal("300.00"));

        repo.addExisting(original);

        Reservation updated = new Reservation();
        updated.setId(1);
        updated.setGuest(guest);
        updated.setHost(host);
        updated.setStartDate(LocalDate.now().plusDays(15));
        updated.setEndDate(LocalDate.now().plusDays(18));

        Result<Reservation> result = service.updateReservation(updated);

        //System.out.println(result.getMessages());
        assertTrue(result.isSuccess());
        assertEquals(1, result.getPayload().getId());
        assertEquals(LocalDate.now().plusDays(15), result.getPayload().getStartDate());
    }

    @Test
    void shouldNotUpdateIfReservationDoesNotExist() throws DataException {
        Reservation fake = new Reservation();
        fake.setId(999);
        fake.setGuest(guest);
        fake.setHost(host);
        fake.setStartDate(LocalDate.now().plusDays(15));
        fake.setEndDate(LocalDate.now().plusDays(17));

        Result<Reservation> result = service.updateReservation(fake);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Reservation does not exist."));
    }

    @Test
    void shouldNotUpdateIfDatesOverlap() throws DataException {
        Reservation existing = new Reservation();
        existing.setId(1);
        existing.setGuest(guest);
        existing.setHost(host);
        existing.setStartDate(LocalDate.now().plusDays(10));
        existing.setEndDate(LocalDate.now().plusDays(13));

        repo.addExisting(existing);

        Reservation overlap = new Reservation();
        overlap.setId(2);
        overlap.setGuest(guest);
        overlap.setHost(host);
        overlap.setStartDate(LocalDate.now().plusDays(12));
        overlap.setEndDate(LocalDate.now().plusDays(15));

        repo.addExisting(overlap);

        // Attempt to update ID 2 to overlap with ID 1
        overlap.setStartDate(LocalDate.now().plusDays(11));
        overlap.setEndDate(LocalDate.now().plusDays(14));

        Result<Reservation> result = service.updateReservation(overlap);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Reservation dates overlap with an existing reservation."));
    }

    @Test
    void shouldNotUpdateIfDatesInvalid() throws DataException {
        Reservation existing = new Reservation();
        existing.setId(1);
        existing.setGuest(guest);
        existing.setHost(host);
        existing.setStartDate(LocalDate.now().plusDays(10));
        existing.setEndDate(LocalDate.now().plusDays(12));

        repo.addExisting(existing);

        Reservation updated = new Reservation();
        updated.setId(1);
        updated.setGuest(guest);
        updated.setHost(host);
        updated.setStartDate(LocalDate.now().plusDays(10));
        updated.setEndDate(LocalDate.now().plusDays(8)); // invalid

        Result<Reservation> result = service.updateReservation(updated);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("End date must be after start date."));
    }

    // Cancel tests
    @Test
    void shouldCancelFutureReservation() throws DataException {
        Reservation future = new Reservation();
        future.setId(1);
        future.setGuest(guest);
        future.setHost(host);
        future.setStartDate(LocalDate.now().plusDays(10));
        future.setEndDate(LocalDate.now().plusDays(12));
        future.setTotal(new BigDecimal("300.00"));

        repo.addExisting(future);

        Result<Void> result = service.cancelReservation(future);

        assertTrue(result.isSuccess());
        assertEquals(0, result.getMessages().size());
    }

    @Test
    void shouldNotCancelPastReservation() throws DataException {
        Reservation past = new Reservation();
        past.setId(2);
        past.setGuest(guest);
        past.setHost(host);
        past.setStartDate(LocalDate.now().minusDays(5));
        past.setEndDate(LocalDate.now().minusDays(2));

        repo.addExisting(past);

        Result<Void> result = service.cancelReservation(past);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Only future reservations can be canceled."));
    }

    @Test
    void shouldNotCancelNonexistentReservation() throws DataException {
        Reservation missing = new Reservation();
        missing.setId(999);
        missing.setGuest(guest);
        missing.setHost(host);
        missing.setStartDate(LocalDate.now().plusDays(10));
        missing.setEndDate(LocalDate.now().plusDays(12));

        Result<Void> result = service.cancelReservation(missing);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Reservation does not exist."));
    }

    @Test
    void shouldNotCancelWithMissingHost() throws DataException {
        Reservation r = new Reservation();
        r.setId(1);
        r.setGuest(guest);
        r.setStartDate(LocalDate.now().plusDays(5));
        r.setEndDate(LocalDate.now().plusDays(8));

        Result<Void> result = service.cancelReservation(r);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Host is required."));
    }


}