import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;


/**
 * Created by nathanhanak on 12/28/16.
 *
 * Test class for ContactManagerImpl
 */
public class ContactManagerImplShould {

    private Set<Contact> contactSet1;
    private Calendar futureDate;
    private ContactManager contactManager;
    private FutureMeeting futureMeeting;
    private Calendar dateInPast;
    private PastMeeting pastMeeting;

    @Before
    public void setUp() throws Exception {
        contactManager = new ContactManagerImpl();
        futureDate = new GregorianCalendar(2050, 06, 06);
        for ( int i = 0; i < 100; i++ ) {
            contactManager.addNewContact(("Name" + i), ("note" + i) );
        }
        dateInPast = new GregorianCalendar(1969, 06, 01);
        pastMeeting = new PastMeetingImpl(contactSet1, dateInPast, "Meeting occurred in past");
        contactManager.addNewContact("Darth Vader", "Use the Force");
        contactSet1 = contactManager.getContacts("");
    }

    @Test
    // tests addFutureMeeting(Set<Contact> , Calendar)
    public void return3WhenCallingAddFutureMeeting() {
        assertEquals(3, contactManager.addFutureMeeting(contactSet1, futureDate));
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

    @Test
    /**
     * tests for addNewContact(String, String)
     * @return an int of newly created Contact
     * Expected result is 507 when running all tests because 101
     * contacts are created in each @Before
     */
    public void returnAnIntWhenCallingAddNewContact() {
        Contact placeHolderContact = new ContactImpl("a name");
        int expected = placeHolderContact.getId() + 1;
        int result = contactManager.addNewContact("Boba Fett", "Is the coolest");
        assertEquals(expected , result);
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
     * Test for addNewContact
     * throws nullPointerException when passing null String parameters
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
    public void containNewlyCreatedContact() {
        contactManager.addNewContact("Princess Leia", "heroine");
        Set<Contact> result = contactManager.getContacts("");
        boolean hasName = result.stream().anyMatch(c -> c.getName().equals("Princess Leia"));
        assertTrue(hasName);
    }

    @Test
    /**
     * tests addFutureMeeting to see if it throws IllegalArgumentException
     * when a contact in @param Set<Contact> isn't known
     */
    public void throwIllegalArgumentExceptionWhenPassingUnknownContactToAddFutureMeeting(){
        boolean exceptionThrown = false;
        Set<Contact> contactSetWithUnknownContact = new HashSet<>();
        Contact unknownContact = new ContactImpl("Stranger");
        contactSetWithUnknownContact.add(unknownContact);
        try {
            contactManager.addFutureMeeting(contactSetWithUnknownContact, futureDate);
        } catch (IllegalArgumentException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    // test to make sure it DOESN'T throw an exception when all contacts are known.
    public void notThrowExceptionWhenCallingAddFutureMeetingAndAllContactsAreKnown() {
        boolean exceptionThrown = false;
        Set<Contact> knownContacts = contactManager.getContacts("");
        try {
            contactManager.addFutureMeeting(knownContacts, futureDate);
        } catch (IllegalArgumentException ex) {
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

   @Test
    // test addNewPastMeeting creates a new past meeting
    public void returnsAnIntWithExpectedIdWhenCallingAddNewPastMeeting() {
        Meeting randomMeeting = new MeetingImpl(contactSet1, dateInPast);
        int expected = randomMeeting.getId() + 1;
        int result = contactManager.addNewPastMeeting(contactSet1, dateInPast, "past meeting");
        assertEquals(expected, result);
    }

    /**
    @Test
    public void returnAPastMeetingByItsIdNumber() {
        int pastMeetingId = contactManager.addNewPastMeeting(contactSet1, dateInPast, "past meeting");
        contactManager.getPastMeeting(pastMeetingId);
        assertEquals();
    }
    */

}