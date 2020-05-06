package useCases;

import entities.Alert;
import entities.Event;
import entities.Memo;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * this is inner class of CalendarManager.
 * It handles only Event and some Alert related methods.
 */
public class EventManager {

	private ArrayList<Event> allEvents;
	private ArrayList<Event> postponedEvents;


	protected EventManager() {
		allEvents = new ArrayList<>();
	}

// Create event(s)

	/**
	 * Create and return a single event with name
	 *
	 * @param duration The array that stores the start date and end date of this event.
	 * @param name     The mame of this event.
	 * @return return the event created
	 */
	public Event createEvent(LocalDateTime[] duration, String name) {

		Event newEvent = new Event(name, duration);
		allEvents.add(newEvent);
		return newEvent;
	}

	/**
	 * Create and return a single event with name
	 *
	 * @param event1    The event that all data except date is copied from.
	 * @param startDate The Start Date of this event.
	 * @param endDate   The End Date of this event.
	 * @return return the event created
	 */
	public Event recreateEvent(Event event1, LocalDateTime startDate, LocalDateTime endDate) {
		String name = event1.getName();
		ArrayList<Alert> alerts = event1.getAlerts();
		ArrayList<String> series = event1.getSeriesNames();
		ArrayList<String> tags = event1.getTags();
		Memo memo = event1.getMemo();
		return new Event(name, startDate, endDate, alerts, series, tags, memo);
	}

	/**
	 * Postpone indefinitely this event.
	 *
	 * @param event The event that'll get postponed.
	 */
	public void postponeEvent(Event event) {
		event.postponeEvent();
		this.allEvents.remove(event);
		this.postponedEvents.add(event);
	}

	/**
	 * Edit the time of this event.
	 *
	 * @param event     The event that will get its time edited.
	 * @param startDate The Start Date of this event
	 * @param endDate   The End Date of this event
	 */
	public void editEventTime(Event event, LocalDateTime startDate, LocalDateTime endDate) {
		event.editEventTime(startDate, endDate);
		if (this.postponedEvents.contains(event)) {
			this.postponedEvents.remove(event);
			this.allEvents.add(event);
		}
	}

	/**
	 * This imports a list of event.
	 *
	 * @param events The list of event
	 */
	public void addEvents(ArrayList<Event> events) {
		this.allEvents.addAll(events);
	}

	/**
	 * Create and return a series of event with same event name and series name
	 * NOTE: 2 series with same series name will be viewed as the same series
	 * @param duration       The array that stores the start date and end date of the first event.
	 * @param freq           The frequency of this series of events.
	 * @param numberOfEvents The number of events in this series
	 * @param name           The mame of this series of events and the series.
	 */
	public void createFreqEvent(LocalDateTime[] duration, int freq, int numberOfEvents, String name) {

		//ArrayList<Event> output = new ArrayList<>();
		for (int i = 0; i < numberOfEvents; i++){
			Event newEvent = this.createEvent(duration, name);
			newEvent.addSeriesNames(name);
			//output.add(newEvent);
			duration[0] = duration[0].plusDays(freq);
			duration[1] = duration[1].plusDays(freq);
		}
	}

	// Return list of events
	/**
	 * This is a getter method for all events of this user
	 * @return all events of this user
	 */
	public ArrayList<Event> getAllEvents() {
		return new ArrayList<>(allEvents);
	}

	/**
	 * This is a getter method for all past events of this user
	 * @return all past events of this user
	 */
	public ArrayList<Event> getPastEvents(){

		ArrayList<Event> output = new ArrayList<>();
		for (Event event: allEvents){
			if (event.checkEndBefore(LocalDateTime.now()) ){ output.add(event);	}
		}
		return output;
	}

	/**
	 * This is a getter method for all on-going events of this user
	 * @return all on-going events of this user
	 */
	public ArrayList<Event> getCurrentEvents(){	return SearchByDate(LocalDateTime.now()); }

	/**
	 * This is a getter method for all further events of this user
	 * @return all further events of this user
	 */
	public ArrayList<Event> getFutureEvents(){

		ArrayList<Event> output = new ArrayList<>();
		for (Event event: allEvents){
			if (event.checkStartAfter(LocalDateTime.now()) ){ output.add(event);	}
		}

		return output;
	}

// Or return events with given date, name, tag, memo or series name
	/**
	 * This search all event and return a list of event that is on-going at the given time/date
	 * @param date the date/time used to search by
	 * @return all all events that is on-going at the given time/date
	 */
	public ArrayList<Event> SearchByDate(LocalDateTime date){

		ArrayList<Event> output = new ArrayList<>();
		for (Event event: allEvents){
			if (event.checkOnGoing(date)) { output.add(event);}
		}

		return output;
	}

	public ArrayList<Event> EventsOnSpecificDate(LocalDateTime date){
		ArrayList<Event> output = new ArrayList<>();
		for(Event event: allEvents){
			if (event.checkOnGoing(date) || eventOnDate(event, date)){
				output.add(event);
			}
		}
		return output;
	};

