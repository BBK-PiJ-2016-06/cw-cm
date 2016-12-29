import org.junit.Ignore;
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
 * Test Class to test Methods of MeetingImpl
 */
public class MeetingImplShould {

    private Meeting meeting1;
    private MeetingImpl meeting2;
    private PastMeeting pastMeeting;
    private Calendar futureDate;
    private Calendar pastDate;
    private Set<Contact> contacts;
    String notes;
    String emptyNotes;

    @Test
    public void name() throws Exception {

    }

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
        meeting2 = new MeetingImpl(contacts, futureDate);
        notes = "These are some notes";
        emptyNotes = "";
        pastMeeting = new MeetingImpl(contacts, pastDate);
    }

    @Test
    public void return1And2ForMeetingId() {
        assertEquals(1, meeting1.getId());
        assertEquals(2, meeting2.getId());
    }

    @Test
    public void returnDateInputWhenCallingGetDate() {
        Calendar result = meeting1.getDate();
        Calendar expectedDate = new GregorianCalendar(2050, 06, 06);
        assertEquals(expectedDate, result);
    }

    @Test
    public void changeDateFieldWhenCallingRescheduleMeeting(){
        Calendar changedDate = new GregorianCalendar(2050, 07, 07);
        meeting2.rescheduleMeeting(changedDate);
        assertEquals(changedDate, meeting2.getDate());
    }

    @Test
    public void returnASizeOf100WhenCallingGetContacts() {
        assertEquals(100, meeting1.getContacts().size());
    }

    @Test
    public void returnEmptyStringWhenCallingGetNotes() {
        assertEquals("", pastMeeting.getNotes());
    }
}