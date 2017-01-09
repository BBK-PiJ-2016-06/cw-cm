import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by nathanhanak on 1/9/17.
 */
public class ContactManagerImplFlushShould {

    private ContactManager myContactManager;
    private ContactManager contactManager2;
    private Calendar futureDate;


    @Before
    public void setUp() throws Exception {
        myContactManager = new ContactManagerImpl();
        for (int i = 0; i < 1000; i++) {
            myContactManager.addNewContact("Name + 1", "Notes " + i);
        }
        futureDate = new GregorianCalendar(2112, 12, 06);
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
        int uniqueId1 = myContactManager.addNewContact("Unique1", "notes1");
        int uniqueId2 = myContactManager.addNewContact("Unique2", "notes2");
        Set<Contact> contactSet = myContactManager.getContacts(uniqueId1, uniqueId2);
        String expectedString = uniqueId1 + "" + uniqueId2;
        int futureMeetingId = myContactManager.addFutureMeeting(contactSet, futureDate);
        myContactManager.flush();
        contactManager2 = new ContactManagerImpl();
        Set<Contact> resultContacts = contactManager2.getFutureMeeting(futureMeetingId).getContacts();
        String resultString = "";
        for (Contact c : resultContacts) {
            resultString += c.getId();
        }
        assertEquals(expectedString, resultString);
    }

}