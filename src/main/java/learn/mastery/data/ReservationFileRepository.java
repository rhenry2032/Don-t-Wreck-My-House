package learn.mastery.data;

import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationFileRepository implements ReservationRepository{
    private static final String HEADER = "id,start_date,end_date,guest_id,total";

    private final String directory;
    private final GuestRepository guestRepository;

    public ReservationFileRepository(String directory, GuestRepository guestRepository) {
        this.directory = directory;
        this.guestRepository = guestRepository;
    }

    private Path getFilePath(Host host) {
        return Paths.get(directory, host.getId() + ".csv");
    }

    @Override
    public List<Reservation> findByHost(Host host) throws DataException {
        List<Reservation> reservations = new ArrayList<>();
        Path filePath = getFilePath(host);

        if (!Files.exists(filePath)) {
            return reservations;        // No reservations yet
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            reader.readLine();      // skips header

            String line;
            while ((line = reader.readLine()) != null) {
                Reservation reservation = deserialize(line, host);
                if (reservation != null) {
                    reservations.add(reservation);
                }
            }
        } catch (IOException ex) {
            throw new DataException("Could not read reservations file: " + filePath, ex);
        }
        return reservations;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        List<Reservation> all = findByHost(reservation.getHost());

        int nextId = getNextId(all);
        reservation.setId(nextId);
        all.add(reservation);

        writeAll(all, reservation.getHost());
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        List<Reservation> all = findByHost(reservation.getHost());          // Assumes not null and valid
        boolean updated = false;

        for (int i = 0; i < all.size(); i++){
            if (all.get(i).getId() == reservation.getId()) {
                all.set(i, reservation);
                updated = true;
                break;
            }
        }

        if (updated) {
            writeAll(all, reservation.getHost());
        }

        return updated;
    }

    @Override
    public boolean cancel(Reservation reservation) throws DataException {
        List<Reservation> all = findByHost(reservation.getHost());
        boolean removed = all.removeIf(r -> r.getId() == reservation.getId());

        if (removed) {
            writeAll(all, reservation.getHost());
        }

        return removed;
    }

    // Helper methods
    private Reservation deserialize(String line, Host host) throws DataException {
        String[] fields = line.split(",", -1);
        if (fields.length != 5) {
            return null;
        }

        Reservation reservation = new Reservation();
        reservation.setId(Integer.parseInt(fields[0]));
        reservation.setStartDate(LocalDate.parse(fields[1]));
        reservation.setEndDate(LocalDate.parse(fields[2]));

        int guestId = Integer.parseInt(fields[3]);
        Guest guest = guestRepository.findById(guestId);
        if (guest == null) {
            throw new DataException("Guest ID " + guestId + " not found.");
        }

        reservation.setGuest(guest);
        reservation.setTotal(new BigDecimal(fields[4]));
        reservation.setHost(host);

        return reservation;
    }

    private String serialize(Reservation r) {
        return String.format("%s,%s,%s,%s,%s",
                r.getId(),
                r.getStartDate(),
                r.getEndDate(),
                r.getGuest().getId(),
                r.getTotal());
    }


    private int getNextId(List<Reservation> reservations) {
        int maxId = 0;
        for (Reservation r : reservations) {
            if (r.getId() > maxId) {
                maxId = r.getId();
            }
        }
        return maxId + 1;
    }

    private void writeAll(List<Reservation> reservations, Host host) throws DataException {
        Path filePath = getFilePath(host);

        try (PrintWriter writer = new PrintWriter(filePath.toFile())) {
            writer.println(HEADER);

            for (Reservation r : reservations) {
                writer.println(serialize(r));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException("Could not write to reservation file: " + filePath, ex);
        }
    }


}
