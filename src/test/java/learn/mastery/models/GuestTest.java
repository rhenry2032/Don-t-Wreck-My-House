package learn.mastery.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuestTest {
    @Test
    void shouldCreateGuestWithValidFields() {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Roger");
        guest.setLastName("Henry");
        guest.setEmail("roger@example.com");
        guest.setPhone("1234567890");
        guest.setState("AZ");

        assertEquals("Henry", guest.getLastName());
        assertEquals("AZ", guest.getState());
        assertEquals("roger@example.com", guest.getEmail());
    }
}