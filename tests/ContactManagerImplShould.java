import org.junit.Before;
import org.junit.Ignore;
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

    private Meeting meeting1;
    private Set<Contact> contactSet1;
    private Calendar futureDate;
    private ContactManager contactManager;
    private FutureMeeting futureMeeting;
    private Calendar dateInPast;
    private PastMeeting pastMeeting;

    @Before
    public void setUp() throws Exception {
        futureDate = new GregorianCalendar(2050, 06, 06);
        contactSet1 = new HashSet<Contact>();
        for ( int i = 0; i < 100; i++ ) {
            Contact person = new ContactImpl("Name" + i);
            contactSet1.add(person);
        }
        contactManager = new ContactManagerImpl();
        dateInPast = new GregorianCalendar(1969, 06, 01);
        pastMeeting = new PastMeetingImpl(contactSet1, dateInPast, "Meeting occurred in past");
    }

    @Test
    // tests for addFutureMeeting(Set<Contact> , Calendar)
    public void return1WhenCallingAddFutureMeeting() {
        assertEquals(1, contactManager.addFutureMeeting(contactSet1, futureDate));
    }

    @Test
    // test for addFutureMeeting(Set<Contact> , Calendar)
    public void throwIllegalArgExceptionWhenPassingPastDate(){
        boolean exceptionThrown = false;
        try {
            contactManager.addFutureMeeting(contactSet1, dateInPast);
        } catch (IllegalArgumentException ex) {
                exceptionThrown = true;
            }
        assertTrue(exceptionThrown);
    }

    // @Test
    // tests addFutureMeeting to see if all contacts in @param Set<Contact> are known
    // by calling on contactIsKnown
    //

    @Test
    public void returnPastMeetingByRequestedIdWhenCallingGetPastMeeting(){
        assertEquals(pastMeeting, contactManager.getPastMeeting(1));
    }

    @Test
    /**
     * tests to see if addnewContact(String, String) @return an int of newly created Contact
     * Expected result is 101 because 100 contacts previously created in @Before
     */
    public void returnAnIntWhenCallingAddNewContact() {
        int result = contactManager.addNewContact("Darth Vader", "I am your Father");
        assertEquals(101 , result);
    }

    @Test
    /**
     * test for addNewContact
     * Test to see if IllegalArgumentException is thrown for passing empty string @param
      */
    public void throwIllegalArgumentExceptionForPassingEmptyStrings() {
        boolean exceptionThrown = false;
        String empty = "";
        String notEmpty = "word";
        try {
            contactManager.addNewContact(empty, notEmpty);
        } catch (IllegalArgumentException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        exceptionThrown = false;
        try {
            contactManager.addNewContact(notEmpty, empty);
        } catch (IllegalArgumentException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    /**
     * Test to see if addNewContact throws nullPointerException when passing null String parameters
     */
    public void throwNullPointerExceptionWhenPassingNullStrings(){
        boolean exceptionThrown = false;
        String nullString = null;
        String notNullString = "not null";
        try{
            contactManager.addNewContact(nullString, notNullString);
        } catch (NullPointerException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    /**
     * Tests addNewContact successfully adds a contact to Set<Contact> allKnownContacts
     */
    public void containsNewlyCreatedContact() {
        contactManager.addNewContact("Darth Vader", "Use the Force");
        Set<Contact> result = contactManager.getContacts("");
        boolean hasName = true;
        if (result.stream()
                    .filter(contact -> contact.getName().equals("Darth Vader"))
                    .findFirst()
            .equals(null) ) {
                hasName = false;
            }
        assertTrue(hasName);
    }

}