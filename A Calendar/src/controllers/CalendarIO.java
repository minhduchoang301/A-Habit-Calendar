package controllers;

import entities.Alert;
import entities.Event;
import entities.Habit;
import entities.Memo;
import useCases.CalendarManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Set;

/**
 * Responsible for storing All data inside a CalendarManager object in a file when the program closes.
 * And retrieves the data back from the file and inserts it back into the CalendarManager argument.
 */
public class CalendarIO extends IO {

    private char TEXT_FILE_DELIMITER = '@';
    private char ALERTS_DATA_DELIMITER = '~';
    private char MEMOS_DATA_DELIMITER = ';';
    private char EVENT_DATA_DELIMITER = ';';
    private char HABIT_DATA_DELIMITER = ';';
    private char MULTIPLE_OBJECTS_DELIMITER = ',';
    private char login_Time_DELIMITER = '#';
    private String FILENAME;

    /**
     * Retrieves the data inside filename.txt, builds and returns a CalendarManager from it.
     *
     * @param calendar the CalendarManager object that'll get written into a file.
     * @param filename the filename where CalendarManager's data will get stored.
     * @throws IOException if an input/output exception occurred.
     */
    void recreateCalendar(CalendarManager calendar, String filename, String folderName) throws IOException {
        FILENAME = folderName + "/" + filename;

        BufferedReader reader;
        try {
            reader = readFile(FILENAME);
            // File not found.
        } catch (Exception FileNotFoundException) {
            // No calendar data.
            return;
        }

        ArrayList<Event> events = new ArrayList<>();
        ArrayList<Memo> memos = new ArrayList<>();

        String line = reader.readLine();
        if (line == null || line.equals(""))
            return;

        while (line.charAt(0) != TEXT_FILE_DELIMITER) {
            for (int i = 0; i < 6; i++) {
                line = line + EVENT_DATA_DELIMITER;
                line = line + reader.readLine();
            }
            if (line != null) {
                events.add(stringToEvent(line));
            }
            line = reader.readLine();
        }

        line = reader.readLine();
        if (line != null) {
            // With the assumption that line ending with delimiter won't get extra empty element when str.split.
            String[] strings = this.splitStringBy(line, Character.toString(MEMOS_DATA_DELIMITER));
            for (String string : strings) {
                memos.add(stringToMemo(string));
            }
        }

        calendar.getEventManager().addEvents(events);
        calendar.getMemoManager().addMemos(memos);

        line = reader.readLine();
        if (line != null) {
            while (line.charAt(0) != TEXT_FILE_DELIMITER) {
                for (int i = 0; i < 6; i++) {
                    line = line + EVENT_DATA_DELIMITER;
                    line = line + reader.readLine();
                }
                Event event = stringToEvent(line);

                line = reader.readLine();
                Memo memo = stringToMemo(this.splitStringBy(line, Character.toString(EVENT_DATA_DELIMITER))[0]);
                calendar.getMemoManager().associateMemo(event, memo);

                line = reader.readLine();
            }
        }
        line = reader.readLine();
        if (line != null) {
            while (line.charAt(0) != TEXT_FILE_DELIMITER) {
                String[] temp = this.splitStringBy(line, Character.toString(MEMOS_DATA_DELIMITER));
                Memo memo1 = stringToMemo(temp[0]);
                Memo memo2 = stringToMemo(temp[1]);
                calendar.getMemoManager().oneWayAssociateMemo(memo1, memo2);

                line = reader.readLine();
            }
        }

        line = reader.readLine();
        if (line != null) {
            while (line.charAt(0) != TEXT_FILE_DELIMITER) {
                String str = line;
                calendar.setLastLoginTime(this.stringToLoginTime(str));

                line = reader.readLine();
            }
        }

        line = reader.readLine();
        if (line != null) {
            while (line.charAt(0) != TEXT_FILE_DELIMITER) {
                Habit habit = stringToHabit(line);
                calendar.getHabitManager().addHabit(habit);
                line = reader.readLine();
            }
        }

        reader.close();
    }

