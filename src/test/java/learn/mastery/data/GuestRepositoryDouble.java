package learn.mastery.data;

import learn.mastery.models.Guest;

import java.util.List;

public class GuestRepositoryDouble implements GuestRepository{

    private final List<Guest> guests;

    public GuestRepositoryDouble(List<Guest> guests) {
        this.guests = guests;
    }

    @Override
    public List<Guest> findAll() {
        return guests;
    }

    @Override
    public Guest findById(int id) {
        return guests.stream()
                .filter(g -> g.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest findByEmail(String email) {
        return guests.stream()
                .filter(g -> g.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
}
