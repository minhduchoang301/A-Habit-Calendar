package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Event implements Comparable<Event>{

    private String name;
    /* name of the event */
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    /* start and end date */
    private ArrayList<Alert> alerts = new ArrayList<>();
    /* an ArrayList of Alerts of the event*/
    private ArrayList<String> seriesNames = new ArrayList<>();
    /* an ArrayList of String represent the series of events that the Event is in */
    private ArrayList<String> tags = new ArrayList<>();
    /* an ArrayList of Tag string */
    private Memo memo;
    /*associated memo */
    private String ID;
    /*associated ID -unique- */
    private static int counter_event = 0;

    public Event(String name, LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.ID = Integer.toString(counter_event++);
    }

    public Event(String name, LocalDateTime startDate, LocalDateTime endDate, ArrayList<Alert> alerts,
                 ArrayList<String> seriesNames, ArrayList<String> tags, Memo memo) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.alerts = alerts;
        this.seriesNames = seriesNames;
        this.tags = tags;
        this.memo = memo;
        this.ID = Integer.toString(counter_event++);
    }

    public String getID() {
        return ID;
    }

    public Event(String name, LocalDateTime[] date) {
        this.startDate = date[0];
        this.endDate = date[1];
        this.name = name;
    }

    public void addAlert(Alert alert) {
        int i = 0;
        if(this.alerts.isEmpty()){this.alerts.add(alert);}
        else{for(Alert c: alerts){
            i++;
            if(alert.getTime().isAfter(c.getTime())){
                alerts.add(i,alert);
            }
        }}
    }

    public void addAlerts(ArrayList<Alert> alerts) {
        for (Alert alert : alerts) {
            this.addAlert(alert);
        }
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public void addSeriesNames(String group) {
        if (!this.seriesNames.contains(group)) {
            this.seriesNames.add(group);
        }
    }

    public void addSeriesNamesByArrayList(ArrayList<String> arrayList) {
        for (String str : arrayList) {
            this.addSeriesNames(str);
        }
    }

    public void addTags(ArrayList<String> tags) {
        for (String tag : tags) {
            this.addTag(tag);
        }
    }

    public void setMemo(Memo memo) {
        this.memo = memo;
    }

    public ArrayList<Alert> getAlerts() { return new ArrayList<>(this.alerts); }

    public ArrayList<String> getSeriesNames(){
        return seriesNames;
    }

    public Memo getMemo(){
        return this.memo;
    }

    public String getName() {
        return this.name;
    }

    public boolean checkName(String name){
        return this.name.equals(name);
    }

    public boolean checkTag(String tag){
        return tags.contains(tag);
    }

    public ArrayList<String> getTags(){
        return tags;
    }

    public boolean checkSeriesNames(String group){
        return seriesNames.contains(group);
    }

    public boolean checkStartAfter(LocalDateTime time){
        return this.startDate.isAfter(time);
    }

    public boolean checkEndBefore(LocalDateTime time) {
        return this.endDate.isBefore(time);
    }

    public boolean checkOnGoing(LocalDateTime time) {
        if (this.startDate == null || this.endDate == null) return false;
        if (this.startDate.isEqual(time) || this.endDate.isEqual(time)) {
            return true;
        }
        return this.startDate.isBefore(time) && this.endDate.isAfter(time);
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    public void postponeEvent() {
        this.startDate = null;
        this.endDate = null;
    }

    public void editEventTime(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /* Delete an Alert. Pre: There exists the event input*/
    public void removeAlert(Alert alert) {
        int i = 0;
        while (i < alerts.size()) {
            if (alerts.get(i).equals(alert)) {
                alerts.remove(i);
            }
            i = i + 1;
        }
    }

    /* Delete an Memo. Pre: There exists the event input*/
    public void removeMemo(){memo = null;}

    public void editName(String name){
        this.name = name;
    }

    public void editDate(LocalDateTime start, LocalDateTime end){
        this.startDate = start;
        this.endDate = end;
    }
    @Override
    public int compareTo(Event o) {
        return this.startDate.compareTo(o.startDate);
    }

    public void deleteSeries(String toDelete){
        for(String c: seriesNames){
            if(c.equals(toDelete)){
                seriesNames.remove(c);
            }
        }
    }

    public void deleteTag(String toDelete){
        for(String c: tags){
            if(c.equals(toDelete)){
                tags.remove(c);
            }
        }
    }

    public String toString() {
        return name + " From: " + this.startDate + " To: " + this.endDate;
    }
}
