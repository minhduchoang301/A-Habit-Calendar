package controllers;

import com.sun.xml.internal.bind.v2.model.core.ID;
import useCases.CalendarManager;
import entities.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class CalendarUI {

    /**
     * @param calendarManager to access the useCase
     * @param userName to access the specific user when the user log in
     */
    private CalendarIO calendarIO;
    private CalendarManager calendarManager;
    private String userName;
    private String calendarName;

    /**
     * Initialize the Calendar Text I/O and useCase
     */
    public CalendarUI(String calendarName, String userName) throws IOException {
        calendarIO = new CalendarIO();
        calendarManager = new CalendarManager();
        this.userName = userName;
        this.calendarName = calendarName;
        calendarIO.recreateCalendar(calendarManager, calendarName, userName);
    }


    /**
     * @param username check the user's Event list, etc
     * @return return some string to recall this method again to implement the Goback step.
     */
    public String start(String username) {
        LocalDateTime lastLogin = calendarManager.getLastLoginTime();
        if(!calendarManager.getEventManager().alertInTimeInterval(lastLogin).isEmpty()){
            System.out.println("******************************");
            System.out.println("There are some alert for you!!!!!");
            for(Alert c: calendarManager.getEventManager().alertInTimeInterval(lastLogin)){
                System.out.println(c);
            }
            System.out.println("******************************");
        }


        /**
         * The first menu met when the user log in and choose. There are three parts.
         * 1.View all the events, memos and alerts.
         * 2.Search the event by date, name, tag, and series. And also user can select the event from the search result.
         * 3.Create the event and setup them for actually using.
         * 4.Delete or edit an existing event
         * 5.Exit the program.
         */



        Scanner scanner = new Scanner(System.in);
        int input_option;


        do {
            try {
                System.out.println("===============================");
                System.out.println("What do you want to do?");
                System.out.println("Option 1: View.");
                System.out.println("Option 2: Search and Select.");
                System.out.println("Option 3: Create and Setup event.");
                System.out.println("Option 4: Delete or edit an existing event");
                System.out.println("Option 5: Exit the program.");
                System.out.println("===============================");
                System.out.print("Enter your option number: ");
                input_option = scanner.nextInt();

                switch (input_option) {
                    case 1:
                        return this.view();

                    case 2:
                        return this.search();

                    case 3:
                        createEvent(scanner, username);
                    case 4:
                        DeleteAnItem();
                    case 5:
                        exitUI();

                    // An invalid integer was inputted, ask for a correct input again.
                    default:
                        System.out.println("Error, please enter a valid option number.");
                        return start(this.userName);
                }

            } catch (Exception InputMismatchException) {
                System.out.println("Error, please enter a valid option number.");
                return start(this.userName);
            }
        } while (true);

    }
    private String createEvent (Scanner scanner, String username) throws IOException{
        String input_Name;
        String input_StartTime;
        String input_EndTime;
        int input_option;
        LocalDateTime[] duration = new LocalDateTime[2];

        System.out.println("Please enter the Event name");
        input_Name = scanner.next();
        System.out.println("Please enter the startTime and day,format\"Year-Month-Day-hour-minute\"." +
                "For Example, \"2001-01-01-20-15\"");
        input_StartTime = scanner.next();
        duration[0] = parse_date_time(input_StartTime);
        System.out.println("Please enter the endTime and day,format\"Year-Month-Day-hour-minute\"." +
                "For Example, \"2001-01-01-20-15\"");
        input_EndTime = scanner.next();
        duration[1] = parse_date_time(input_EndTime);
        Event newEvent = calendarManager.getEventManager().createEvent(duration,input_Name);

        System.out.println("Do you want to setup the event? Enter 1 for Yes, 2 for No");
        input_option = scanner.nextInt();
        switch (input_option) {
            case 1:
                modify(newEvent);
            case 2:
                return start(this.userName);
            default:
                System.out.println("Error, please enter a valid option number.");
        }

        return "";
    }

    private LocalDateTime parse_date_time(String input_time){
        Integer year = Integer.parseInt(input_time.substring(0, 4));
        Integer month = Integer.parseInt(input_time.substring(5, 7));
        Integer day = Integer.parseInt(input_time.substring(8,10));
        Integer hour = Integer.parseInt(input_time.substring(11, 13));
        Integer last = Integer.parseInt(input_time.substring(14));

        return LocalDateTime.of(year, month, day, hour, last);
    }
    /**
     * View all events, alerts, memos from the user's file by choosing one option.
     * @return some String to implement Goback function.
     */
    private String view(){
        System.out.println("===============================");
        System.out.println("What do you want to do?");
        System.out.println("Option 1: View events.");
        System.out.println("Option 2: View alerts.");
        System.out.println("Option 3: View memos.");
        System.out.println("Option 4: Go back.");
        System.out.println("Option 5: Exit the program.");
        System.out.println("===============================");

        Scanner scanner = new Scanner(System.in);
        int input_option;

        do {
            try {
                System.out.print("Enter your option number: ");
                input_option = scanner.nextInt();

                switch (input_option) {
                    case 1:
                        return this.viewEvents();

                    // Call private method for logging in.
                    case 2:
                        if (calendarManager.getEventManager().getAlerts().isEmpty()) System.out.println("No alerts exist.");
                        for(Alert c: calendarManager.getEventManager().getAlerts()){
                            System.out.println(c);
                        }
                        return this.view();

                    case 3:
                        int i = 0;
                        if (calendarManager.getMemoManager().getAllMemos().isEmpty()) System.out.println("No memos exist.");
                        for(Memo c: calendarManager.getMemoManager().getAllMemos()){
                            System.out.println(i+1 + ". " + c);
                            i++;
                        }
                        System.out.println("===============================");
                        System.out.println("What do you want to do?");
                        System.out.println("Option 1: Select a memo and see all events with that memo.");
                        System.out.println("Option 2: Select a memo and associate it with another memo.");
                        System.out.println("Option 3: Go back.");
                        System.out.println("Option 4: Exit the program.");
                        System.out.println("===============================");
                        System.out.print("Enter your option number: ");
                        input_option = scanner.nextInt();
                        switch (input_option){
                            case 1:
                                System.out.println("Please enter the memo number.");
                                input_option = scanner.nextInt();
                                for(Event c: calendarManager.getMemoManager().getAllMemos().get(input_option-1).getEvents()){
                                    System.out.println(c);
                                }
                                return this.view();
                            case 2:
                                System.out.println("Please enter the number of the first memo.");
                                input_option = scanner.nextInt();
                                Memo memo1 = calendarManager.getMemoManager().getAllMemos().get(input_option-1);
                                System.out.println("Please enter the number of the second memo.");
                                input_option = scanner.nextInt();
                                Memo memo2 = calendarManager.getMemoManager().getAllMemos().get(input_option-1);
                                calendarManager.getMemoManager().associateMemo(memo1, memo2);
                                System.out.println("Memos associated Successfully!");
                                return this.view();
                            case 3:
                                return this.view();
                            case 4:
                                exitUI();
                        }

                    case 4:
                        return this.start(this.userName);

                    case 5:
                        exitUI();

                    // An invalid integer was inputted, ask for a correct input again.
                    default:
                        System.out.println("Error, please enter a valid option number.");
                        return view();
                }

            } catch (Exception InputMismatchException) {
                System.out.println("Error, please enter a valid option number.");
                return view();
            }
        } while (true);
    }

    /**
     * How people will view events
     * Option 1: View past events.
     * Option 2: View on-going events.
     * Option 3: View future events.
     * @return the string to recall this method for go back.
     */
    private String viewEvents() {
        System.out.println("===============================");
        System.out.println("What do you want to do?");
        System.out.println("Option 1: View past events.");
        System.out.println("Option 2: View on-going events.");
        System.out.println("Option 3: View future events.");
        System.out.println("Option 4: Go back.");
        System.out.println("Option 5: Exit the program.");
        System.out.println("===============================");

        Scanner scanner = new Scanner(System.in);
        int input_option;

        do {
            try {
                System.out.print("Enter your option number: ");
                input_option = scanner.nextInt();

                switch (input_option) {
                    case 1:
                        ArrayList<Event> pastEvents = calendarManager.getEventManager().getPastEvents();
                        if (pastEvents.isEmpty()) System.out.println("No past events exist.");
                        for (Event c : pastEvents) {
                            System.out.println(c);
                        }
                        return this.viewEvents();

                    case 2:
                        ArrayList<Event> curEvents = calendarManager.getEventManager().getCurrentEvents();
                        if (curEvents.isEmpty()) System.out.println("No current events exist.");
                        for (Event c : curEvents) {
                            System.out.println(c);
                        }
                        return this.viewEvents();

                    case 3:
                        ArrayList<Event> futEvents = calendarManager.getEventManager().getFutureEvents();
                        if (futEvents.isEmpty()) System.out.println("No future events exist.");
                        for (Event c : futEvents) {
                            System.out.println(c);
                        }
                        return this.viewEvents();

                    case 4:
                        return this.view();

                    case 5:
                        exitUI();

                    default:
                        System.out.println("Error, please enter a valid option number.");
                        return viewEvents();
                }

            } catch (Exception InputMismatchException) {
                System.out.println("Error, please enter a valid option number.");
                scanner.next();
            }
        } while (true);
    }

    private void exitUI() throws IOException{
        System.out.println("Goodbye!");
        calendarIO.saveCalendarToFile(calendarManager, calendarName, userName);
        System.exit(0);
    }

    /**
     * How people search an event
     * Option 1: By date.
     * Option 2: By Event Name.
     * Option 3: By Series Name.
     * Option 4: By Tag.
     * @return the string to recall this method for go back.
     */
    private String search(){
        System.out.println("===============================");
        System.out.println("What do you want to do?");
        System.out.println("Option 1: Search by Date");
        System.out.println("Option 2: Search by Event Name.");
        System.out.println("Option 3: Search by Series Name.");
        System.out.println("Option 4: Search by Tag.");
        System.out.println("Option 5: Go back.");
        System.out.println("Option 6: Exit the program.");
        System.out.println("===============================");

        Scanner scanner = new Scanner(System.in);
        int input_option;
        String input_Name;
        LocalDateTime search_Date;

        do {
            try {
                System.out.print("Enter your option number: ");
                input_option = scanner.nextInt();
                int i;

                switch (input_option) {
                    case 1:
                        i = 1;
                        System.out.println("Enter the date for search: For example, \"2001-01-01-20-15\": ");
                        input_Name = scanner.next();
                        search_Date = parse_date_time(input_Name);
                        if (!calendarManager.getEventManager().SearchByDate(search_Date).isEmpty()) {
                            for (Event c : calendarManager.getEventManager().SearchByDate(search_Date)) {
                                System.out.println(i + ". "+ c);
                                i++;
                            }
                        }
                        if (!calendarManager.getEventManager().SearchByDate(search_Date).isEmpty()) {
                            select(calendarManager.getEventManager().SearchByDate(search_Date));
                        } else
                            System.out.println("No results!");
                        return this.search();

                    case 2:
                        i = 1;
                        System.out.print("Enter the Event name for search: ");
                        input_Name = scanner.next();
                        if (!calendarManager.getEventManager().SearchByName(input_Name).isEmpty()) {
                            for (Event c : calendarManager.getEventManager().SearchByName(input_Name)) {
                                System.out.println(i + ". "+ c);
                                i++;
                            }
                        }
                        if (!calendarManager.getEventManager().SearchByName(input_Name).isEmpty()) {
                            select(calendarManager.getEventManager().SearchByName(input_Name));
                        } else
                            System.out.println("No results!");
                        return this.search();

                    case 3:
                        i = 1;
                        System.out.print("Enter the Series name for search: ");
                        input_Name = scanner.next();
                        if (!calendarManager.getEventManager().SearchBySeries(input_Name).isEmpty()) {
                            for (Event c : calendarManager.getEventManager().SearchBySeries(input_Name)) {
                                System.out.println(i + ". "+ c);
                                i++;
                            }
                        }
                        if (!calendarManager.getEventManager().SearchBySeries(input_Name).isEmpty()) {
                            select(calendarManager.getEventManager().SearchBySeries(input_Name));
                        } else
                            System.out.println("No results!");
                        return this.search();

                    case 4:
                        i = 1;
                        System.out.print("Enter the Tag name for search: ");
                        input_Name = scanner.next();
                        if (!calendarManager.getEventManager().SearchByTag(input_Name).isEmpty()) {
                            for (Event c : calendarManager.getEventManager().SearchByTag(input_Name)) {
                                System.out.println(i + ". "+ c);
                                i++;
                            }
                        }
                        if (!calendarManager.getEventManager().SearchByTag(input_Name).isEmpty())
                            select(calendarManager.getEventManager().SearchByTag(input_Name));
                        else
                            System.out.println("No results!");
                        return this.search();

                    case 5:
                        return this.start(this.userName);

                    case 6:
                        exitUI();

                        // An invalid integer was inputted, ask for a correct input again.
                    default:
                        System.out.println("Error, please enter a valid option number.");
                        return search();
                }

            } catch (Exception InputMismatchException) {
                System.out.println("Error, please enter a valid option number.");
                return search();
            }
        } while (true);
    }

    /**
     * Do people want to select and modify an event.
     * Option : Select an event to view OR Go back OR Exit.
     * @param events from the events given by search result
     * @return
     */
    private String select(ArrayList<Event> events) {
        System.out.println("===============================");
        System.out.println("What do you want to do?");
        System.out.println("Option 1: Select to view or modify");
        System.out.println("Option 2: Go back.");
        System.out.println("Option 3: Exit the program.");
        System.out.println("===============================");

        Scanner scanner = new Scanner(System.in);
        int input_option;
        int input_selection;

        do {
            try {
                System.out.print("Enter your option number: ");
                input_option = scanner.nextInt();

                switch (input_option) {
                    case 1:
                        System.out.println("Enter the Event number to check or modify");
                        input_selection = scanner.nextInt();
                        return this.checkEvent(events.get(input_selection-1));

                    case 2:
                        return this.search();

                    case 3:
                        exitUI();
                    // An invalid integer was inputted, ask for a correct input again.
                    default:
                        System.out.println("Error, please enter a valid option number.");
                        return select(events);
                }

            } catch (Exception InputMismatchException) {
                System.out.println("Error, please enter a valid option number.");
                return select(events);
            }
        } while (true);
    }

    /**
     * Do people want to check the event or actually modify the event
     * @param event select event from searched list
     * @return some string to go back
     */
    private String checkEvent(Event event) {
        System.out.println("===============================");
        System.out.println("What do you want to do?");
        System.out.println("Option 1: View this event");
        System.out.println("Option 2: Modify this event");
        System.out.println("Option 3: Go back.");
        System.out.println("Option 4: Exit the program.");
        System.out.println("===============================");

        Scanner scanner = new Scanner(System.in);
        int input_option;

        do {
            try {
                System.out.print("Enter your option number: ");
                input_option = scanner.nextInt();

                switch (input_option) {
                    case 1:
                        this.event_info_check(event);
                    case 2:
                        return this.modify(event);

                    case 3:
                        return search();

                    case 4:
                        exitUI();

                    // An invalid integer was inputted, ask for a correct input again.
                    default:
                        System.out.println("Error, please enter a valid option number.");
                        return checkEvent(event);
                }

            } catch (Exception InputMismatchException) {
                System.out.println("Error, please enter a valid option number.");
                return checkEvent(event);
            }
        } while (true);
    }


    /**
     * How people modify the event
     * Option 1: Line this event to series.
     * Option 2: Associate this event with tag.
     * Option 3: Associate this event with memo.
     * Option 4: Set the Alert.
     * @param event
     * @return
     */
    private String event_info_check(Event event){
        System.out.println("===============================");
        System.out.println("Event's Name: " + event.getName());
        System.out.println("Event's memo: " + event.getMemo());
        System.out.println("Event's series: ");
        for(String c: event.getSeriesNames()){
            System.out.println(c);
        }
        System.out.println("Event's Alert:");
        for(Alert c: event.getAlerts()){
            System.out.println(c);
        }
        System.out.println("Event's Tags: ");
        for(String c: event.getTags()){
            System.out.println(c);
        }
        System.out.println("===============================");
        return this.checkEvent(event);
    }

    private String modify(Event event) {
        System.out.println("===============================");
        System.out.println("What do you want to do?");
        System.out.println("Option 1: Link this event to series");
        System.out.println("Option 2: Associate this event with tag");
        System.out.println("Option 3: Associate this event with memo");
        System.out.println("Option 4: Set the Alert.");
        System.out.println("Option 5: Go back.");
        System.out.println("Option 6: Exit the program.");
        System.out.println("===============================");

        Scanner scanner = new Scanner(System.in);
        int input_option;
        String input_SomeString;
        String input_Content;
        String input_StartTime;
        LocalDateTime duration;

        do {
            try {
                System.out.print("Enter your option number: ");
                input_option = Integer.parseInt(scanner.nextLine());

                switch (input_option) {
                    case 1:
                        System.out.println("Please enter the series name: ");
                        input_SomeString = scanner.nextLine();
                        event.addSeriesNames(input_SomeString);
                        System.out.println("Series Added Successfully!");
                        System.out.println("Do you want to set up the frequency of the series? 1 for Yes, 2 for No.");
                        input_option = scanner.nextInt();
                        switch (input_option){
                            case 1:LocalDateTime[] durations = new LocalDateTime[2];
                                String input_EndTime;

                                System.out.println("Please enter the startTime and day,format\"Year-Month-Day-hour-minute\"." +
                                        "For Example, \"2001-01-01-20-15\"");
                                input_StartTime = scanner.next();
                                durations[0] = parse_date_time(input_StartTime);
                                System.out.println("Please enter the endTime and day,format\"Year-Month-Day-hour-minute\"." +
                                        "For Example, \"2001-01-01-20-15\"");
                                input_EndTime = scanner.next();
                                durations[1] = parse_date_time(input_EndTime);
                                System.out.println("Please enter the frequency of the series. For example, 1 represent per day.");
                                int input_frequency = scanner.nextInt();
                                System.out.println("Please enter the number of events in the series.");
                                int input_numOfEvents = scanner.nextInt();
                                calendarManager.getEventManager().createFreqEvent(durations, input_frequency, input_numOfEvents, input_SomeString);
                            case 2:
                                return modify(event);
                        }

                    case 2:
                        System.out.println("Please enter the tag name: ");
                        input_SomeString = scanner.nextLine();
                        event.addTag(input_SomeString);
                        System.out.println("Tag Added Successfully!");
                        return modify(event);

                    case 3:
                        System.out.println("Please enter the memo: ");
                        input_SomeString = scanner.nextLine();
                        Memo newMemo = calendarManager.getMemoManager().createMemo(input_SomeString);
                        event.setMemo(newMemo);
                        newMemo.addEvent(event);
                        System.out.println("Memo Added Successfully!");
                        return modify(event);

                    case 4:
                        System.out.println("Which type of alert do you want to set? Enter 1 for individual alert, 2 for frequent alerts.");
                        input_option = scanner.nextInt();
                        switch (input_option){
                            case 1:
                                System.out.println("Please enter the Alert message: ");
                                input_Content = scanner.next();
                                System.out.println("Please enter the time and day for alert,format\"Year-Month-Day-hour-minute\"." +
                                        "For Example, \"2001-01-01-20-15\"");
                                input_StartTime = scanner.next();
                                duration = parse_date_time(input_StartTime);
                                calendarManager.createAlert(event,duration,input_Content);
                                return modify(event);
                            case 2:
                                LocalDateTime alertTime;
                                System.out.println("Please enter the Alert message: ");
                                input_Content = scanner.next();
                                System.out.println("Please enter the time and day,format\"Year-Month-Day-hour-minute\"." +
                                        "For Example, \"2001-01-01-20-15\"");
                                input_StartTime = scanner.next();
                                alertTime = parse_date_time(input_StartTime);
                                System.out.println("Please enter the frequency of the Alert. For example, 1 means there will be an alert every 1 hour. 2 means there will be an alert every 2 hours.");
                                int input_frequency = scanner.nextInt();
                                calendarManager.createFreqAlert(event, alertTime, input_frequency, input_Content);
                                return modify(event);
                        }

                    case 5:
                        return checkEvent(event);

                    case 6:
                        exitUI();

                    // An invalid integer was inputted, ask for a correct input again.
                    default:
                        System.out.println("Error, please enter a valid option number.");
                        return modify(event);
                }

            } catch (Exception InputMismatchException) {
                System.out.println("Error, please enter a valid option number.");
                return modify(event);
            }
        } while (true);
    }
    public String DeleteAnItem(){
    Scanner scanner = new Scanner(System.in);
    int input_option;


        do {
        try {
            System.out.println("===============================");
            System.out.println("What do you want to do?");
            System.out.println("Option 1: Delete an event");
            System.out.println("Option 2: Delete an alert");
            System.out.println("Option 3: Delete a memo");
            System.out.println("Option 4: Edit an event");
            System.out.println("Option 5: Edit a memo");
            System.out.println("Option 6: Edit an alert");
            System.out.println("Option 7: Go back");
            System.out.println("Option 8: Exit the program.");
            System.out.println("===============================");
            System.out.print("Enter your option number: ");
            input_option = scanner.nextInt();

            switch (input_option) {
                case 1:
                    System.out.println("Please enter a valid integer ID");
                    String input_ID = scanner.next();
                    Event e = calendarManager.searchEvent(input_ID);
                    if (!e.equals(null)){
                        calendarManager.deleteEvent(e);
                        System.out.println("Event deleted");
                    }
                    DeleteAnItem();
                case 2:
                    System.out.println("Please enter a valid integer ID");
                    String input_ID_2 = scanner.next();
                    Alert a = calendarManager.searchAlert(input_ID_2);
                    if (!a.equals(null)) {
                        System.out.println("Alert deleted");
                    }
                    DeleteAnItem();
                case 3:
                    System.out.println("Please enter a valid integer ID");
                    String input_ID_3 = scanner.next();
                    Memo m = calendarManager.searchMemo(input_ID_3);
                    if (!m.equals(null)){
                        calendarManager.deleteMemo(m);
                        System.out.println("Memo deleted");
                    }
                    DeleteAnItem();
                case 4:
                    System.out.println("Please enter a valid ID");
                    String input_ID_4 = scanner.next();
                    EditEvent(input_ID_4);
                case 5:
                    System.out.println("Please enter a valid ID");
                    String input_ID_5 = scanner.next();
                    EditMemo(input_ID_5);
                case 6:
                    System.out.println("Please enter a valid ID");
                    String input_ID_6 = scanner.next();
                    EditAlert(input_ID_6);
                case 7:
                    return this.start(this.userName);
                case 8:
                    exitUI();
                    // An invalid integer was inputted, ask for a correct input again.
                default:
                    System.out.println("Error, please enter a valid option number.");
                    return start(this.userName);
            }

        } catch (Exception InputMismatchException) {
            System.out.println("Error, please enter a valid option number.");
            return start(this.userName);
        }
    } while (true);

}
    public String EditEvent(String ID){
        Scanner scanner = new Scanner(System.in);
        int input_option;
        do {
            try {
                System.out.println("===============================");
                System.out.println("What do you want to do?");
                System.out.println("Option 1: Edit name");
                System.out.println("Option 2: Edit start and end date");
                System.out.println("Option 3: Go back");
                System.out.println("Option 4: Exit the program.");
                System.out.println("===============================");
                System.out.print("Enter your option number: ");
                input_option = scanner.nextInt();

                switch (input_option) {
                    case 1:
                        System.out.println("Please enter a new name");
                        String input_name = scanner.next();
                        calendarManager.getEventManager().editName(ID, input_name);
                        return EditEvent(ID);
                    case 2:
                        System.out.println("Please enter a valid start date. For example, 2001-01-01-20-15: ");
                        String date_1 = scanner.next();
                        System.out.println("Please enter a valid end date. For example, 2001-01-01-20-15:");
                        String date_2 = scanner.next();
                        LocalDateTime sdate = parse_date_time(date_1);
                        LocalDateTime edate = parse_date_time(date_2);
                        calendarManager.getEventManager().editDate(ID, sdate, edate);
                        return EditEvent(ID);
                    case 3:
                        return DeleteAnItem();
                    case 4:
                        exitUI();

                        // An invalid integer was inputted, ask for a correct input again.
                    default:
                        System.out.println("Error, please enter a valid option number.");
                        return EditEvent(ID);
                }

            } catch (Exception InputMismatchException) {
                System.out.println("Error, please enter a valid option number.");
                return EditEvent(ID);
            }
        } while (true);
    }
    public String EditMemo(String ID){
        Scanner scanner = new Scanner(System.in);
        int input_option;
        do {
            try {
                System.out.println("===============================");
                System.out.println("What do you want to do?");
                System.out.println("Option 1: Edit note");
                System.out.println("Option 2: Go back");
                System.out.println("Option 3: Exit the program.");
                System.out.println("===============================");
                System.out.print("Enter your option number: ");
                input_option = scanner.nextInt();

                switch (input_option) {
                    case 1:
                        System.out.println("Please enter a new note");
                        String input_note = scanner.next();
                        calendarManager.getMemoManager().editNote(ID, input_note);
                        return EditMemo(ID);
                    case 2:
                        return DeleteAnItem();
                    case 3:
                        exitUI();

                        // An invalid integer was inputted, ask for a correct input again.
                    default:
                        System.out.println("Error, please enter a valid option number.");
                        return EditMemo(ID);
                }

            } catch (Exception InputMismatchException) {
                System.out.println("Error, please enter a valid option number.");
                return EditMemo(ID);
            }
        } while (true);
    }
    public String EditAlert(String ID){
        Scanner scanner = new Scanner(System.in);
        int input_option;
        do {
            try {
                System.out.println("===============================");
                System.out.println("What do you want to do?");
                System.out.println("Option 1: Edit message");
                System.out.println("Option 2: Edit time");
                System.out.println("Option 3: Go back");
                System.out.println("Option 4: Exit the program.");
                System.out.println("===============================");
                System.out.print("Enter your option number: ");
                input_option = scanner.nextInt();

                switch (input_option) {
                    case 1:
                        System.out.println("Please enter a new message");
                        String input_note = scanner.next();
                        calendarManager.getEventManager().editAlertMessage(ID, input_note);
                    case 2:
                        System.out.println("Please enter a valid date. For example, 2001-01-01-20-15:");
                        String date_2 = scanner.next();
                        LocalDateTime date = parse_date_time(date_2);
                        calendarManager.getEventManager().editTime(ID, date);
                    case 3:
                        return DeleteAnItem();
                    case 4:
                        exitUI();

                        // An invalid integer was inputted, ask for a correct input again.
                    default:
                        System.out.println("Error, please enter a valid option number.");
                        return EditAlert(ID);
                }

            } catch (Exception InputMismatchException) {
                System.out.println("Error, please enter a valid option number.");
                return EditAlert(ID);
            }
        } while (true);
    }


}
