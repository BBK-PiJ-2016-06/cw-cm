import java.util.*;
import java.util.concurrent.Future;

/**
 * Created by nathanhanak on 12/28/16.
 *
 * A class to manage your contacts and meetings.
 *
 */
public class ContactManagerImpl implements ContactManager{

    private Set<Contact> allKnownContacts = new HashSet<>();
    private List<FutureMeeting> futureMeetingList = new ArrayList<>();
    private List<PastMeeting> pastMeetingList = new ArrayList<>();

    /**
     * Add a new meeting to be held in the future.
     *
     * An ID is returned when the meeting is put into the system. This
     * ID must be positive and non-zero.
     *
     * @param contacts a set of contacts that will participate in the meeting
     * @param date the date on which the meeting will take place
     * @return the ID for the meeting
     * @throws IllegalArgumentException if the meeting is set for a time
     *       in the past, of if any contact is unknown / non-existent.
     * @throws NullPointerException if the meeting or the date are null
     */
    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
       if (calendarOccursIn(date).equals("past")) {
            throw new IllegalArgumentException("Date is in the past");
        }
        FutureMeeting newMeeting = new MeetingImpl(contacts, date);
        futureMeetingList.add(newMeeting);
        return newMeeting.getId();
    }

    @Override
    public PastMeeting getPastMeeting(int id) {
        return null;
    }

    @Override
    public FutureMeeting getFutureMeeting(int id) {
        return null;
    }

    @Override
    public Meeting getMeeting(int id) {
        return null;
    }

    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        return null;
    }

    @Override
    public List<Meeting> getMeetingListOn(Calendar date) {
        return null;
    }

    @Override
    public List<PastMeeting> getPastMeetingListFor(Contact contact) {
        return null;
    }

    /**
     * Create a new record for a meeting that took place in the past.
     *
     * @param contacts a set of participants
     * @param date the date on which the meeting took place
     * @param text messages to be added about the meeting.
     * @return the ID for the meeting
     * @throws IllegalArgumentException if the list of contacts is
     *     empty, if any of the contacts does not exist, or if
     *     the date provided is in the future
     * @throws NullPointerException if any of the arguments is null
     */
    @Override
    public int addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        return 0;
    }

    @Override
    public PastMeeting addMeetingNotes(int id, String text) {
        return null;
    }

    /**
     * Create a new contact with the specified name and notes.
     *
     * @param name the name of the contact.
     * @param notes notes to be added about the contact.
     * @return the ID for the new contact
     * @throws IllegalArgumentException if the name or the notes are empty strings
     * @throws NullPointerException if the name or the notes are null
     */
    @Override
    public int addNewContact(String name, String notes) {
        if (name.equals("") || notes.equals("")) {
            throw new IllegalArgumentException("Passed an empty String parameter");
        }
        Contact newContact = new ContactImpl(name);
        newContact.addNotes(notes);
        allKnownContacts.add(newContact);
        return newContact.getId();
    }

    @Override
    public Set<Contact> getContacts(String name) {
        if (name.equals("")) {
            return allKnownContacts;
        }
        return null;
    }

    @Override
    public Set<Contact> getContacts(int... ids) {
        return null;
    }

    @Override
    public void flush() {

    }

    /**
     * Method which checks if a contact exists in allKnownContacts
     * @throws IllegalArgumentException if not
     * @param contact to be checked
     */
    /** METHOD IS BELOW
    private boolean contactIsKnown throws IllegalArgumentException (Contact contact){
        // refer against allKnownContacts field
        // have it throw IllegalArgumentException if it does not
        // need to write addNewContact method first
    }

    */

    /**
     * Method which returns a String indicating when in time meeting occurs
     * by comparing against a Calendar set to the current date
     * @param calendarToCheck a Calendar object
     * @return String containing the result of the operation
     */
    private String calendarOccursIn(Calendar calendarToCheck) {
        Calendar currentDate = Calendar.getInstance();
        String result = "future";
        if (calendarToCheck.compareTo(currentDate) < 0) {
            return "past";
        } else {
            return result;
        }
    }

}



