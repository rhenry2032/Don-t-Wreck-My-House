package learn.mastery.data;

import learn.mastery.models.Guest;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {

    static final String TEST_FILE_PATH = Path.of("src", "test", "resources",
            "data", "guests_test.csv").toString();

    // findAll tests
    @Test
    void shouldReadAllGuests() throws DataException {
        GuestFileRepository repo = new GuestFileRepository(TEST_FILE_PATH);
        List<Guest> guests = repo.findAll();

        assertNotNull(guests);
        assertEquals(2, guests.size());

        Guest first = guests.get(0);
        assertEquals(1, first.getId());
        assertEquals("John", first.getFirstName());
        assertEquals("Doe", first.getLastName());
    }

    @Test
    void shouldThrowWhenFileDoesNotExist() {
        GuestFileRepository repo = new GuestFileRepository("nonexistent.csv");

        assertThrows(DataException.class, repo::findAll);
    }

    // findById tests
    @Test
    void shouldFindGuestById() throws DataException {
        GuestFileRepository repo = new GuestFileRepository(TEST_FILE_PATH);
        Guest guest = repo.findById(2);

        assertNotNull(guest);
        assertEquals("Jane", guest.getFirstName());
    }

    @Test
    void shouldReturnNullWhenIdNotFound() throws DataException {
        GuestFileRepository repo = new GuestFileRepository(TEST_FILE_PATH);
        Guest guest = repo.findById(999);

        assertNull(guest);
    }

    // findByEmail tests
    @Test
    void shouldFindGuestByEmail() throws DataException {
        GuestFileRepository repo = new GuestFileRepository(TEST_FILE_PATH);
        Guest guest = repo.findByEmail("john.doe@example.com");

        assertNotNull(guest);
        assertEquals(1, guest.getId());
        assertEquals("CA", guest.getState());
    }

    @Test
    void shouldReturnNullWhenEmailNotFound() throws DataException {
        GuestFileRepository repo = new GuestFileRepository(TEST_FILE_PATH);
        Guest guest = repo.findByEmail("notfound@example.com");

        assertNull(guest);
    }

    // Invalid format of .csv file
    @Test
    void shouldSkipInvalidFormatRows() throws DataException {
        GuestFileRepository repo = new GuestFileRepository("src/test/resources/data/guests_bad.csv");
        List<Guest> guests = repo.findAll();

        assertNotNull(guests);
        assertEquals(1, guests.size()); // Only the well-formed row should be loaded
    }

}