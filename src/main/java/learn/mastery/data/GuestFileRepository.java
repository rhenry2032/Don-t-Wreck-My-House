package learn.mastery.data;

import learn.mastery.models.Guest;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GuestFileRepository implements GuestRepository {
    private static final String HEADER = "guest_id,first_name,last_name,email,phone,state";
    private final String filePath;

    public GuestFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Guest> findAll() throws DataException {
        List<Guest> guests = new ArrayList<>();
        Path path = Path.of(filePath);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            reader.readLine();      // skips header

            String line;
            while ((line = reader.readLine()) != null) {
                Guest guest = deserialize(line);
                if (guest != null) {
                    guests.add(guest);
                }
            }
        } catch (IOException ex) {
            throw new DataException("Could not read guests from file: " + filePath, ex);
        }
        return guests;
    }

    @Override
    public Guest findById(int id) throws DataException {
        return findAll().stream()
                .filter(g -> g.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest findByEmail(String email) throws DataException {
        return findAll().stream()
                .filter(g -> g.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    private Guest deserialize(String line) {
        String[] fields = line.split(",", -1);
        if (fields.length != 6){
            return null;
        }

        Guest guest = new Guest();
        guest.setId(Integer.parseInt(fields[0]));
        guest.setFirstName(fields[1]);
        guest.setLastName(fields[2]);
        guest.setEmail(fields[3]);
        guest.setPhone(fields[4]);
        guest.setState(fields[5]);

        return guest;
    }
}
