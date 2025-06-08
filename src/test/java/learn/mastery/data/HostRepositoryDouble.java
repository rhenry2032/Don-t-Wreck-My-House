package learn.mastery.data;

import learn.mastery.models.Host;

import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    private final List<Host> hosts;

    public HostRepositoryDouble(List<Host> hosts) {
        this.hosts = hosts;
    }

    @Override
    public List<Host> findAll() {
        return hosts;
    }

    @Override
    public Host findByEmail(String email) {
        return hosts.stream()
                .filter(h -> h.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Host findById(String id) {
        return hosts.stream()
                .filter(h -> h.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
