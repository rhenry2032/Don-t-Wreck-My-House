package learn.mastery.data;

import learn.mastery.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {

    static final String TEST_FILE_PATH = Path.of("src", "test", "resources",
                                            "data", "hosts_test.csv").toString();

    HostFileRepository repo;


    @BeforeEach
    void setup() {
        repo = new HostFileRepository(TEST_FILE_PATH);
    }
    // findAll tests
    @Test
    void shouldReadAllHosts() throws DataException {
        HostFileRepository repo = new HostFileRepository(TEST_FILE_PATH);
        List<Host> hosts = repo.findAll();

        assertNotNull(hosts);
        assertEquals(2, hosts.size());

        Host first = hosts.get(0);
        assertEquals("Henry", first.getLastName());
        assertEquals(new BigDecimal("200.00"), first.getStandardRate());
    }

    @Test
    void shouldThrowWhenFileDoesNotExist() {
        GuestFileRepository repo = new GuestFileRepository("nonexistent.csv");

        assertThrows(DataException.class, repo::findAll);
    }

    // findById tests
    @Test
    void shouldFindHostById() throws DataException {
        HostFileRepository repo = new HostFileRepository(TEST_FILE_PATH);
        Host host = repo.findById("zyx-987");

        assertNotNull(host);
        assertEquals("McTest", host.getLastName());
    }

    @Test
    void shouldReturnNullWhenIdNotFound() throws DataException {
        HostFileRepository repo = new HostFileRepository(TEST_FILE_PATH);
        Host host = repo.findById("xyz-999");

        assertNull(host);
    }

    // findByEmail tests
    @Test
    void shouldFindHostByEmail() throws DataException {
        HostFileRepository repo = new HostFileRepository(TEST_FILE_PATH);
        Host host = repo.findByEmail("roger@example.com");

        assertNotNull(host);
        assertEquals("abc-123", host.getId());
        assertEquals("AZ", host.getState());
    }

    @Test
    void shouldReturnNullWhenEmailNotFound() throws DataException {
        HostFileRepository repo = new HostFileRepository(TEST_FILE_PATH);
        Host host = repo.findByEmail("notfound@example.com");

        assertNull(host);
    }

    // Invalid format of .csv file
    @Test
    void shouldSkipInvalidFormatRows() throws DataException {
        String badFilePath = Path.of("src", "test", "resources", "data", "hosts_bad.csv").toString();
        HostFileRepository repo = new HostFileRepository(badFilePath);

        List<Host> hosts = repo.findAll();

        // only one good row expected
        assertEquals(1, hosts.size());
    }

}