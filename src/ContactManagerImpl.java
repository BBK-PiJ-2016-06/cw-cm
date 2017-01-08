import java.time.Year;
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
        Objects.requireNonNull(contacts, "Null Set<contacts>");
        Objects.requireNonNull(date, "Null Calendar");
        if (calendarOccursIn(date).equals("past")) {
            throw new IllegalArgumentException("Date is in the past");
        }
        FutureMeeting newMeeting = new FutureMeetingImpl(contacts, date);
        futureMeetingList.add(newMeeting);
        return newMeeting.getId();
    }


    @Override
    public PastMeeting getPastMeeting (int id) throws IllegalStateException {
        if (returnMeetingById(futureMeetingList, id) != null ) {
            throw new IllegalStateException( "ID belongs to a Future Meeting");
        }
        return returnMeetingById(pastMeetingList, id);
    }

    @Override
    public FutureMeeting getFutureMeeting(int id) {
        if (returnMeetingById(pastMeetingList, id) != null ) {
            throw new IllegalStateException( "ID belongs to a Past Meeting");
        }
        return returnMeetingById(futureMeetingList, id);
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
        Objects.requireNonNull(date, "Null Calendar");
        if (calendarOccursIn(date).equals("past")) {
            return getMeetingListOn(date, pastMeetingList);
        } else {
            return getMeetingListOn(date, futureMeetingList);
        }
    }

    private List<Meeting> getMeetingListOn(Calendar date, List<? extends Meeting> meetingList) {
        return meetingList.parallelStream()
                            .filter(m ->    (m.getDate().get(Calendar.YEAR) == date.get(Calendar.YEAR)) &&
                                            (m.getDate().get(Calendar.MONTH) == date.get(Calendar.MONTH)) &&
                                            (m.getDate().get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)) )
                            .sorted(Comparator.comparing(Meeting::getDate))
                            .collect(Collectors.toList());
    }

    @Override
    public List<PastMeeting> getPastMeetingListFor(Contact contact) throws IllegalArgumentException {
        return returnsMeetingListByContact(pastMeetingList, contact);
    }

    @Override
    public int addNewPastMeeting (Set<Contact> contacts, Calendar date, String text) {
        Objects.requireNonNull(contacts, "Null Set<Contact>");
        Objects.requireNonNull(date, "Null Calendar");
        Objects.requireNonNull(text, "Null String");
        contacts.parallelStream().forEach(contact -> contactIsKnown(contact));
        if (calendarOccursIn(date).equals("future") || contacts.isEmpty()) {
            throw new IllegalArgumentException("Attempted to pass a Calendar in the future through or contacts is empty");
        }
        PastMeeting newPastMeeting = new PastMeetingImpl(contacts, date);
        pastMeetingList.add(newPastMeeting);
        newPastMeeting = addMeetingNotes(newPastMeeting.getId(), text);
        return newPastMeeting.getId();
    }

    @Override
    public PastMeeting addMeetingNotes(int id, String text) {
        Objects.requireNonNull(text, "Null String");
        PastMeeting retrievedPastMeeting = getPastMeeting(id);
        if (retrievedPastMeeting == null) {
            throw new IllegalArgumentException("Meeting does not exist");
        }
        if ( retrievedPastMeeting instanceof PastMeetingImpl) {
            ((PastMeetingImpl) retrievedPastMeeting).addNotes(text);
        }
        return retrievedPastMeeting;
    }

    @Override
    public int addNewContact(String name, String notes) {
        Objects.requireNonNull(name, "Null String");
        Objects.requireNonNull(notes, "Null String");
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
        Objects.requireNonNull(name, "Null String");
        if (name.equals("")) {
            return  allKnownContacts;
        } else {
            return  allKnownContacts.parallelStream()
                                    .filter(c -> c.getName().equals(name))
                                    .collect(Collectors.toSet());
        }
    }

    /**
     * Returns a set containing the contacts that correspond to the IDs.
     * Note that this method can be used to retrieve just one contact by passing only one ID.
     *
     * @param ids an arbitrary number of contact IDs
     * @return a set containing the contacts that correspond to the IDs.
     * @throws IllegalArgumentException if no IDs are provided or if
     *     any of the provided IDs does not correspond to a real contact
     */
    @Override
    public Set<Contact> getContacts(int... ids) {
        if (ids.length == 0) {
            throw new IllegalArgumentException("No IDs provided");
        }
        Set<Contact> resultSet = new HashSet<>();
        for (int i : ids) {
            Contact contact = getContactById(i);
            if (contact == null) {
                throw new IllegalArgumentException("ID " + i + " does not exist.");
            } else {
                resultSet.add(contact);
            }
        }
        return resultSet;
    }

    private Contact getContactById(int id) {
        return allKnownContacts.parallelStream()
                                    .filter(c -> c.getId() == id)
                                    .findAny()
                                    .orElse(null);
    }


    @Override
    public void flush() {

    }

    /**
     * Method which checks if a contact has previously been created by
     * looking in allKnownContacts
     * @param contact to be checked
     * @return true as long as the contact has been created and added to allKnownContacts
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
    private <T extends Meeting> T returnMeetingById(List<T> meetingList, int id) {
            return   meetingList.parallelStream()
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
     * @throws NullPointerException if the contact is null
     */
    private <T extends Meeting> List<T> returnsMeetingListByContact(List<? extends T> meetingList, Contact contact)
            throws IllegalArgumentException, NullPointerException {
        Objects.requireNonNull(contact, "Null Contact");
        contactIsKnown(contact);
        return   meetingList.parallelStream()
                            .filter(m -> m.getContacts().contains(contact))
                            .sorted(Comparator.comparing(Meeting::getDate))
                            .collect(Collectors.toList());
    }


}



