package impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import spec.Contact;
import spec.ContactManager;
import spec.FutureMeeting;
import spec.Meeting;
import spec.PastMeeting;

/**
 * Created by nathanhanak on 12/28/16.
 *
 * <p>A class to manage your contacts and meetings.</p>
 */
public class ContactManagerImpl implements ContactManager, Serializable {

  private Set<Contact> allKnownContacts = new HashSet<>();
  private List<FutureMeeting> futureMeetingList = new ArrayList<>();
  private List<PastMeeting> pastMeetingList = new ArrayList<>();
  //[0]= Meeting, [1]= Contact
  private List<Integer> savedIdList = new ArrayList<>(Arrays.asList(0, 0));
  private String saveFileName = "ContactManagerFile.txt";
  private File file = new File(saveFileName);

  /**
   * Constructor for ContactManagerImpl.
   */
  public ContactManagerImpl() {
    createFileIfNecessary();
    try (ObjectInputStream oIS = new ObjectInputStream(new FileInputStream(saveFileName))) {
      allKnownContacts = (Set<Contact>)oIS.readObject();
      futureMeetingList = (List<FutureMeeting>)oIS.readObject();
      pastMeetingList = (List<PastMeeting>)oIS.readObject();
      savedIdList = (List<Integer>)oIS.readObject();
      convertExpiredFutureMeetings();
    } catch (IOException ex) {
      ex.printStackTrace();
    } catch (ClassNotFoundException ex) {
      System.out.println("Class not found");
      ex.printStackTrace();
    }
    calibrateMeetingAndContactIds();
  }

  /**
   * Called when creating a new main.java.impl.ContactManagerImpl
   * Synchronizes main.java.impl.MeetingImpl and main.java.impl.ContactImpl static ID counters
   * to their last values - when flush() was last called.
   */
  private void calibrateMeetingAndContactIds() {
    MeetingImpl.setAllMeetingIdCounter(savedIdList.get(0));
    ContactImpl.setAllContactIdCounter(savedIdList.get(1));
  }

