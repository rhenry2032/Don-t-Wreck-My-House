package learn.mastery.data;

import learn.mastery.models.Host;
import learn.mastery.models.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationRepositoryDouble implements ReservationRepository{
    private final List<Reservation> reservations = new ArrayList<>();

    @Override
    public List<Reservation> findByHost(Host host) {
        return reservations.stream()
                .filter(r -> r.getHost().getId().equals(host.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Reservation add(Reservation reservation) {
        reservation.setId(reservations.size() + 1);
        reservations.add(reservation);
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) {
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getId() == reservation.getId() &&
                    reservations.get(i).getHost().getId().equals(reservation.getHost().getId())) {
                reservations.set(i, reservation);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean cancel(Reservation reservation) {
        return reservations.removeIf(r ->
                r.getId() == reservation.getId()
                        && r.getHost().getId().equals(reservation.getHost().getId()));
    }

    // For testing
    public void addExisting(Reservation r) {
        reservations.add(r);
    }
}