	private boolean eventOnDate(Event event, LocalDateTime date){
		return (event.getStartDate().getYear() == date.getYear() &&
				event.getStartDate().getDayOfMonth() == date.getDayOfMonth() &&
				event.getStartDate().getMonth() == date.getMonth()) ||
				(event.getEndDate().getYear() == date.getYear() &&
				event.getEndDate().getDayOfMonth() == date.getDayOfMonth() &&
				event.getEndDate().getMonth() == date.getMonth());
	}
	/**
	 * This search all event and return a list of event that has the given name
	 * @param name the name used to search by
	 * @return a list of event that has the given name
	 */
	public ArrayList<Event> SearchByName(String name){

		ArrayList<Event> output = new ArrayList<>();
		for (Event event: allEvents){
			if (event.checkName(name) ){ output.add(event);	}
		}

		return output;
	}

//	public ArrayList<Event> EventsOnDate(String month, int day, int year){
//		ArrayList<Event> output = new ArrayList<>();
//		LocalDateTime dateTime = LocalDateTime.parse();
//
//	}
	/**
	 * This search all event and return a list of event that has the given tag
	 * @param tag the name of the tag used to search by
	 * @return a list of event that has the given tag
	 */
	public ArrayList<Event> SearchByTag(String tag){

		ArrayList<Event> output = new ArrayList<>();
		for (Event event: allEvents){
			if (event.checkTag(tag) ){ output.add(event);	}
		}

		return output;
	}

	/**
	 * This search all event and return a list of event that has the given series name
	 * @param seriesName the name of the series used to search by
	 * @return a list of event that has the given tag
	 */
	public ArrayList<Event> SearchBySeries(String seriesName){

		ArrayList<Event> output = new ArrayList<>();
		for (Event event: allEvents){
			if (event.checkSeriesNames(seriesName) ){ output.add(event);	}
		}
		return output;
	}

	/**
	 * This is a getter method for all alert of this user
	 * @return all alert of this user
	 */
	public ArrayList<Alert> getAlerts(){

		ArrayList<Alert> output = new ArrayList<>();
		for (Event event: allEvents){
			output.addAll( event.getAlerts() );
		}
		return output;
	}

	/**
	 * this is a getter method that return Alerts with alert time between last login time and current login time.
	 * @return The Alerts from that time interval.
	 */
	public ArrayList<Alert> alertInTimeInterval(LocalDateTime lastLoginTime){
		ArrayList<Alert> output = new ArrayList<>();
		ArrayList<Alert> allAlert = this.getAlerts();
		if(lastLoginTime != null){
			for(Alert alert: allAlert) {
				if (alert.checkIsBetween(lastLoginTime, LocalDateTime.now())) { output.add(alert);	}
			}
		}
		return output;
	}

	/* get an event by ID */
	public Event getEventbyID(String ID){
		for (Event e: allEvents){
			if (e.getID().equals(ID))
				return e;
		}
		System.out.println("There is no such event");
		return null;
	}

	/* get an Alert by ID */
	public Alert getAlertbyID(String ID){
		for (Event e: allEvents){
			for (Alert a: e.getAlerts())
			{
				if (a.getID().equals(ID))
					return a;
			}
		}
		System.out.println("There is no such event");
		return null;
	}

	/** delete a selected event if there exists **/
	public void deleteEvent(Event event){
		int i = 0;
		int initial_length = allEvents.size();

		ArrayList<Integer> toBeDeletedindex = new ArrayList();
		while (i < allEvents.size()){
			if (allEvents.get(i).equals(event)){
				toBeDeletedindex.add(i);
			}
			i = i + 1;
		}
		for (int i_1: toBeDeletedindex) {
			allEvents.get(i_1).getMemo().removeEvent(event);
			allEvents.remove(i_1);
		}
		if (initial_length == i)
			System.out.println("There is no such event");
	}

	/** delete a selected Alert if there exists **/
	public void deleteAlert(LocalDateTime someTime){
		Alert someAlert = new Alert(someTime,"wht");
		int i = 0;
		while (i < allEvents.size()){
			if (allEvents.get(i).getAlerts().contains(someAlert)){
				allEvents.get(i).removeAlert(someAlert);
			}
			i = i + 1;
		}
	}
	/** editing an event **/
	public void editName(String ID, String Name){
		int i = 0;
		for (Event e: allEvents){
			if (e.getID().equals(ID)){
				e.editName(Name);
			i = i + 1;
			}
		}
		if (i == 0){
			System.out.println("There is no such Event");
		}
	}

	public void editDate(String ID, LocalDateTime start, LocalDateTime end){
		int i = 0;
		for (Event e: allEvents){
			if (e.getID().equals(ID)) {
				e.editDate(start, end);
				i += 1;
			}
		}
		if (i == 0){
			System.out.println("There is no such Event");
		}
	}
	/** editing an Alert **/
	public void editAlertMessage(String ID, String Message){
		int i = 0;
		for (Event e: allEvents){
			for (Alert a: e.getAlerts()){
				if (a.getID().equals(ID)){
					a.editMessage(Message);
				i += 1;
				}
			}
		}
		if (i == 0){
			System.out.println("There is no such Alert");
		}
	}

	public void editTime(String ID, LocalDateTime time){
		int i = 0;
		for (Event e: allEvents){
			for (Alert a: e.getAlerts()){
				if (a.getID().equals(ID)) {
					a.editTime(time);
					i += 1;
				}
			}
		}
		if (i == 0){
			System.out.println("There is no such Event");
		}
	}
}
