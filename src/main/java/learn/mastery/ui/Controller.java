package learn.mastery.ui;

import learn.mastery.data.DataException;
import learn.mastery.domain.GuestService;
import learn.mastery.domain.HostService;
import learn.mastery.domain.ReservationService;
import learn.mastery.domain.Result;
import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;

import java.time.LocalDate;
import java.util.List;

public class Controller {

    private final View view;
    private final GuestService guestService;
    private final HostService hostService;
    private final ReservationService reservationService;

    public Controller(View view, GuestService guestService, HostService hostService, ReservationService reservationService) {
        this.view = view;
        this.guestService = guestService;
        this.hostService = hostService;
        this.reservationService = reservationService;
    }

    public void run() throws DataException {
        view.displayHeader("Don't Wreck My House");
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS:
                    view.printHeader("View Reservations for Host");
                    viewReservations();
                    break;
                case ADD_RESERVATION:
                    view.printHeader("Add Reservation");
                    makeReservations();
                    break;
                case EDIT_RESERVATION:
                    view.printHeader("Edit Reservation");
                    editReservation();
                    break;
                case CANCEL_RESERVATION:
                    view.printHeader("Cancel Reservation");
                    cancelReservation();
                    break;
            }
        } while (option != MainMenuOption.EXIT);
        view.displayMessage("\nThanks for not wrecking my house! Goodbye!");
    }

    private void viewReservations() throws DataException {
        Host host = view.selectHost(hostService);

        if (host == null) {
            return;
        }

        List<Reservation> reservations = reservationService.findByHost(host);
        if (reservations.isEmpty()) {
            view.displayMessage("No reservations found.");
            return;
        }

        view.displayReservations(host, reservations);
    }

    private void makeReservations() throws DataException {
        Guest guest = view.selectGuest(guestService);
        if (guest == null){
            return;
        }

        Host host = view.selectHost(hostService);
        if (host == null) {
            return;
        }

        List<Reservation> existing = reservationService.findByHost(host);
        Reservation reservation = view.makeReservation(guest, host, existing, reservationService);

        if (reservation == null) {
            return;
        }

        Result<Reservation> result = reservationService.addReservation(reservation);
        view.displayResult(result, "created");
    }

    private void editReservation() throws DataException {
        Guest guest = view.selectGuest(guestService);
        if (guest == null){
            return;
        }

        Host host = view.selectHost(hostService);
        if (host == null) {
            return;
        }

        List<Reservation> reservations = reservationService.findByHost(host);
        Reservation toEdit = view.chooseReservation(guest, host, reservations);
        if (toEdit == null) {
            return;
        }

        Reservation updated = view.editReservation(toEdit, reservationService);
        if (updated == null) {
            return;
        }

        // Display reservation summary before update
        view.displayReservationSummary(updated);

        if (view.readConfirm("Is this okay? [y/n]: ")) {
            Result<Reservation> result = reservationService.updateReservation(updated);
            view.displayResult(result, "updated");
        } else {
            view.displayMessage("Reservation update canceled.");
        }
    }


    private void cancelReservation() throws DataException {
        Guest guest = view.selectGuest(guestService);
        if (guest == null) {
            return;
        }

        Host host = view.selectHost(hostService);
        if (host == null) {
            return;
        }

        List<Reservation> reservations = reservationService.findByHost(host);
        Reservation toCancel = view.chooseReservation(guest, host, reservations);
        if (toCancel == null) {
            return;
        }

        if (toCancel.getStartDate().isBefore(LocalDate.now())) {
            view.displayMessage("You cannot cancel a reservation that is in the past.");
            return;
        }

        if (view.readConfirmCancel()) {
            Result<Void> result = reservationService.cancelReservation(toCancel);
            if (result.isSuccess()) {
                // Create a result with the Reservation payload just for display
                Result<Reservation> displayResult = new Result<>();
                displayResult.setPayload(toCancel);
                view.displayResult(displayResult, "cancelled");
            } else {
                view.displayResult(result, "cancelled");
            }
        }
    }

}
