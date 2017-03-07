package java.ContactManagerImplTests;

import main.java.spec.Contact;
import main.java.spec.ContactManager;
import main.java.impl.ContactManagerImpl;
import main.java.impl.MeetingImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
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


    @Before
    public void setUp() throws Exception {
        myContactManager = new ContactManagerImpl();
        for (int i = 0; i < 1000; i++) {
            myContactManager.addNewContact("Name " + i, "Notes " + i);
        }
        futureDate = new GregorianCalendar(2112, 12, 06);
        pastDate = new GregorianCalendar(1969, 11, 12);
        uniqueId1 = myContactManager.addNewContact("Unique1", "notes1");
        uniqueId2 = myContactManager.addNewContact("Unique2", "notes2");
        contactSet = myContactManager.getContacts(uniqueId1, uniqueId2);
    }

    @After
    public void cleanUp() {
        File file = new File("ContactManagerFile.txt");
        file.delete();
    }

    @Test
    public void returnAContactSetOfSameSizeWhenCreatingNewContactManagerImplFromOldContactManagerFlush() {
        int expectedSizeOfAllContacts = myContactManager.getContacts("").size();
        myContactManager.flush();
        ContactManager contactManager2 = new ContactManagerImpl();
        assertEquals(expectedSizeOfAllContacts, contactManager2.getContacts("").size());
    }

    @Test
    public void returnSameUniqueContactsFromGetFutureFutureMeetingListAfterFlushing() {
        String expectedString = uniqueId1 + "" + uniqueId2;
        int futureMeetingId = myContactManager.addFutureMeeting(contactSet, futureDate);
        myContactManager.flush();
        contactManager2 = new ContactManagerImpl();
        Set<Contact> resultContacts = contactManager2.getFutureMeeting(futureMeetingId).getContacts();
        List<Contact> resultsArrayList = new ArrayList<>();
        resultsArrayList = resultContacts.stream()
                                       .sorted(Comparator.comparingInt(Contact::getId))
                                       .collect(Collectors.toList()); // need to guarantee order
        String resultString = "";
        for (Contact c : resultsArrayList) {
            resultString += c.getId();
        }
        assertEquals(expectedString, resultString);
    }

    @Test
    public void returnSameUniqueContactsIDFromGetPastMeetingListAfterFlushing() {
        String expectedString = uniqueId1 + "" + uniqueId2;
        int pastMeetingId = myContactManager.addNewPastMeeting(contactSet, pastDate, "some notes");
        myContactManager.flush();
        contactManager2 = new ContactManagerImpl();
        Set<Contact> resultContacts = contactManager2.getPastMeeting(pastMeetingId).getContacts();
        List<Contact> resultsArrayList = new ArrayList<>();
        resultsArrayList = resultContacts.stream()
                                       .sorted(Comparator.comparingInt(Contact::getId))
                                       .collect(Collectors.toList()); // need to guarantee order
        String resultString = "";
        for (Contact c : resultsArrayList) {
            resultString += c.getId();
        }
        assertEquals(expectedString, resultString);
    }

    @Test
    public void returnNullWhenCallingGetFutureMeetingOfAnExpiredFutureMeetingAfterFlush() {
        Calendar currentTimePlus5sec = Calendar.getInstance();
        currentTimePlus5sec.add(Calendar.SECOND,  5);
        int futureMeetId = myContactManager.addFutureMeeting(contactSet, currentTimePlus5sec);
        try {
            for (int i = 0; i < 5; i++) {
                System.out.println("Sleeping..." + (5 - (i)));
                Thread.sleep(1001);
            }
        } catch (InterruptedException ex) {
            System.out.println("Was interrupted!");
        }
        myContactManager.flush();
        assertNull(myContactManager.getFutureMeeting(futureMeetId));
    }

    @Test
    public void storeLastMeetingIdAndReturnItWhenCallingNewConstructor(){
        int expectedMeetingId = MeetingImpl.getAllMeetingIdCounter() + 1;
        myContactManager.flush();
        contactManager2 = new ContactManagerImpl();
        for( int i = 0; i < 10 ; i++) {
            contactManager2.addNewContact("Dude#" + i, "notes" + i);
        }
        Set<Contact> allCM2Contacts = contactManager2.getContacts("");
        int resultId = contactManager2.addFutureMeeting(allCM2Contacts, futureDate);
        assertEquals(expectedMeetingId, resultId);
    }

}