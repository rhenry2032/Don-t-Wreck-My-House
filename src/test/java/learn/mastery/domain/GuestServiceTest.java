package learn.mastery.domain;

import learn.mastery.data.DataException;
import learn.mastery.data.GuestRepositoryDouble;
import learn.mastery.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {

    GuestService service;

    @BeforeEach
    void setup() {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("John");
        guest.setLastName("Doe");
        guest.setEmail("test@example.com");
        guest.setPhone("555-555-5555");
        guest.setState("AZ");

        service = new GuestService(new GuestRepositoryDouble(List.of(guest)));
    }

    // findByEmail tests
    @Test
    void shouldFindByEmail() throws DataException {
        Guest result = service.findByEmail("test@example.com");
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void shouldReturnNullForUnknownEmail() throws DataException {
        Guest result = service.findByEmail("unknown@example.com");
        assertNull(result);
    }

    // findByLastName tests
    @Test
    void shouldFindByLastName() throws DataException {
        List<Guest> result = service.findByLastName("Doe");
        assertEquals(1, result.size());
        assertEquals("Doe", result.get(0).getLastName());
    }

    @Test
    void shouldReturnEmptyListForUnknownLastName() throws DataException {
        List<Guest> result = service.findByLastName("Smithsonian");
        assertEquals(0, result.size());
    }

    // findAll test
    @Test
    void shouldReturnAllGuests() throws Exception {
        List<Guest> result = service.findAll();
        assertEquals(1, result.size());
    }

}