package contactmanagerimpltests;

import impl.ContactManagerImpl;
import impl.MeetingImpl;

import java.io.File;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import spec.Contact;
import spec.ContactManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Class to test features of the flush method for ContactManagerImpl class.
 * Created by nathanhanak on 1/9/17.
 */
public class ContactManagerImplFlushShould {

  private ContactManager myContactManager;
  private ContactManager contactManager2;
  private Calendar futureDate;
  private Calendar pastDate;
  private int uniqueId1;
  private int uniqueId2;
  private Set<Contact> contactSet;

  /**
   * Instantiates objects to conduct tests.
   */
  @Before
  public void setUp() throws Exception {
    myContactManager = new ContactManagerImpl();
    for (int i = 0; i < 1000; i++) {
      myContactManager.addNewContact("Name " + i, "Notes " + i);
    }
    futureDate = new GregorianCalendar(2112, 10, 06);
    pastDate = new GregorianCalendar(1969, 11, 12);
    uniqueId1 = myContactManager.addNewContact("Unique1", "notes1");
    uniqueId2 = myContactManager.addNewContact("Unique2", "notes2");
    contactSet = myContactManager.getContacts(uniqueId1, uniqueId2);
  }

  /**
   * Deconstructs objects in order to run tests.
   * Deletes the created file especially necessary to test all functionality of flush.
   */
  @After
  public void cleanUp() {
    File file = new File("ContactManagerFile.txt");
    boolean result = file.delete();
    if (!result) {
      throw new RuntimeException("file did not delete");
    }
  }

  @Test
  public void maintainContactSizeOnCreatingNewContactManagerFromOldContactManager() {
    int expectedSizeOfAllContacts = myContactManager.getContacts("").size();
    myContactManager.flush();
    ContactManager contactManager2 = new ContactManagerImpl();
    assertEquals(expectedSizeOfAllContacts, contactManager2.getContacts("").size());
  }

  @Test
  public void returnSameUniqueContactsFromGetFutureFutureMeetingListAfterFlushing() {
    final String expectedString = uniqueId1 + "" + uniqueId2;
    int futureMeetingId = myContactManager.addFutureMeeting(contactSet, futureDate);
    myContactManager.flush();
    contactManager2 = new ContactManagerImpl();
    Set<Contact> resultContacts = contactManager2.getFutureMeeting(futureMeetingId).getContacts();
    List<Contact> resultsArrayList = resultContacts.stream()
                                   .sorted(Comparator.comparingInt(Contact::getId))
                                   .collect(Collectors.toList()); // need to guarantee order
    StringBuffer resultString = new StringBuffer();
    for (Contact c : resultsArrayList) {
      resultString.append(c.getId());
    }
    assertEquals(expectedString, resultString.toString());
  }

  @Test
  public void returnSameUniqueContactsIdFromGetPastMeetingListAfterFlushing() {
    final String expectedString = uniqueId1 + "" + uniqueId2;
    int pastMeetingId = myContactManager.addNewPastMeeting(contactSet, pastDate, "some notes");
    myContactManager.flush();
    contactManager2 = new ContactManagerImpl();
    Set<Contact> resultContacts = contactManager2.getPastMeeting(pastMeetingId).getContacts();
    List<Contact> resultsArrayList = resultContacts.stream()
                                   .sorted(Comparator.comparingInt(Contact::getId))
                                   .collect(Collectors.toList()); // need to guarantee order
    StringBuffer resultString = new StringBuffer();
    for (Contact c : resultsArrayList) {
      resultString.append(c.getId());
    }
    assertEquals(expectedString, resultString.toString());
  }

  @Test
  public void returnNullWhenCallingGetFutureMeetingOfAnExpiredFutureMeetingAfterFlush() {
    Calendar currentTimePlus5sec = Calendar.getInstance();
    currentTimePlus5sec.add(Calendar.SECOND,  5);
    int futureMeetId = myContactManager.addFutureMeeting(contactSet, currentTimePlus5sec);
    try { // need to sleep to allow future meeting to become a past meeting
      for (int i = 0; i < 6; i++) {
        System.out.println("Sleeping..." + (6 - (i)));
        Thread.sleep(1000);
      }
    } catch (InterruptedException ex) {
      System.out.println("Was interrupted!");
    }
    myContactManager.flush();
    assertNull(myContactManager.getFutureMeeting(futureMeetId));
  }

  @Test
  public void storeLastMeetingIdAndReturnItWhenCallingNewConstructor() {
    final int expectedMeetingId = MeetingImpl.getAllMeetingIdCounter() + 1;
    myContactManager.flush();
    contactManager2 = new ContactManagerImpl();
    for (int i = 0; i < 10 ; i++) {
      contactManager2.addNewContact("Dude#" + i, "notes" + i);
    }
    Set<Contact> allCM2Contacts = contactManager2.getContacts("");
    int resultId = contactManager2.addFutureMeeting(allCM2Contacts, futureDate);
    assertEquals(expectedMeetingId, resultId);
  }

}