  /**
   * method which runs on first Launch of a new main.java.impl.ContactManagerImpl
   * Runs if the expected file has not been created or
   * if it was deleted previously, it will create a new blank one
   */
  private void createFileIfNecessary() {
    try {
      if (!file.exists()) {
        if (file.createNewFile()) {
          flush();
        } else {
          throw new IOException("Not able to create file");
        }
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Method which runs every time constructor or flush is called. Checks to see
   * if any of the dates in futureMeetingList have past and adds them
   * to pastMeetingList
   */
  private void convertExpiredFutureMeetings() {
    Calendar current = Calendar.getInstance();
    futureMeetingList.parallelStream()
            .filter(m -> m.getDate().compareTo(current) < 0)
            .forEach(f -> addNewPastMeeting(f.getContacts(), f.getDate(), "Meeting complete"));
    trimExpiredFutureMeetings(current);
  }

  /**
   * Culls futureMeetingList of any FutureMeeting objects with dates occurring in past.
   * @param current Calendar object reflecting exact local time method called.
   */
  private void trimExpiredFutureMeetings(Calendar current) {
    futureMeetingList.removeIf(m -> m.getDate().compareTo(current) < 0);
  }

  @Override
  public int addFutureMeeting(Set<Contact> contacts, Calendar date)
          throws IllegalArgumentException {
    // checks all Contacts are known, throws exception if not.
    contacts.parallelStream().forEach(contact -> contactIsKnown(contact));
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
  public PastMeeting getPastMeeting(int id) throws IllegalStateException {
    if (returnMeetingById(futureMeetingList, id) != null) {
      throw new IllegalStateException("ID belongs to a Future Meeting");
    }
    return returnMeetingById(pastMeetingList, id);
  }

  @Override
  public FutureMeeting getFutureMeeting(int id) {
    if (returnMeetingById(pastMeetingList, id) != null) {
      throw new IllegalStateException("ID belongs to a Past Meeting");
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

  /**
   * Method which returns a list of Meetings occuring on the same calendar date.
   * @param date a calendar to use as a search reference.
   * @param meetingList a list of Meetings to search through.
   * @return a List containing meetings which occur on the date passed in the param.
   */
  private List<Meeting> getMeetingListOn(Calendar date, List<? extends Meeting> meetingList) {
    return meetingList.parallelStream()
            .filter(m -> (m.getDate().get(Calendar.YEAR) == date.get(Calendar.YEAR))
                    && (m.getDate().get(Calendar.MONTH) == date.get(Calendar.MONTH))
                    && (m.getDate().get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)))
            .sorted(Comparator.comparing(Meeting::getDate))
            .collect(Collectors.toList());
  }

  @Override
  public List<PastMeeting> getPastMeetingListFor(Contact contact) throws IllegalArgumentException {
    return returnsMeetingListByContact(pastMeetingList, contact);
  }

  @Override
  public int addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
    Objects.requireNonNull(contacts, "Null Set<Contact>");
    Objects.requireNonNull(date, "Null Calendar");
    Objects.requireNonNull(text, "Null String");
    contacts.parallelStream().forEach(contact -> contactIsKnown(contact));
    if (calendarOccursIn(date).equals("future") || contacts.isEmpty()) {
      throw new IllegalArgumentException("Calendar must not be Future or contacts is empty");
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
    if (retrievedPastMeeting instanceof PastMeetingImpl) {
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
      return allKnownContacts.parallelStream()
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
            .filter(p -> (Arrays.stream(ids).anyMatch(i -> i == p.getId())))
            .sorted(Comparator.comparing(Contact::getId))
            .collect(Collectors.toSet());
    if (resultSet.size() != ids.length) {
      throw new IllegalArgumentException("Passed a non-existent ID");
    } else {
      return resultSet;
    }
  }

  @Override
  public void flush() {
    saveLatestMeetingAndContactIds();
    try (ObjectOutputStream oOS = new ObjectOutputStream(new FileOutputStream(saveFileName))) {
      convertExpiredFutureMeetings();
      oOS.writeObject(allKnownContacts);
      oOS.writeObject(futureMeetingList);
      oOS.writeObject(pastMeetingList);
      oOS.writeObject(savedIdList);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Method which saves static id counters of associated classes.
   */
  private void saveLatestMeetingAndContactIds() {
    savedIdList.set(0, MeetingImpl.getAllMeetingIdCounter());
    savedIdList.set(1, ContactImpl.getAllContactIdCounter());
  }


  /**
   * Method which checks if a contact has previously been created by
   * looking in allKnownContacts.
   * @param contact to be checked.
   * @return true as long as the contact has been created and added to allKnownContacts.
   * @throws IllegalArgumentException if contact is not contained in allKnownContacts.
   */
  private boolean contactIsKnown(Contact contact) throws IllegalArgumentException {
    if (!allKnownContacts.contains(contact)) {
      throw new IllegalArgumentException("Unknown contact passed through parameter");
    }
    return true;
  }

  /**
   * Method which returns a String indicating when in time meeting occurs
   * by comparing against a Calendar set to the current date.
   * @param calendarToCheck a Calendar object
   * @return String containing the result of the operation
   */
  private String calendarOccursIn(Calendar calendarToCheck) {
    return calendarToCheck.compareTo(Calendar.getInstance()) < 0 ? "past" : "future";
  }

  /**
   * Method which searches a generic Meeting List meetingList for a meeting containing
   * the identifying int.
   * @param meetingList the List of meetings to search through
   * @param id the identifying id of the meeting
   * @param <T> The type of meeting (can beFutureMeeting, PastMeeting, etc)
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
   * returns a Meeting List containing all meetings with specified contact.
   * @param meetingList a list of meetings to search.
   * @param contact a specified contact whose participating meetings we are searching for
   * @return a list of meetings (past or future) in time frame containing contact.
   * @throws IllegalArgumentException if the contact is not contained in allKnownContacts.
   * @throws NullPointerException if the contact is null
   */
  private <T extends Meeting> List<T> returnsMeetingListByContact(
      List<? extends T> meetingList, Contact contact)
          throws IllegalArgumentException, NullPointerException {
    Objects.requireNonNull(contact, "Null Contact");
    contactIsKnown(contact);
    return   meetingList.parallelStream()
            .filter(m -> m.getContacts().contains(contact))
            .sorted(Comparator.comparing(Meeting::getDate))
            .collect(Collectors.toList());
  }




}



