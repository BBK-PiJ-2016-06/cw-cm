import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by nathanhanak on 12/28/16.
 *
 * Test class for ContactManagerImpl
 */
public class ContactManagerImplShould {

    private ContactManagerImpl;
    private Meeting meeting1;
    private Set<Contact> contactSet1;
    private Calendar futureDate;
    private ContactManager contactManager;
    private FutureMeeting futureMeeting;


    @Before
    public void setUp() throws Exception {
        futureDate = new GregorianCalendar(2050, 06, 06);
        contactSet1 = new HashSet<Contact>();
        for ( int i = 0; i < 100; i++ ) {
            Contact person = new ContactImpl("Name" + i);
            contactSet1.add(person);
        }
        contactManager = new ContactManagerImpl();
    }

    @Test
    public void return1WhenCallingAddFutureMeeting() {
        assertEquals(1, contactManager.addFutureMeeting(contactSet1, futureDate))
    }

}