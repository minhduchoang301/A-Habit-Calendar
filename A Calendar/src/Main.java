import controllers.CalendarGUI;
import controllers.IO;
import controllers.LoggingGUI;

import java.io.IOException;

public class Main extends IO {
    public static void main(String[] args) throws IOException {
        LoggingGUI loggingGUI = new LoggingGUI();
        String username = loggingGUI.start();

        if (username == null){
            return;
        }

        CalendarGUI calendarGUI = new CalendarGUI(username);
        calendarGUI.start(username);

    }
}
