package learn.mastery.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private int id;
    private Guest guest;
    private Host host;
    private LocalDate startDate, endDate;
    private BigDecimal total;

    public Reservation() {
    }

    public Reservation(int id, Guest guest, Host host, LocalDate startDate, LocalDate endDate, BigDecimal total) {
        this.id = id;
        this.guest = guest;
        this.host = host;
        this.startDate = startDate;
        this.endDate = endDate;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return id == that.id &&
                Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, host);
    }
}
