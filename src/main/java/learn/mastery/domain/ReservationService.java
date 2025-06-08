package learn.mastery.domain;

import learn.mastery.data.DataException;
import learn.mastery.data.ReservationRepository;
import learn.mastery.models.Reservation;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // Add reservation
    public Result<Reservation> addReservation(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        // Check for date overlap
        List<Reservation> existing = reservationRepository.findByHost(reservation.getHost());
        for (Reservation r : existing) {
            if (datesOverlap(r, reservation)) {
                result.addErrorMessage("Reservation dates overlap with an existing reservation.");
                return result;
            }
        }

        // Calculate total
        BigDecimal total = calculateTotal(reservation);
        reservation.setTotal(total);

        Reservation added = reservationRepository.add(reservation);
        result.setPayload(added);

        return result;
    }

    // Update reservations
    public Result<Reservation> updateReservation(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        List<Reservation> existing = reservationRepository.findByHost(reservation.getHost());
        // Reservation must exist
        boolean exists = existing.stream()
                .anyMatch(r -> r.getId() == reservation.getId());

        if (!exists) {
            result.addErrorMessage("Reservation does not exist.");
            return result;
        }

        // No overlap with other reservations
        for (Reservation r : existing) {
            if (r.getId() != reservation.getId() && datesOverlap(r, reservation)) {
                result.addErrorMessage("Reservation dates overlap with an existing reservation.");
                return result;
            }
        }

        // Recalculate total
        BigDecimal total = calculateTotal(reservation);
        reservation.setTotal(total);

        boolean success = reservationRepository.update(reservation);
        if (!success) {
            result.addErrorMessage("Could not update reservation.");
        } else {
            result.setPayload(reservation);
        }

        return result;
    }

    // Cancel reservation
    public Result<Void> cancelReservation(Reservation reservation) throws DataException {
        Result<Void> result = new Result<>();

        if (reservation == null ||
            reservation.getHost() == null ||
            reservation.getHost().getId() == null ||
            reservation.getHost().getId().isBlank()) {
            result.addErrorMessage("Host is required.");
            return result;
        }

        if (reservation.getStartDate() == null || !reservation.getStartDate().isAfter(LocalDate.now())) {
            result.addErrorMessage("Only future reservations can be canceled.");
            return result;
        }

        List<Reservation> existing = reservationRepository.findByHost(reservation.getHost());

        boolean exists = existing.stream()
                .anyMatch(r -> r.getId() == reservation.getId());

        if (!exists) {
            result.addErrorMessage("Reservation does not exist.");
            return result;
        }

        boolean success = reservationRepository.cancel(reservation);
        if (!success) {
            result.addErrorMessage("Could not cancel reservation.");
        }

        return result;
    }



    // Helper methods
    private Result<Reservation> validate(Reservation r) {
        Result<Reservation> result = new Result<>();

        if (r == null) {
            result.addErrorMessage("Reservation is required.");
            return result;
        }

        if (r.getGuest() == null || r.getGuest().getId() <= 0) {
            result.addErrorMessage("Guest is required.");
        }

        if (r.getHost() == null || r.getHost().getId() == null || r.getHost().getId().isBlank()) {
            result.addErrorMessage("Host is required.");
        }

        if (r.getStartDate() == null || r.getEndDate() == null) {
            result.addErrorMessage("Start and end dates are required.");
        } else {
            if (!r.getStartDate().isAfter(LocalDate.now())) {
                result.addErrorMessage("Start date must be in the future.");
            }
            if (!r.getEndDate().isAfter(r.getStartDate())) {
                result.addErrorMessage("End date must be after start date.");
            }
        }

        return result;
    }

    private BigDecimal calculateTotal(Reservation r) {
        BigDecimal total = BigDecimal.ZERO;
        LocalDate date = r.getStartDate();

        while (!date.isAfter(r.getEndDate().minusDays(1))) {
            boolean weekend = date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                                date.getDayOfWeek() == DayOfWeek.SUNDAY;

            BigDecimal rate = weekend ? r.getHost().getWeekendRate() : r.getHost().getStandardRate();
            total = total.add(rate);
            date = date.plusDays(1);
        }

        return total;
    }

    private boolean datesOverlap(Reservation existing, Reservation future) {
        return !(future.getEndDate().isBefore((existing.getStartDate())) ||
                future.getStartDate().isAfter(existing.getEndDate()));
    }
}