    /**
     * Saves the data inside a CalendarManager object into a text file.
     *
     * @param calendar the CalendarManager object that'll get written into a file.
     * @param filename the filename where CalendarManager's data will get stored.
     * @throws IOException if an input/output exception occurred.
     */
    void saveCalendarToFile(CalendarManager calendar, String filename, String folderName) throws IOException {
        FILENAME = folderName + "/" + filename;
        BufferedWriter writer = overwriteToFile(FILENAME);

        ArrayList<Event> eventsList = calendar.getEventManager().getAllEvents();
        for (Event event : eventsList) {
            String str = eventToString(event);
            writer.write(str);
        }
        writer.write(TEXT_FILE_DELIMITER + "\n");
        ArrayList<Memo> memosList = calendar.getMemoManager().getAllMemos();
        for (Memo memo : memosList) {
            writer.write(memoToString(memo));
        }
        writer.write("\n");

        Hashtable<Event, Memo> eventToMemo = calendar.getMemoManager().getEventToMemo();
        Hashtable<Memo, Memo> memoToMemo = calendar.getMemoManager().getMemoToMemo();
        Set<Event> eventToMemoKeys = eventToMemo.keySet();
        Set<Memo> memoToMemoKeys = memoToMemo.keySet();

        for (Event key : eventToMemoKeys) {
            String event = this.eventToString(key);
            String memo = this.memoToString(eventToMemo.get(key));
            writer.write(event + memo + "\n");
        }
        writer.write(TEXT_FILE_DELIMITER + "\n");
        for (Memo key : memoToMemoKeys) {
            String memo1 = this.memoToString(key);
            String memo2 = this.memoToString(memoToMemo.get(key));
            writer.write(memo1 + memo2 + "\n");
        }
        writer.write(TEXT_FILE_DELIMITER + "\n");
        writer.write(loginTimeToString(calendar.getCurrentTime()) + "\n");
        writer.write(TEXT_FILE_DELIMITER + "\n");

        ArrayList<Habit> habits = calendar.getHabitManager().getAllHabits();
        if (!habits.isEmpty()) {
            for (Habit habit : habits) {
                String str = this.habitToString(habit);
                writer.write(str + "\n");
            }
        }
        writer.write(TEXT_FILE_DELIMITER + "\n");


        writer.close();
    }

    private Event stringToEvent(String input) {
        String[] arr = this.splitStringBy(input, Character.toString(EVENT_DATA_DELIMITER));
        String name = arr[0];
        LocalDateTime startDate = stringToLocalDateTime(arr[1]);
        LocalDateTime endDate = stringToLocalDateTime(arr[2]);
        Event event = new Event(name, startDate, endDate);

        for (int i = 3; i < 7; i++) {
            try {
                if (i == 3) {
                    String[] tempAlerts = this.splitStringBy(arr[i], Character.toString(MULTIPLE_OBJECTS_DELIMITER));
                    ArrayList<Alert> alerts = this.alertsStringToArrayList(tempAlerts);
                    event.addAlerts(alerts);
                } else if (i == 4) {
                    String[] tempSeries = this.splitStringBy(arr[i], Character.toString(MULTIPLE_OBJECTS_DELIMITER));
                    ArrayList<String> seriesNames = new ArrayList<>(Arrays.asList(tempSeries));
                    event.addSeriesNamesByArrayList(seriesNames);
                } else if (i == 5) {
                    String[] tempTags = this.splitStringBy(arr[i], Character.toString(MULTIPLE_OBJECTS_DELIMITER));
                    ArrayList<String> tags = new ArrayList<>(Arrays.asList(tempTags));
                    event.addTags(tags);
                } else if (i == 6) {
                    Memo memo = stringToMemo(arr[i]);
                    if (memo != null)
                        event.setMemo(memo);
                }
            } catch (Exception ignored) {
            }
        }

        return event;
    }

    private String eventToString(Event event) {
        String name = event.getName();
        String startDate = localDateTimeToString(event.getStartDate());
        String endDate = localDateTimeToString(event.getEndDate());

        String alerts = this.alertsArrayToString(event.getAlerts());
        String seriesName = this.insertSeparator(event.getSeriesNames(), Character.toString(MULTIPLE_OBJECTS_DELIMITER));
        String tags = this.insertSeparator(event.getTags(), Character.toString(MULTIPLE_OBJECTS_DELIMITER));
        String memo = this.memoToString(event.getMemo());

        String str = "";
        str = str + name + "\n";
        str = str + startDate + "\n";
        str = str + endDate + "\n";
        str = str + alerts + "\n";
        str = str + seriesName + "\n";
        str = str + tags + "\n";
        str = str + memo + "\n";

        return str;
    }

