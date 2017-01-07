import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;
import java.util.stream.Collectors;


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
    private Contact knownFutureContact;
    private Contact contactNotInFutureMeetings;
    private Set<Contact> evenContacts;
    private Set<Contact> oddContacts;
    private List<Integer> intListForFutureMeetingList;
    private List<Integer> intListForPastMeetingList;

    @Before
    public void setUp() throws Exception {
        contactManager = new ContactManagerImpl();
        contactManager.addNewContact("Luke Skywalker", "Nooooo!");
        contactManager.addNewContact("Darth Vader", "I am your father");
        futureDate = new GregorianCalendar(2050, 06, 06);
        knownFutureContact = contactManager.getContacts("")
                .stream()
                .filter(m -> m.getName().equals("Darth Vader"))
                .findFirst()
                .get();
        contactNotInFutureMeetings = contactManager.getContacts("")
                                                    .stream()
                                                    .filter(m -> m.getName().equals("Luke Skywalker"))
                                                    .findFirst()
                                                    .get();
        for ( int i = 0; i < 100; i++ ) {
            contactManager.addNewContact(("Name" + i), ("note" + i) );
        }
        dateInPast = new GregorianCalendar(1969, 06, 01);
        pastMeeting = new PastMeetingImpl(contactSet1, dateInPast, "Meeting occurred in past");
        evenContacts = contactManager.getContacts("").stream()
                                        .filter(c -> (c.getId() % 2 == 0) )
                                        .collect(Collectors.toSet());
        oddContacts = contactManager.getContacts("")
                                    .stream()
                                    .filter(c -> (c.getId() % 2 != 0) )
                                    .collect(Collectors.toSet());
        intListForFutureMeetingList = new ArrayList<>();
        intListForPastMeetingList = new ArrayList<>();
        for(int j = 01; j<13; j++) {
            futureDate = new GregorianCalendar( 2112, j, j);
            intListForFutureMeetingList.add(contactManager.addFutureMeeting(evenContacts, futureDate));
        }
        for(int j = 01; j<13; j++) {
            dateInPast = new GregorianCalendar( 1969, j, j);
            intListForPastMeetingList.add
                    (contactManager.addNewPastMeeting(oddContacts, dateInPast, "past meeting" + j));
        }
        contactSet1 = contactManager.getContacts("");
    }

    @Test
    // tests addFutureMeeting(Set<Contact> , Calendar)
    public void returnExpectedIntWhenCallingAddFutureMeeting() {
        FutureMeeting randomMeeting = new FutureMeetingImpl(contactSet1, futureDate);
        int expected = randomMeeting.getId() + 1;
        assertEquals(expected, contactManager.addFutureMeeting(contactSet1, futureDate));
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

    @Test
    /**
     * tests addNewPastMeeting to see if it throws IllegalArgumentException
     * when a contact in @param Set<Contact> isn't known
     */
    public void throwIllegalArgumentExceptionWhenPassingUnknownContactToAddNewPastMeeting(){
        boolean exceptionThrown = false;
        Set<Contact> contactSetWithUnknownContact = new HashSet<>();
        Contact unknownContact = new ContactImpl("Stranger");
        contactSetWithUnknownContact.add(unknownContact);
        try {
            contactManager.addNewPastMeeting(contactSetWithUnknownContact, dateInPast, "past notes");
        } catch (IllegalArgumentException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    // test to make sure addNewPastMeeting DOESN'T throw an exception when all contacts are known.
    public void notThrowExceptionWhenCallingAddNewPastMeetingAndAllContactsAreKnown() {
        boolean exceptionThrown = false;
        Set<Contact> knownContacts = contactManager.getContacts("");
        try {
            contactManager.addNewPastMeeting(knownContacts, dateInPast, "past notes");
        } catch (IllegalArgumentException ex) {
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    // test for addNewPastMeeting(Set<Contact> , Calendar, String notes)
    public void throwIllegalArgExceptionWhenPassingFutureDateToAddNewPastMeeting(){
        boolean exceptionThrown = false;
        try {
            contactManager.addNewPastMeeting(contactSet1, futureDate, "Some notes");
        } catch (IllegalArgumentException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    // test for addNewPastMeeting(Set<Contact> , Calendar, String notes)
    public void throwIllegalArgExceptionWhenPassingEmptyContactSetToAddNewPastMeeting(){
        boolean exceptionThrown = false;
        Set<Contact> emptyContact = new HashSet<>();
        try {
            contactManager.addNewPastMeeting(emptyContact, futureDate, "Some notes");
        } catch (IllegalArgumentException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    // test for addNewPastMeeting(Set<Contact> , Calendar, String notes)
    public void throwNullPointerExceptionIfAnyParamsForAddNewPastMeetingAreNull(){
        boolean nullPointerThrown = false;
        String someNotes = "here are some notes";
        try{
            Set<Contact> nullContactSet = null;
            contactManager.addNewPastMeeting( nullContactSet, dateInPast, someNotes);
        } catch (NullPointerException ex) {
            nullPointerThrown = true;
        }
        assertTrue(nullPointerThrown);
        nullPointerThrown = false;
        try{
            String nullNotes = null;
            contactManager.addNewPastMeeting(contactSet1, dateInPast, nullNotes);
        } catch (NullPointerException ex) {
            nullPointerThrown = true;
        }
        assertTrue(nullPointerThrown);
        nullPointerThrown = false;
        try{
            Calendar nullCalendar = null;
            contactManager.addNewPastMeeting( contactSet1, nullCalendar, someNotes);
        } catch (NullPointerException ex) {
            nullPointerThrown = true;
        }
        assertTrue(nullPointerThrown);
    }

    @Test
    /**
     * Test for getPastMeeting(int);
     * addNewPastMeeting returns an int of the newly created meeting, is expected
     * Makes sure the resultMeeting @return the Id int that is expected
     */
    public void returnAPastMeetingByItsIdNumber() {
        int expected = contactManager.addNewPastMeeting(contactSet1, dateInPast, "past meeting");
        Meeting resultMeeting = contactManager.getPastMeeting(expected);
        int result = resultMeeting.getId();
        assertEquals(expected, result);
    }

    @Test
    /**
     * Test for getPastMeeting(int);
     */
    public void returnNullMeetingIfGetPastMeetingCantFindId() {
        int expected = 10 + contactManager.addNewPastMeeting(contactSet1, dateInPast, "past meeting");
        // creates an int 10 larger than the highest possible # of Meetings
        Meeting resultMeeting = contactManager.getPastMeeting(expected);
        assertNull(resultMeeting);
    }

    @Test
    /**
     * Test for getPastMeeting(int);
     * @throws IllegalStateException if there is a meeting with that ID happening
     *         in the future
     */
    public void throwIllegalStateExceptionIfIdBelongsToFutureMeeting() {
        int idOfFutureMeeting = contactManager.addFutureMeeting(contactSet1, futureDate);
        boolean exceptionThrown = false;
        try {
            Meeting resultMeeting = contactManager.getPastMeeting(idOfFutureMeeting);
        } catch (IllegalStateException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    /**
     * Test for getFutureMeeting(int);
     * addFutureMeeting returns an int of the newly created meeting, is expected
     * Makes sure the resultMeeting @return the Id int that is expected
     */
    public void returnAFutureMeetingByItsIdNumber() {
        int expected = contactManager.addFutureMeeting(contactSet1, futureDate);
        Meeting resultMeeting = contactManager.getFutureMeeting(expected);
        int result = resultMeeting.getId();
        assertEquals(expected, result);
    }

    @Test
    /**
     * Test for getFutureMeeting(int);
     */
    public void returnNullMeetingIfGetFutureMeetingCantFindId() {
        int expected = 10 + contactManager.addFutureMeeting(contactSet1, futureDate);
        // creates an int 10 larger than the highest possible # of Meetings
        Meeting resultMeeting = contactManager.getFutureMeeting(expected);
        assertNull(resultMeeting);
    }

    @Test
    /**
     * Test for getFutureMeeting(int);
     * @throws IllegalStateException if there is a meeting with that ID happening
     *         in the past
     */
    public void throwIllegalStateExceptionIfIdBelongsToPastMeeting() {
        int idOfPastMeeting = contactManager.addNewPastMeeting(contactSet1, dateInPast, "past meeting");
        boolean exceptionThrown = false;
        try {
            Meeting resultMeeting = contactManager.getFutureMeeting(idOfPastMeeting);
        } catch (IllegalStateException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    /**
     * Test for getMeeting(int);
     * addFutureMeeting returns an int of the newly created meeting, is expected
     * Makes sure the resultMeeting @return the Id int that is expected
     */
    public void returnAMeetingByItsIdNumber() {
        int expected = contactManager.addFutureMeeting(contactSet1, futureDate);
        Meeting resultMeeting = contactManager.getMeeting(expected);
        int result = resultMeeting.getId();
        assertEquals(expected, result);
        // recreate for  pastMeeting as well
        expected = contactManager.addNewPastMeeting(contactSet1, dateInPast, "past meeting");
        resultMeeting = contactManager.getPastMeeting(expected);
        result = resultMeeting.getId();
        assertEquals(expected, result);
    }

    @Test
    /**
     * Test for getMeeting(int);
     */
    public void returnNullMeetingIfGetMeetingCantFindId() {
        int expected = 10 + contactManager.addFutureMeeting(contactSet1, futureDate);
        // creates an int 10 larger than the highest possible # of Meetings
        Meeting resultMeeting = contactManager.getMeeting(expected);
        assertNull(resultMeeting);
        // recreate for  pastMeeting as well
        expected = 10 + contactManager.addNewPastMeeting(contactSet1, dateInPast, "past meeting");
        // creates an int 10 larger than the highest possible # of Meetings
        resultMeeting = contactManager.getPastMeeting(expected);
        assertNull(resultMeeting);
    }

    @Test
    /**
     * Test for getFutureMeetingList(Contact);
     */
    public void returnAListOfFutureMeetingsContainingKnownContactWhenCallingGetFutureMeetingList() {
        List<Meeting> resultMeetingList = contactManager.getFutureMeetingList(knownFutureContact);
        boolean allMeetingsContainKnownContact = true;

        for ( Meeting m : resultMeetingList) {
            if ( !m.getContacts().contains(knownFutureContact) ) {
                allMeetingsContainKnownContact = false;
            }
        }
        assertTrue(allMeetingsContainKnownContact);
    }

    @Test
    /**
     * Test for getFutureMeetingList(Contact);
     */
    public void returnsAnEmptyListIfContactIsNotContainedInAnyFutureMeetings() {
        List<Meeting> emptyList = contactManager.getFutureMeetingList(contactNotInFutureMeetings);
        assertTrue(emptyList.isEmpty());
    }

    @Test
    /**
     * Test for getFutureMeetingList(Contact);
     */
    public void throwNullPointerExceptionWhenPassingANullContactThroughGetFutureMeetingList() {
        Contact nullContact = null;
        boolean exceptionThrown = false;
        try {
            List<Meeting> shouldNotGenerate = contactManager.getFutureMeetingList(nullContact);
        } catch (NullPointerException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    /**
     * Test for getFutureMeetingList(Contact);
     */
    public void throwIllegalArgumentExceptionWhenCallingGetFutureMeetingWithUnknownContact() {
        Contact unknownContact = new ContactImpl("Name");
        boolean exceptionThrown = false;
        try{
            contactManager.getFutureMeetingList(unknownContact);
        } catch (IllegalArgumentException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    /**
     * Test for getFutureMeetingList(Contact);
     * adds a new Meeting that is chronologically before any meetings added in @Before
     * retrieves the Id of that meeting
     * calls getFutureMeetingList and builds a result List<Meeting>
     * iterate over every Meeting in List, calling getId and adding to new List<Integer>
     * First element should match the Id of earliestMeetingId
     */
    public void returnAListThatIsChronologicallySortedWhenCallingGetFutureMeeting() {
        Calendar earlierDate = new GregorianCalendar(2080, 06, 12);
        int earliestMeetingId = contactManager.addFutureMeeting(evenContacts, earlierDate);

        //adds a meeting w/higher ID but earlier date to existing futureMeetingList

        List<Meeting> resultMeetingList = contactManager.getFutureMeetingList(knownFutureContact);
        List<Integer> resultIntList = new ArrayList<>();
        for (Meeting m : resultMeetingList) {
            resultIntList.add(m.getId());
        }
        int result = resultIntList.get(0);
        assertEquals(earliestMeetingId, result);

    }





    @Test
    /**
     * Test for getPastMeetingListFor(Contact);
     */
    public void returnAListOfPastMeetingsContainingKnownContactWhenCallingGetPastMeetingList() {
        List<PastMeeting> resultMeetingList = contactManager.getPastMeetingListFor(contactNotInFutureMeetings);
        boolean allMeetingsContainContactNotInFutureMeetings = true;

        for ( Meeting m : resultMeetingList) {
            if ( !m.getContacts().contains(contactNotInFutureMeetings) ) {
                allMeetingsContainContactNotInFutureMeetings = false;
            }
        }
        assertTrue(allMeetingsContainContactNotInFutureMeetings);
    }

    @Test
    /**
     * Test for getPastMeetingListFor(Contact);
     */
    public void returnsAnEmptyListIfContactIsNotContainedInAnyPastMeetings() {
        contactManager.addNewContact("Boba Fett", "Not in past meetings");
        Contact contactNotInPastMeetings =    contactManager.getContacts("")
                                                            .stream()
                                                            .filter(m -> m.getName().equals("Boba Fett"))
                                                            .findFirst()
                                                            .get();
        List<PastMeeting> emptyList = contactManager.getPastMeetingListFor(contactNotInPastMeetings);
        assertTrue(emptyList.isEmpty());
    }

    @Test
    /**
     * Test for getPastMeetingListFor(Contact);
     */
    public void throwNullPointerExceptionWhenPassingANullContactToGetPastMeetingFor() {
        Contact nullContact = null;
        boolean exceptionThrown = false;
        try {
            List<PastMeeting> shouldNotGenerate = contactManager.getPastMeetingListFor(nullContact);
        } catch (NullPointerException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    /**
     * Test for getPastMeetingListFor(Contact);
     */
    public void throwIllegalArgumentExceptionWhenCallingGetPastMeetingForWithUnknownContact() {
        Contact unknownContact = new ContactImpl("Name");
        boolean exceptionThrown = false;
        try{
            contactManager.getPastMeetingListFor(unknownContact);
        } catch (IllegalArgumentException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    /**
     * Test for getPastMeetingListFor(Contact);
     * adds a new Meeting that is chronologically before any meetings added in @Before
     * retrieves the Id of that meeting
     * calls getPastMeetingListFor(Contact) and builds a result List<PastMeeting>
     * iterate over every Meeting in List, calling getId and adding to new List<Integer>
     * First element should match the Id of earliestMeetingId
     */
    public void returnAListThatIsChronologicallySortedWhenCallingGetPastMeetingListFor() {
        Calendar earlierDate = new GregorianCalendar(1960, 06, 12);
        int earliestMeetingId = contactManager.addNewPastMeeting(oddContacts, earlierDate, "oldest meeting");

        //adds a meeting w/higher ID but earlier date to existing pastMeetingList

        List<PastMeeting> resultMeetingList = contactManager.getPastMeetingListFor(contactNotInFutureMeetings);
        List<Integer> resultIntList = new ArrayList<>();
        for (Meeting m : resultMeetingList) {
            resultIntList.add(m.getId());
        }
        int result = resultIntList.get(0);
        assertEquals(earliestMeetingId, result);
    }

    @Test
    // test for getContacts(String)
    public void returnASetOfContactsContainingOnlyTheDesiredName() {
        boolean containsNonDesiredName = false;
        for (int i = 0; i < 10; i++) {
            contactManager.addNewContact("Stormtrooper", "Clone:" + i);
        }
        Set<Contact> resultSet = contactManager.getContacts("Stormtrooper");
        for ( Contact c : resultSet) {
            if ( !c.getName().equals("Stormtrooper")) {
                containsNonDesiredName = true;
            }
        }
        assertFalse(containsNonDesiredName);
    }

    @Test
    // test for getContacts(String);
    public void throwNullPointerExceptionWhenPassingNullStringThroughGetContacts() {
        String nullString = null;
        boolean exceptionThrown = false;
        try {
            contactManager.getContacts(nullString);
        } catch (NullPointerException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    //test for getContacts(String);
    public void returnAnEmptySetWhenCallingGetContactsForAnUnknownContact() {
        Set<Contact> resultSet = contactManager.getContacts("Doesn't exist");
        assertTrue(resultSet.isEmpty());
    }

}