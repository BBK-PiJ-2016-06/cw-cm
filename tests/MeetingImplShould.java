import org.junit.Ignore;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by nathanhanak on 12/27/16.
 *
 * Test Class to test Methods of MeetingImpl
 */
public class MeetingImplShould {

    private Meeting meeting1;
    private MeetingImpl meeting2;
    private Calendar futureDate;
    private Set<Contact> contacts;

    @Before
    public void setUp() throws Exception {
        futureDate = new GregorianCalendar(2050, 06, 06);
        meeting1 = new MeetingImpl(contacts, futureDate);
        meeting2 = new MeetingImpl(contacts, futureDate);
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

    //add contacts to contacts in the @Before and work from there
}