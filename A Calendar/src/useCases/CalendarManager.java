package useCases;

import entities.Alert;
import entities.Event;
import entities.Memo;

import java.time.LocalDateTime;

/**
 * The CalendarManager is a facade class for all use cases.
 * It includes the eventManager and memoManager inner class, and some other short codes(mostly alert creation)
 */
public class CalendarManager {

	/**
	 * an EventManager handles all Event related methods.
	 * All the events are stored in this EventManager.
	 */
	private EventManager eventManager;

	/**
	 * a MemoManager handles all Memo related methods.
	 * All the events are stored in this MemoManager.
	 */
	private MemoManager memoManager;

	/**
	 * a HabitManager handles all Habit related methods.
	 * All habits are stored in this HabitManager.
	 */
	private HabitManager habitManager;

	/**
	 * This will store the lastloginTime
	 */     // This was a last minute addition, and too late to move. but it will
	private LocalDateTime lastLoginTime = null; // move to a more appropriate place(i.e. a User class) for phase 2.


	/**
	 * The constructor of CalendarManager
	 * It also create a new EventManager and MemoManager with no entities in them
	 */
	public CalendarManager() {
		eventManager = new EventManager();
		memoManager = new MemoManager();
		habitManager = new HabitManager();
	}

	/**
	 * Get the HabitManager of this Calendar.
	 *
	 * @return the HabitManager
	 */
	public HabitManager getHabitManager() {
		return this.habitManager;
	}

	/**
	 * Get the EventManager of this Calendar.
	 *
	 * @return the EventManager.
	 */
	public EventManager getEventManager() {
		return this.eventManager;
	}

	/**
	 * Get the MemoManager of this Calendar.
	 *
	 * @return the MemoManager.
	 */
	public MemoManager getMemoManager(){
		return this.memoManager;
	}

// Create alerts, and store them in the corresponding event
	/**
	 Create and return a single Alert with message
	 @param event The event this alert belong.
	 @param time The time of the alert.
	 @param message The message of the alert.
	 */
	public void createAlert(Event event, LocalDateTime time, String message){
		Alert newAlert = new Alert(time, message);
		event.addAlert(newAlert);
	}

	/**
	 * Create and return a set of alerts for a certain frequency without message
	 * @param event the event this set of alerts belong.
	 * @param alertTime the  time of the first alert.
	 * @param frequency the time between 2 alerts, in hours.
	 * @param message the message of this set of alert
	 */
	public void createFreqAlert(Event event, LocalDateTime alertTime, int frequency, String message) {
		//ArrayList<Alert> output = new ArrayList<>();
		if (event.getStartDate() == null) return;

		while (!event.getStartDate().isBefore(alertTime)) {
			this.createAlert(event, alertTime, message);
			alertTime = alertTime.plusHours(frequency);
		}
	}

	/**
	 * Set last login time for user.
	 * @param lastLoginTime is the last login time for user.
	 */
	public void setLastLoginTime(LocalDateTime lastLoginTime) { this.lastLoginTime = lastLoginTime;	}

	/**
	 * Get last login time for user.
	 * @return the last login time.
	 */
	public LocalDateTime getLastLoginTime(){ return this.lastLoginTime;}
	/**
	 * Get the current time when we create the calender for alert usage
	 * @return the current time.
	 */
	public LocalDateTime getCurrentTime() { return LocalDateTime.now();}

	/** Delete functionalities **/
	public Event searchEvent(String ID){
		return eventManager.getEventbyID(ID);
		/*return null if nothing is found */
	}
	public Memo searchMemo(String ID){
		return memoManager.getMemobyID(ID);
		/*return null if nothing is found */
	}
	public Alert searchAlert(String ID){
		return eventManager.getAlertbyID(ID);
		/*return null if nothing is found */
	}
	public void deleteEvent(Event event){
		eventManager.deleteEvent(event);
		memoManager.deleteEvent(event);
	}
	public void deleteMemo(Memo memo){
		memoManager.deleteMemo(memo);
	}

	/** Edit functionalities **/
//	public void editEventName(String ID, String Name){
//		eventManager.editName(ID, Name);
//	}

//	public void editEventDate(String ID, LocalDateTime StartDate, LocalDateTime EndDate){
//		eventManager.editDate(ID, StartDate, EndDate);
//	}

//	public void editAlertMessage(String ID, String Message){
//		eventManager.editAlertMessage(ID, Message);
//	}

//	public void editAlertTime(String ID, LocalDateTime time) {
//		eventManager.editTime(ID, time);
//	}


	// TODO: Add functionalities of these methods to GUI (With calls directly to EventManager)
//	public void recreateEvent(Event event1, LocalDateTime startDate, LocalDateTime endDate) {
//		eventManager.recreateEvent(event1, startDate, endDate);
//	}
//
//	public void postponeEvent(Event event) {
//		eventManager.postponeEvent(event);
//	}
//
//	public void editEventTime(Event event, LocalDateTime startDate, LocalDateTime endDate) {
//		eventManager.editEventTime(event, startDate, endDate);
//	}
}

