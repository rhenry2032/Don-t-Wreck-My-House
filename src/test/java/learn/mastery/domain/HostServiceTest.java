package learn.mastery.domain;

import learn.mastery.data.HostRepositoryDouble;
import learn.mastery.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {

    HostService service;

    @BeforeEach
    void setup() {
        Host host = new Host();
        host.setId("abc-123");
        host.setLastName("Hosty");
        host.setEmail("host@example.com");
        host.setPhone("555-123-4567");
        host.setAddress("123 Hosting Ln");
        host.setCity("Phoenix");
        host.setState("AZ");
        host.setPostalCode("85001");
        host.setStandardRate(new BigDecimal("100.00"));
        host.setWeekendRate(new BigDecimal("150.00"));

        service = new HostService(new HostRepositoryDouble(List.of(host)));
    }

    // findByEmail tests
    @Test
    void shouldFindByEmail() throws Exception {
        Host result = service.findByEmail("host@example.com");
        assertNotNull(result);
        assertEquals("Hosty", result.getLastName());
    }

    @Test
    void shouldReturnNullForUnknownEmail() throws Exception {
        Host result = service.findByEmail("unknown@example.com");
        assertNull(result);
    }

    // findByLastName tests
    @Test
    void shouldFindByLastName() throws Exception {
        List<Host> result = service.findByLastName("Hosty");
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListForUnknownLastName() throws Exception {
        List<Host> result = service.findByLastName("Unknown");
        assertTrue(result.isEmpty());
    }

    // findAll test
    @Test
    void shouldReturnAllHosts() throws Exception {
        List<Host> result = service.findAll();
        assertEquals(1, result.size());
    }
}
