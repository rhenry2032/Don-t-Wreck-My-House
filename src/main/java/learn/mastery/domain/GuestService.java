package learn.mastery.domain;

import learn.mastery.data.DataException;
import learn.mastery.data.GuestRepository;
import learn.mastery.models.Guest;

import java.util.List;
import java.util.stream.Collectors;

public class GuestService {

    private final GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    public Guest findByEmail(String email) throws DataException {
        return repository.findAll().stream()
                .filter(g -> g.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public List<Guest> findByLastName(String lastName) throws DataException {
        return repository.findAll().stream()
                .filter(g -> g.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    public List<Guest> findAll() throws DataException {
        return repository.findAll();
    }
}
