package entities;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * The Habit class, keeps track of habits for a calendar.
 */
public class Habit {
    private int id;
    private String name;
    private String description;
    private int points;
    private LocalDateTime startdate;
    private ArrayList<Alert> reminders = new ArrayList<>();

    public Habit(String name, String description) {
        this.name = name;
        this.description = description;
        this.points = 0;
        this.startdate = LocalDateTime.now();
    }

    public Habit(int id, String name, String description, int points, LocalDateTime date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.points = points;
        this.startdate = date;
    }

    public void handleHabit(int response) {
        if (response == 1) {
            points--;
        } else {
            points++;
        }
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public int getPoints() {
        return this.points;
    }

    public LocalDateTime getStartdate(){
        return this.startdate;
    }

    public String getPrettyStartDate() {
        String month = this.startdate.getMonth().toString();
        int day = this.startdate.getDayOfMonth();
        int year = this.startdate.getYear();

        return month + " " + day + " " + year;
    }
    public void changePoints(int points){
        this.points += points;
    }

    public String toString(){
        return getName() + "points: " + getPoints();
    }
    public void addReminder(Alert newAlert) {
        int i = 0;
        if(this.reminders.isEmpty()){this.reminders.add(newAlert);}
        else{for(Alert c: reminders){
            i++;
            if(newAlert.getTime().isAfter(c.getTime())){
                reminders.add(i,newAlert);
            }
        }}
    }
}
