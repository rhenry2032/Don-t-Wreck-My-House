package learn.mastery.data;

import learn.mastery.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {

    static final Path SEED_PATH = Path.of("src", "test", "resources", "data", "hosts-seed.csv");
    static final Path TEST_PATH = Path.of("src", "test", "resources", "data", "hosts_test.csv");

    HostFileRepository repo;

    @BeforeEach
    void setup() throws IOException {
        Files.copy(SEED_PATH, TEST_PATH, StandardCopyOption.REPLACE_EXISTING);
        repo = new HostFileRepository(TEST_PATH.toString());
    }

    // findAll tests
    @Test
    void shouldReadAllHosts() throws DataException {
        List<Host> hosts = repo.findAll();

        assertNotNull(hosts);
        assertEquals(2, hosts.size());

        Host first = hosts.get(0);
        assertEquals("Henry", first.getLastName());
        assertEquals(new BigDecimal("200.00"), first.getStandardRate());
    }

    @Test
    void shouldThrowWhenFileDoesNotExist() {
        HostFileRepository badRepo = new HostFileRepository("nonexistent.csv");
        assertThrows(DataException.class, badRepo::findAll);
    }

    // findById tests
    @Test
    void shouldFindHostById() throws DataException {
        Host host = repo.findById("zyx-987");

        assertNotNull(host);
        assertEquals("McTest", host.getLastName());
    }

    @Test
    void shouldReturnNullWhenIdNotFound() throws DataException {
        Host host = repo.findById("xyz-999");
        assertNull(host);
    }

    // findByEmail tests
    @Test
    void shouldFindHostByEmail() throws DataException {
        Host host = repo.findByEmail("roger@example.com");

        assertNotNull(host);
        assertEquals("abc-123", host.getId());
        assertEquals("AZ", host.getState());
    }

    @Test
    void shouldReturnNullWhenEmailNotFound() throws DataException {
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