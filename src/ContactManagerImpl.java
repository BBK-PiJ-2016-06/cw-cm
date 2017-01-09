import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.Calendar;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by nathanhanak on 12/28/16.
 *
 * A class to manage your contacts and meetings.
 *
 */
public class ContactManagerImpl implements ContactManager, Serializable {

    private Set<Contact> allKnownContacts = new HashSet<>();
    private List<FutureMeeting> futureMeetingList = new ArrayList<>();
    private List<PastMeeting> pastMeetingList = new ArrayList<>();

    private String saveFileName = "ContactManagerFile.txt";
    private File file = new File(saveFileName);


    public ContactManagerImpl() {
        createFileIfNecessary();
        try ( ObjectInputStream oIS = new ObjectInputStream(new FileInputStream(saveFileName))) {
            allKnownContacts = (Set<Contact>)oIS.readObject();
            futureMeetingList = (List<FutureMeeting>)oIS.readObject();
            pastMeetingList = (List<PastMeeting>)oIS.readObject();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found");
            ex.printStackTrace();
        }
    }

    /**
     * method which runs on first Launch of a new ContactManagerImpl
     * Runs if the expected file has not been created or
     * if it was deleted previously, it will create a new blank one
     */
    private void createFileIfNecessary() {
        try {
            if (!file.exists()) {
                file.createNewFile();
                flush();
            }
        } catch (IOException ex) {
                ex.printStackTrace();
            }
    }


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
                                    .sorted(Comparator.comparing(Contact::getId))
                                    .collect(Collectors.toSet());
        }
    }


    @Override
    public Set<Contact> getContacts(int... ids) {
        if (ids.length == 0) {
            throw new IllegalArgumentException("No IDs provided");
        }
        Set<Contact> resultSet = allKnownContacts.stream()
                                                 .filter(p -> (Arrays.stream(ids).anyMatch(i -> i == p.getId())) )
                                                 .sorted(Comparator.comparing(Contact::getId))
                                                 .collect(Collectors.toSet());
        if (resultSet.size() != ids.length ) {
            throw new IllegalArgumentException("Passed a non-existent ID");
        } else {
            return resultSet;
        }
    }

    @Override
    public void flush() {
        try ( ObjectOutputStream oOS = new ObjectOutputStream(new FileOutputStream(saveFileName)) ) {
            oOS.writeObject(allKnownContacts);
            oOS.writeObject(futureMeetingList);
            oOS.writeObject(pastMeetingList);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
        return calendarToCheck.compareTo(Calendar.getInstance()) < 0 ? "past" : "future";
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
     * returns a List<T extends Meeting> containing all meetings with specified contact.
     * @param meetingList a list of meetings to search.
     * @param contact a specified contact whose participating meetings we are searching for
     * @param <? extends T> Meeting. A List of a type that is an extension of Meeting.
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



