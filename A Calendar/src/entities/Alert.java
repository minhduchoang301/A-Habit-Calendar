package entities;
import java.time.LocalDateTime;

public class  Alert implements Comparable<Alert>{
    private String Message;
    /* message of the alert */
    private LocalDateTime time;
    /* time for the alerts to be activated */
    private String ID;
    /*ID to find alert later on */
    private static int counter = 0;

    public Alert(LocalDateTime Time, String message) {
        this.Message = message;
        this.time = Time;
        this.ID = Integer.toString(counter++);
    }
    public Alert(String id, LocalDateTime Time, String message) {
        this.Message = message;
        this.time = Time;
        this.ID = id;
    }

    public String getMessage() {
        return Message;
    }

    public String getID() {
        return this.ID;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String toString(){
        return Message + "<br> At: " + this.time;
    }

    public String viewToString(){
        return Message + " At: " + this.time + "<br>";
    }

    public boolean checkUpComing(LocalDateTime t){
        return this.time.isBefore(t);
    }

    public boolean checkIsBetween(LocalDateTime startTime, LocalDateTime endTime){
        return (! this.time.isBefore(startTime)) && (! this.time.isAfter(endTime));
    }
    public void editMessage(String message){this.Message = message;}

    public void editTime(LocalDateTime Time) {this.time = Time;}
    @Override
    public int compareTo(Alert o) {
        return this.time.compareTo(o.time);
    }
}
