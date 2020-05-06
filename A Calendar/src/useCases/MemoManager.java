package useCases;

import com.sun.istack.internal.Nullable;
import entities.Event;
import entities.Memo;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * this is inner class of CalendarManager.
 * It handles only Memo related methods.
 */
public class MemoManager {

	private ArrayList<Memo> allMemos;

	private Hashtable<Memo, Memo> MemoToMemo = new Hashtable<>();
	private Hashtable<Event, Memo> EventToMemo = new Hashtable<>();

	protected MemoManager(){
		allMemos = new ArrayList<>();
	}

// Create memo(s)
	/**
	 * Create a memo with notes and no association
	 * @param note The note/message of the memo
	 * @return the memo created
	 */
	public Memo createMemo(String note) {

		Memo newMemo = new Memo(note);
		allMemos.add(newMemo);
		return newMemo;
	}

	/**
	 * This imports a list of memos
	 * @param memos the list of memo to be added.
	 */
	public void addMemos(ArrayList<Memo> memos) {
		this.allMemos.addAll(memos);
	}

	// Create association between given event and memos
	/**
	 * This associate a event with a memo
	 * @param event the event to be associated
	 * @param memo the memo to be associated
	 */
	public void associateMemo(Event event, Memo memo) {
		event.setMemo(memo);
		memo.addEvent(event);
		EventToMemo.put(event, memo);
	}

	// Create association between given memo and memo
	/**
	 * This creates a association between two memos
	 * @param memo1 the first memo to be associated
	 * @param memo2 the second memo to be associated
	 */
	public void associateMemo(Memo memo1, Memo memo2) {

		memo1.addMemo(memo2);
		memo2.addMemo(memo1);
		MemoToMemo.put(memo1, memo2);
		MemoToMemo.put(memo2, memo1);
	}

	/**
	 * This creates a one way association between two memos.
	 * This is for reload/rebuild this class
	 * @param memo1 the memo to be associated
	 * @param memo2 the memo to be associated with
	 */
	public void oneWayAssociateMemo(Memo memo1, Memo memo2) {
		MemoToMemo.put(memo1, memo2);
	}

	/**
	 * A getter method for the hashtable that stores the association between memos
	 * @return the hashtable that stores the association between memos
	 */
	public Hashtable<Memo, Memo> getMemoToMemo() { return MemoToMemo; }

	/**
	 * A getter method for the hashtable that stores the association between event and memo
	 * @return the hashtable that stores the association between event and memo
	 */
	public Hashtable<Event, Memo> getEventToMemo() { return EventToMemo; }

	/**
	 * This is a getter method for all memo of this user
	 * @return all memo of this user
	 */
	public ArrayList<Memo> getAllMemos() { return new ArrayList<>(allMemos); }

	/* get a Memo by ID */
	public Memo getMemobyID(String ID){
		for (Memo m: allMemos){
			if (m.getID().equals(ID))
				return m;
		}
		System.out.println("There is no such event");
		return null;
	}

	public void deleteMemo(Memo memo){
		int i = 0;
		int initial_length = allMemos.size();
		ArrayList<Integer> toBeDeletedindex = new ArrayList();
		while (i < allMemos.size()){
			if (allMemos.get(i).equals(memo)){
				toBeDeletedindex.add(i);
			}
			i = i + 1;
		}
		for (Integer i_1: toBeDeletedindex){
            removeFromHash(allMemos.get(i_1));
			allMemos.remove(i_1);
		}
		if (initial_length == i)
			System.out.println("There is no such memo");
	}
	/*delete given event from hashtable*/
	public void deleteEvent(Event event){
		EventToMemo.remove(event);
	}

	/*helper method to remove an memo from the hashtable above. Pre: there exists such memo in hashtable */
	protected void removeFromHash(Memo memo){
		MemoToMemo.remove(memo);
		@Nullable Event flag1 = null;
		@Nullable Memo flag2 = null;
		/* find and delete from 2 hashtable*/
		for (Event e: EventToMemo.keySet()){
			if (e.getMemo().equals(memo))
				e.removeMemo();
				flag1 = e;
		}
		for (Memo m: MemoToMemo.keySet()){
			if (MemoToMemo.get(m).equals(memo))
				flag2 = m;
		}
		EventToMemo.remove(flag1);
		MemoToMemo.remove(flag2);
	}

	public void editNote(String ID, String content){
		int i = 0;
		for (Memo m: allMemos){
			if (m.getID().equals(ID)) {
				m.editNote(content);
				i = i + 1;
			}
		}
		if (i == 0)
			System.out.println("There is no such memo");
		}
}
