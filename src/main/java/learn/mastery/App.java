package learn.mastery;

import learn.mastery.data.DataException;
import learn.mastery.ui.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) throws DataException {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Controller controller = context.getBean(Controller.class);
        controller.run();
    }
}
