import impl.ContactImpl;
import impl.MeetingImpl;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import spec.Contact;
import spec.Meeting;

import static org.junit.Assert.assertEquals;

/**
 * Created by nathanhanak on 12/27/16.
 * Test class to test methods of MeetingImpl.
 */
public class MeetingImplShould {

  private Meeting meeting1;
  private Calendar futureDate;
  private Calendar pastDate;
  private Set<Contact> contacts;

  /**
   * Instantiates objects to conduct tests.
   */
  @Before
  public void setUp() throws Exception {
    futureDate = new GregorianCalendar(2050, 06, 06);
    pastDate = new GregorianCalendar(2014, 02, 03);
    contacts = new HashSet<Contact>();
    for (int i = 0; i < 100; i++) {
      Contact person = new ContactImpl("Name" + i);
      contacts.add(person);
    }
    meeting1 = new MeetingImpl(contacts, futureDate);
  }

  @Test
  public void returnMeetingsWithIdsOneHigherThanGetter() {
    int expected = MeetingImpl.getAllMeetingIdCounter() + 1;
    Meeting newMeeting = new MeetingImpl(contacts, futureDate);
    assertEquals(expected, newMeeting.getId());
    expected = MeetingImpl.getAllMeetingIdCounter() + 1;
    newMeeting = new MeetingImpl(contacts, pastDate);
    assertEquals(expected, newMeeting.getId());
  }

  @Test
  public void returnDateInputWhenCallingGetDate() {
    Calendar result = meeting1.getDate();
    Calendar expectedDate = new GregorianCalendar(2050, 06, 06);
    assertEquals(expectedDate, result);
  }

  @Test
  public void returnASizeOf100WhenCallingGetContacts() {
    assertEquals(100, meeting1.getContacts().size());
  }

  @Test
  // tests to make sure I can alter the static ID counter
  // any new spec.Meeting created after should be
  public void createAContactWithAnID1HigherThanParamOfSetMax() {
    MeetingImpl.setAllMeetingIdCounter(555);
    Meeting newMeeting = new MeetingImpl(contacts, futureDate);
    assertEquals(556, newMeeting.getId());
    MeetingImpl.setAllMeetingIdCounter(0);
    newMeeting = new MeetingImpl(contacts, pastDate);
    assertEquals(1, newMeeting.getId());
  }

}