package learn.mastery.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class HostTest {

    @Test
    void shouldCreateHostWithValidFields() {
        Host host = new Host();
        host.setId("abc-123");
        host.setLastName("Henry");
        host.setEmail("roger@example.com");
        host.setPhone("1234567890");
        host.setAddress("123 Main St");
        host.setCity("Phoenix");
        host.setState("AZ");
        host.setPostalCode("85001");
        host.setStandardRate(new BigDecimal("100.00"));
        host.setWeekendRate(new BigDecimal("150.00"));

        assertEquals("abc-123", host.getId());
        assertEquals("Henry", host.getLastName());
        assertEquals(new BigDecimal("100.00"), host.getStandardRate());
    }

}