package learn.mastery.ui;

import learn.mastery.data.DataException;
import learn.mastery.domain.GuestService;
import learn.mastery.domain.HostService;
import learn.mastery.domain.ReservationService;
import learn.mastery.domain.Result;
import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class View {

    private final ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }

    // Display section headers
    public void displayHeader(String message) {
        io.println("");
        io.println("=".repeat(message.length()));
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    // General purpose message display
    public void displayMessage(String message) {
        io.println(message);
    }

    // Main menu prompt
    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        for (MainMenuOption option : MainMenuOption.values()) {
            io.println(String.format("%d. %s", option.getValue(), option.getLabel()));
        }

        int value = io.readInt("Select [0-4]: ", 0, 4);
        return MainMenuOption.fromValue(value);
    }

    public Guest selectGuest(GuestService service) throws DataException {
        String email = io.readRequiredString("Guest email: ");
        Guest guest = service.findByEmail(email);

        if (guest == null) {
            displayMessage("Guest not found.");
        }
        return guest;
    }

    public Host selectHost(HostService service) throws DataException {
        String email = io.readRequiredString("Host email: ");
        Host host = service.findByEmail(email);

        if (host == null) {
            displayMessage("Host not found.");
        }

        return host;
    }

    public void displayReservations(Host host, List<Reservation> reservations) {
        displayHeader(String.format("%s: %s, %s", host.getLastName(), host.getCity(), host.getState()));

        for (Reservation r : reservations) {
            Guest g = r.getGuest();
            io.printf("ID: %d%nDates: %s - %s%nGuest: %s, %s%nEmail: %s%n%n",
                    r.getId(),
                    r.getStartDate(),
                    r.getEndDate(),
                    r.getGuest().getFirstName(),
                    r.getGuest().getLastName(),
                    r.getGuest().getEmail());
        }
    }

    public Reservation makeReservation(Guest guest, Host host, List<Reservation> existing) {
        displayReservations(host, existing);

        LocalDate start = io.readDate("Start (MM/dd/yyyy): ");
        LocalDate end = io.readDate("End (MM/dd/yyyy: ");

        Reservation reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setHost(host);
        reservation.setStartDate(start);
        reservation.setEndDate(end);

        io.println("\nSummary");
        io.println("=======");
        io.printf("Start: %s%n", start);
        io.printf("End: %s%n", end);

        boolean confirm = io.readBoolean("Is that okay? [y/n]: ");
        return confirm ? reservation : null;
    }

    public Reservation chooseReservation(Guest guest, Host host, List<Reservation> reservations) {
        List<Reservation> matches = reservations.stream()
                .filter(r -> r.getGuest().getId() == guest.getId())
                .collect(Collectors.toList());

        if (matches.isEmpty()) {
            displayMessage("No reservations found for this guest and host.");
            return null;
        }

        displayReservations(host, reservations);
        int id = io.readInt("Reservation ID: ", 1, 100);        // 100 reservation max

        return matches.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);

    }

    public Reservation editReservation(Reservation reservation) {
        LocalDate start = io.readDate(String.format("Start (%s): ", reservation.getStartDate()));
        LocalDate end = io.readDate(String.format("End (%s): ", reservation.getEndDate()));

        if (start != null && !start.equals(reservation.getStartDate())) {
            reservation.setStartDate(start);
        }
        if (end != null && !end.equals(reservation.getEndDate())) {
            reservation.setEndDate(end);
        }

        return reservation;
    }

    public void displayResult(Result<?> result) {
        if (result.isSuccess()) {
            io.println("Success");
            io.println("=======");
        } else {
            io.println("Error(s):");
            for (String message : result.getMessages()) {
                io.println("- " + message);
            }
        }
    }

    public boolean readConfirmCancel() {
        return io.readBoolean("Are you sure you want to cancel this reservation? [y/n]: ");
    }




}
