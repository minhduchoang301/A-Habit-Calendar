package entities;

import java.util.ArrayList;

public class Memo {
    private ArrayList<Event> events = new ArrayList<>();
    /* an ArrayList of associated events of the memo*/

    private ArrayList<Memo> memos = new ArrayList<>();
    /* an ArrayList of associated memos of the memo*/

    private String note;
    /* content of the memo*/
    private String ID;
    /*associated ID -unique- */
    private static int counter_memo = 0;

    public Memo(String note){
        this.ID =Integer.toString(counter_memo++);
        this.note = note;
    }

    public String getID(){
        return ID;
    }

    public ArrayList<Event> getEvents(){
        return new ArrayList<>(this.events);
    }

    public void addEvent(Event e) {
        if (!this.events.contains(e)) {
            this.events.add(e);
        }
    }

    public void addMemo(Memo m){
        if (!this.memos.contains(m)) {
            this.memos.add(m);
        }
    }

    public String getNote(){
        return this.note;
    }

/*
    public boolean isEqual(Memo memo2){
        if ( !(this.events.containsAll(memo2.getEvents()) && memo2.events.containsAll(this.events)) ){return false;}
        if ( !( this.memos.containsAll(memo2.memos) && memo2.memos.containsAll(this.memos)) ){return false;}
        return this.note.equals(memo2.note);
    }
*/
    /* Delete an event. Pre: There exists the event input*/
    public void removeEvent(Event event){
        int i = 0;
        while (i < events.size()){
            if (events.get(i).equals(event)){
                events.remove(i);
            }
            i = i + 1;
        }
    }
    /** change the memo's note **/
    public void editNote(String content){
        note = content;
    }



    @Override
    public String toString() {
        return "Memo{" +
                "note= " + note + "}<br>";
    }
}
