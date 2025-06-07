package learn.mastery.data;

import learn.mastery.models.Host;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class HostFileRepository implements HostRepository {
    private static final String HEADER = "id,last_name,email,phone,address,city,state," +
            "postal_code,standard_rate,weekend_rate";
    private final String filePath;

    public HostFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Host> findAll() throws DataException {
        List<Host> hosts = new ArrayList<>();
        Path path = Path.of(filePath);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            reader.readLine();      // skips header

            String line;
            while ((line = reader.readLine()) != null) {
                Host host = deserialize(line);
                if (host != null) {
                    hosts.add(host);
                }
            }
        } catch (IOException ex) {
            throw new DataException("Could not read hosts from file: " + filePath, ex);
        }
        return hosts;
    }

    @Override
    public Host findById(String id) throws DataException {
        return findAll().stream()
                .filter(h -> h.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Host findByEmail(String email) throws DataException {
        return findAll().stream()
                .filter(h -> h.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    private Host deserialize(String line) {
        String[] fields = line.split(",", -1);
        if (fields.length != 10) {
            return null;
        }

        Host host = new Host();
        host.setId(fields[0]);
        host.setLastName(fields[1]);
        host.setEmail(fields[2]);
        host.setPhone(fields[3]);
        host.setAddress(fields[4]);
        host.setCity(fields[5]);
        host.setState(fields[6]);
        host.setPostalCode(fields[7]);
        host.setStandardRate(new BigDecimal(fields[8]));
        host.setWeekendRate(new BigDecimal(fields[9]));

        return host;
    }
}
