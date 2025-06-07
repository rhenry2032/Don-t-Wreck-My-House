package learn.mastery.data;

import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {

    final String TEST_DIR = "src/test/resources/data/reservations_test";
    ReservationRepository repo;
    Host host;

    @BeforeEach
    void setup() {
        Guest guest1 = new Guest(1, "John", "Doe", "john@example.com", "1234567890", "AZ");
        Guest guest2 = new Guest(2, "Jane", "Smith", "jane@example.com", "5555555555", "AZ");

        GuestRepository guestRepo = new GuestRepositoryDouble(List.of(guest1, guest2));

        repo = new ReservationFileRepository(TEST_DIR, guestRepo);
        host = new Host();
        host.setId("abc-123");
    }

    @AfterEach
    void cleanupTempFiles() {
        try {
            Files.deleteIfExists(Path.of(TEST_DIR, "invalid-guest.csv"));
        } catch (IOException e) {
            // Log or ignore silently if file couldn't be deleted
            System.err.println("Cleanup failed for invalid-guest.csv: " + e.getMessage());
        }
    }

    // findAll tests
    @Test
    void shouldFindAllReservationsForHost() throws DataException {
        List<Reservation> result = repo.findByHost(host);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenFileMissing() throws DataException {
        Host missingHost = new Host();
        missingHost.setId("missing-999");

        List<Reservation> result = repo.findByHost(missingHost);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowWhenGuestIdNotFound() {
        Host invalidGuestHost = new Host();
        invalidGuestHost.setId("invalid-guest");

        Path path = Path.of(TEST_DIR, "invalid-guest.csv");
        String csvData =
                "id,start_date,end_date,guest_id,total\n" +
                        "1,2025-10-01,2025-10-03,999,500.00\n";

        try {
            Files.write(path, csvData.getBytes());
        } catch (IOException e) {
            fail("Failed to create invalid test file.");
        }

        assertThrows(DataException.class, () -> repo.findByHost(invalidGuestHost));
    }


    // add tests
    @Test
    void shouldAddReservation() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);

        Reservation res = new Reservation();
        res.setHost(host);
        res.setGuest(guest);
        res.setStartDate(LocalDate.of(2025, 9, 1));
        res.setEndDate(LocalDate.of(2025, 9, 3));
        res.setTotal(new BigDecimal("700.00"));

        Reservation added = repo.add(res);
        assertNotNull(added);
        assertTrue(added.getId() > 0);
    }

    @Test
    void shouldNotAddReservationWithMissingGuest() {
        Reservation reservation = new Reservation();
        reservation.setHost(host);
        reservation.setStartDate(LocalDate.of(2025, 12, 1));
        reservation.setEndDate(LocalDate.of(2025, 12, 5));
        reservation.setTotal(new BigDecimal("1000.00"));
        // guest is not set

        assertThrows(NullPointerException.class, () -> repo.add(reservation));
    }


    // update tests
    @Test
    void shouldUpdateReservation() throws DataException {
        List<Reservation> all = repo.findByHost(host);
        Reservation original = all.get(0);
        original.setTotal(new BigDecimal("2000.00"));

        boolean updated = repo.update(original);
        assertTrue(updated);
    }

    @Test
    void shouldNotUpdateNonexistentReservation() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);

        Reservation reservation = new Reservation();
        reservation.setId(999);
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setStartDate(LocalDate.of(2025, 12, 10));
        reservation.setEndDate(LocalDate.of(2025, 12, 15));
        reservation.setTotal(new BigDecimal("1300.00"));

        boolean updated = repo.update(reservation);
        assertFalse(updated);
    }


    // cancel tests
    @Test
    void shouldCancelReservation() throws DataException {
        List<Reservation> all = repo.findByHost(host);
        Reservation toCancel = all.get(1);

        boolean success = repo.cancel(toCancel);
        assertTrue(success);
    }

    @Test
    void shouldReturnFalseWhenCancelingNonexistent() throws DataException {
        Reservation fake = new Reservation();
        fake.setId(999);
        fake.setHost(host);

        boolean result = repo.cancel(fake);
        assertFalse(result);
    }


    // Invalid format of .csv file
    @Test
    void shouldSkipInvalidFormatRows() throws DataException {
        Host badHost = new Host();
        badHost.setId("abc-bad");

        List<Reservation> reservations = repo.findByHost(badHost);
        assertNotNull(reservations);
        assertEquals(2, reservations.size());
    }
}