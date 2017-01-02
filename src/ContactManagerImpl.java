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
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) throws IllegalArgumentException {
        contacts.parallelStream().forEach(contact -> contactIsKnown(contact)); // checks all Contacts are known
        if (calendarOccursIn(date).equals("past")) {
            throw new IllegalArgumentException("Date is in the past");
        }
        FutureMeeting newMeeting = new MeetingImpl(contacts, date);
        futureMeetingList.add(newMeeting);
        return newMeeting.getId();
    }


    @Override
    public PastMeeting getPastMeeting (int id) throws IllegalStateException {
        if (returnMeeting(futureMeetingList, id) != null ) {
            throw new IllegalStateException( "ID belongs to a Future Meeting");
        }
        return returnMeeting(pastMeetingList, id);
    }

    @Override
    public FutureMeeting getFutureMeeting(int id) {
        if (returnMeeting(pastMeetingList, id) != null ) {
            throw new IllegalStateException( "ID belongs to a Past Meeting");
        }
        return returnMeeting(futureMeetingList, id);
    }

    /**
     * Returns the meeting with the requested ID, or null if it there is none.
     *
     * @param id the ID for the meeting
     * @return the meeting with the requested ID, or null if it there is none.
     */
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

    @Override
    public int addNewPastMeeting (Set<Contact> contacts, Calendar date, String text) {
        contacts.parallelStream().forEach(contact -> contactIsKnown(contact));
        if (calendarOccursIn(date).equals("future") || contacts.isEmpty()) {
            throw new IllegalArgumentException("Attempted to pass a Calendar in the future through" +
                    "addNewPastMeeting() or contacts is empty");
        }
        PastMeeting newPastMeeting = new PastMeetingImpl(contacts, date, text);
        pastMeetingList.add(newPastMeeting);
        return newPastMeeting.getId();
    }

    @Override
    public PastMeeting addMeetingNotes(int id, String text) {
        return null;
    }

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
     * @param contact to be checked
     * @return true as long as the contact is previously known
     * @throws IllegalArgumentException if not
     */
    private boolean contactIsKnown(Contact contact) throws IllegalArgumentException {
        if (!allKnownContacts.contains(contact)) {
          throw new IllegalArgumentException("Unknown contact passed through parameter");
        }
        return true;
    }


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

    /**
     * Method which searches a List<T> meetingList for a meeting containing
     * the identifying int
     * @param meetingList the List<T> of meetings to search through
     * @param id the identifying id of the meeting
     * @param <T> The type of meeting (can be FutureMeeting, PastMeeting, etc)
     * @return a meeting of the specified requirements or a null meeting if there is no
     *          meeting within the list
     */
    private <T extends Meeting> T returnMeeting(List<T> meetingList, int id) {
            return meetingList.parallelStream()
                    .filter(m -> m.getId() == id)
                    .findFirst()
                    .orElse(null);
    }

}



