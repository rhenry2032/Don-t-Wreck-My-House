package learn.mastery.ui;

import learn.mastery.data.DataException;
import learn.mastery.domain.GuestService;
import learn.mastery.domain.HostService;
import learn.mastery.domain.ReservationService;
import learn.mastery.domain.Result;
import learn.mastery.models.Guest;
import learn.mastery.models.Host;
import learn.mastery.models.Reservation;

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
                    viewReservations();
                    break;
                case MAKE_RESERVATION:
                    makeReservations();
                    break;
                case EDIT_RESERVATION:
                    editReservation();
                    break;
                case CANCEL_RESERVATION:
                    cancelReservation();
                    break;
            }
        } while (option != MainMenuOption.EXIT);
        view.displayMessage("Goodbye.");
    }

    private void viewReservations() throws DataException {
        Host host = view.selectHost(hostService);

        if (host == null) {
            view.displayMessage("Host not found.");
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
        Host host = view.selectHost(hostService);

        if (guest == null || host == null) {
            view.displayMessage("Guest or Host not found.");
            return;
        }

        List<Reservation> existing = reservationService.findByHost(host);
        Reservation reservation = view.makeReservation(guest, host, existing);

        if (reservation == null) {
            return;
        }

        Result<Reservation> result = reservationService.addReservation(reservation);
        view.displayResult(result);
    }

    private void editReservation() throws DataException {
        Guest guest = view.selectGuest(guestService);
        Host host = view.selectHost(hostService);
        if (guest == null || host == null) {
            view.displayMessage("Guest or Host not found.");
            return;
        }

        List<Reservation> reservations = reservationService.findByHost(host);
        Reservation toEdit = view.chooseReservation(guest, host, reservations);
        if (toEdit == null) return;

        Reservation updated = view.editReservation(toEdit);
        Result<Reservation> result = reservationService.updateReservation(updated);
        view.displayResult(result);
    }

    private void cancelReservation() throws DataException {
        Guest guest = view.selectGuest(guestService);
        Host host = view.selectHost(hostService);
        if (guest == null || host == null) {
            view.displayMessage("Guest or Host not found.");
            return;
        }

        List<Reservation> reservations = reservationService.findByHost(host);
        Reservation toCancel = view.chooseReservation(guest, host, reservations);
        if (toCancel == null) return;

        if (view.readConfirmCancel()) {
            Result<Void> result = reservationService.cancelReservation(toCancel);
            view.displayResult(result);
        }
    }
}