    private Memo stringToMemo(String input) {
        if (input.equals("")) {
            return null;
        }
        return new Memo(input);
    }

    private String memoToString(Memo memo) {
        if (memo == null)
            return "";
        return memo.getNote() + MEMOS_DATA_DELIMITER;
    }

    private String habitToString(Habit habit) {
        //
        String str = "";
        str += String.valueOf(habit.getId()) + HABIT_DATA_DELIMITER;
        str += habit.getName() + HABIT_DATA_DELIMITER;
        str += habit.getDescription() + HABIT_DATA_DELIMITER;
        str += String.valueOf(habit.getPoints()) + HABIT_DATA_DELIMITER;
        str += this.localDateTimeToString(habit.getStartdate()) + HABIT_DATA_DELIMITER;
        return str;
    }

    private Habit stringToHabit(String input) {
        String[] arr = this.splitStringBy(input, Character.toString(HABIT_DATA_DELIMITER));
        int id = Integer.parseInt(arr[0]);
        String name = arr[1];
        String description = arr[2];
        int points = Integer.parseInt(arr[3]);
        LocalDateTime date = this.stringToLocalDateTime(arr[4]);
        return new Habit(id, name, description, points, date);
    }

    private Alert stringToAlert(String input) {
        String[] arr = splitStringBy(input, Character.toString(ALERTS_DATA_DELIMITER));
        String message = arr[0];
        LocalDateTime date = stringToLocalDateTime(arr[1]);
        String id = arr[2];
        return new Alert(id, date, message);
    }

    private String alertToString(Alert alert) {
        ArrayList<String> arr = new ArrayList<>();
        arr.add(alert.getMessage());
        arr.add(this.localDateTimeToString(alert.getTime()));
        arr.add(alert.getID());
        return insertSeparator(arr, Character.toString(ALERTS_DATA_DELIMITER));
    }

    private String loginTimeToString(LocalDateTime time) {
        ArrayList<String> arr = new ArrayList<>();
        arr.add(this.localDateTimeToString(time));
        return insertSeparator(arr, Character.toString(login_Time_DELIMITER));
    }

    private LocalDateTime stringToLoginTime(String input) {
        String[] arr = splitStringBy(input, Character.toString(login_Time_DELIMITER));
        LocalDateTime date = stringToLocalDateTime(arr[0]);
        return date;
    }

    private String alertsArrayToString(ArrayList<Alert> alerts) {
        String str;
        ArrayList<String> arr = new ArrayList<>();
        for (Alert alert : alerts) {
            arr.add(alertToString(alert));
        }
        str = insertSeparator(arr, ",");
        return str;
    }

    private ArrayList<Alert> alertsStringToArrayList(String[] alerts) {
        ArrayList<Alert> arr = new ArrayList<>();
        for (String alert : alerts) {
            arr.add(stringToAlert(alert));
        }
        return arr;
    }

    private LocalDateTime stringToLocalDateTime(String input) {
        return LocalDateTime.of(Integer.parseInt(input.substring(0, 4)),
                Integer.parseInt(input.substring(4, 6)),
                Integer.parseInt(input.substring(6, 8)),
                Integer.parseInt(input.substring(8, 10)),
                Integer.parseInt(input.substring(10)));
    }

    private String localDateTimeToString(LocalDateTime date) {
        if (date == null) return "";

        String month = zeroLeadDate(date.getMonthValue());
        String day = zeroLeadDate(date.getDayOfMonth());
        String hour = zeroLeadDate(date.getHour());
        String min = zeroLeadDate(date.getMinute());

        return date.getYear() + month + day + hour + min;
    }

    private String[] splitStringBy(String str, String separator) {
        return str.split(separator);
    }

    private String insertSeparator(ArrayList<String> arr, String separator) {
        String str = "";
        for (String s : arr) {
            str = str + s + separator;
        }
        if (!str.isEmpty())
            str = str.substring(0, str.length() - separator.length());
        return str;
    }

    private String zeroLeadDate(int date) {
        String str = "0" + date;
        return str.substring(str.length() - 2);
    }

}
