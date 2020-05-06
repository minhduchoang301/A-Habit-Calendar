package useCases;

import entities.Alert;
import entities.Habit;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HabitManager {
    private ArrayList<Habit> allHabits;
    protected HabitManager() {
        allHabits = new ArrayList<>();
    }

    public Habit createHabit(String name, String description) {

        Habit newHabit = new Habit(name, description);
        addHabit(newHabit);
        return newHabit;
    }

    public void addHabit(Habit habit) {
        allHabits.add(habit);
    }

    public ArrayList<Habit> getAllHabits() {
        return new ArrayList<>(allHabits);
    }

    public ArrayList<Habit> searchByStartTime(LocalDateTime date) {

        ArrayList<Habit> correspHabits = new ArrayList<>();
        for (Habit habit : allHabits) {
            if (habit.getStartdate() == date) {
                correspHabits.add(habit);
            }
        }
        return correspHabits;
    }

    public ArrayList<Habit> searchByName(String name){

        ArrayList<Habit> correspHabits = new ArrayList<>();
        for (Habit habit: allHabits){
            if(habit.getName() == name) {
                correspHabits.add(habit);
            }
        }
        return correspHabits;
    }

    public ArrayList<Habit> searchByDescription(String descr){
        ArrayList<Habit> correspHabits = new ArrayList<>();
        for (Habit habit: allHabits){
            if (habit.getDescription().contains(descr)) {
                correspHabits.add(habit);
            }
        }
        return correspHabits;
    }

    public void changeHabitPoints(int inc_dec, int points, Habit habit){
        if(inc_dec == 0){
            habit.changePoints(-1 * points);
        } else if (inc_dec == 1){
            habit.changePoints(points);
        }
    }
}

