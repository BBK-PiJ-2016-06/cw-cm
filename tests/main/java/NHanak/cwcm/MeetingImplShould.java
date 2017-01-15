package main.java.NHanak.cwcm;

import main.java.NHanak.cwcm.Contact;
import main.java.NHanak.cwcm.ContactImpl;
import main.java.NHanak.cwcm.Meeting;
import main.java.NHanak.cwcm.MeetingImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Created by nathanhanak on 12/27/16.
 *
 * Test Class to test Methods of main.java.NHanak.cwcm.MeetingImpl
 */
public class MeetingImplShould {

    private Meeting meeting1;
    private Calendar futureDate;
    private Calendar pastDate;
    private Set<Contact> contacts;


    @Before
    public void setUp() throws Exception {
        futureDate = new GregorianCalendar(2050, 06, 06);
        pastDate = new GregorianCalendar(2014, 02, 03);
        contacts = new HashSet<Contact>();
        for ( int i = 0; i < 100; i++ ) {
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
    // any new main.java.NHanak.cwcm.Meeting created after should be
    public void createAContactWithAnID1HigherThanParamOfSetMax() {
        MeetingImpl.setAllMeetingIdCounter(555);
        Meeting newMeeting = new MeetingImpl(contacts, futureDate);
        assertEquals(556, newMeeting.getId());
        MeetingImpl.setAllMeetingIdCounter(0);
        newMeeting = new MeetingImpl(contacts, pastDate);
        assertEquals(1, newMeeting.getId());
    }

}