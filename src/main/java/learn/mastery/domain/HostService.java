package learn.mastery.domain;

import learn.mastery.data.DataException;
import learn.mastery.data.HostRepository;
import learn.mastery.models.Host;

import java.util.List;
import java.util.stream.Collectors;

public class HostService {
    private final HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    public Host findByEmail(String email) throws DataException {
        return repository.findAll().stream()
                .filter(h -> h.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public List<Host> findByLastName(String lastName) throws DataException {
        return repository.findAll().stream()
                .filter(h -> h.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    public List<Host> findAll() throws DataException {
        return repository.findAll();
    }
}
