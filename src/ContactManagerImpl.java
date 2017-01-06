import java.util.*;
import java.util.stream.Collectors;

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


    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) throws IllegalArgumentException {
        contacts.parallelStream().forEach(contact -> contactIsKnown(contact)); // checks all Contacts are known
        if (calendarOccursIn(date).equals("past")) {
            throw new IllegalArgumentException("Date is in the past");
        }
        FutureMeeting newMeeting = new FutureMeetingImpl(contacts, date);
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

    @Override
    public Meeting getMeeting(int id) {
        Meeting meetingToReturn;
        try {
            meetingToReturn = getPastMeeting(id);
        } catch (IllegalStateException ex) {
            meetingToReturn = getFutureMeeting(id);
        }
        return meetingToReturn;
    }

    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) throws IllegalArgumentException {
        return returnsMeetingListByContact(futureMeetingList, contact);
    }

    @Override
    public List<Meeting> getMeetingListOn(Calendar date) {
        return null;
    }

    /**
     * Returns the list of past meetings in which this contact has participated.
     *
     * If there are none, the returned list will be empty. Otherwise,
     * the list will be chronologically sorted and will not contain any
     * duplicates.
     *
     * @param contact one of the user’s contacts
     * @return the list of future meeting(s) scheduled with this contact (maybe empty).
     * @throws IllegalArgumentException if the contact does not exist
     * @throws NullPointerException if the contact is null
     */
    @Override
    public List<PastMeeting> getPastMeetingListFor(Contact contact) throws IllegalArgumentException {
        return returnsMeetingListByContact(pastMeetingList, contact);
    }

    @Override
    public int addNewPastMeeting (Set<Contact> contacts, Calendar date, String text) {
        contacts.parallelStream().forEach(contact -> contactIsKnown(contact));
        if (text == null) {
            throw new NullPointerException("Notes are null");
        }
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

    /**
     * Returns a set with the contacts whose name contains that string.
     *
     * If the string is the empty string, this methods returns the set
     * that contains all current contacts.
     *
     * @param name the string to search for
     * @return a set with the contacts whose name contains that string.
     * @throws NullPointerException if the parameter is null
     */
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
     * @throws IllegalArgumentException if contact is not contained in allKnownContacts
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

    /**
     * Method which first checks if the contact is null, then
     * returns a List<T extends Meeting> based on which type of List is passed through parameters
     * containing all meetings with specified contact.
     * @param meetingList a list of meetings to search.
     * @param contact a specified contact whose participating meetings we are searching for
     * @param <? extends T> Meeting. A List of an type that is an extension of Meeting.
     * @return List<T extends Meeting> a list of all meetings in time frame containing contact
     * @throws IllegalArgumentException if the contact is not contained in allKnownContacts.
     */
    private <T extends Meeting> List<T> returnsMeetingListByContact(List<? extends T> meetingList, Contact contact)
            throws IllegalArgumentException {
        if (contact == null) {
            throw new NullPointerException("contact is null");
        }
        contactIsKnown(contact);
        return   meetingList.parallelStream()
                            .filter(m -> m.getContacts().contains(contact))
                            .sorted(Comparator.comparing(Meeting::getDate))
                            .collect(Collectors.toList());
    }

}



