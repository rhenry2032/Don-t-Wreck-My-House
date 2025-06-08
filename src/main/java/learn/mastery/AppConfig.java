package learn.mastery;

import learn.mastery.data.*;
import learn.mastery.domain.*;
import learn.mastery.ui.ConsoleIO;
import learn.mastery.ui.Controller;
import learn.mastery.ui.View;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public GuestRepository guestRepository() {
        return new GuestFileRepository("./data/guests.csv");
    }

    @Bean
    public HostRepository hostRepository() {
        return new HostFileRepository("./data/hosts.csv");
    }

    @Bean
    public ReservationRepository reservationRepository(GuestRepository guestRepository) {
        return new ReservationFileRepository("./data/reservations", guestRepository);
    }

    @Bean
    public GuestService guestService(GuestRepository guestRepository) {
        return new GuestService(guestRepository);
    }

    @Bean
    public HostService hostService(HostRepository hostRepository) {
        return new HostService(hostRepository);
    }

    @Bean
    public ReservationService reservationService(ReservationRepository reservationRepository) {
        return new ReservationService(reservationRepository);
    }

    @Bean
    public ConsoleIO consoleIO() {
        return new ConsoleIO();
    }

    @Bean
    public View view(ConsoleIO io) {
        return new View(io);
    }

    @Bean
    public Controller controller(View view, GuestService guestService, HostService hostService, ReservationService reservationService) {
        return new Controller(view, guestService, hostService, reservationService);
    }
}
