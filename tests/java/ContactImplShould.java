package java;

import main.java.impl.ContactImpl;
import main.java.spec.Contact;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Testing class for main.java.impl.ContactImpl
 */


public class ContactImplShould {

    private Contact contact1;
    private Contact contact2;

    @Before
    public void ContactImplSetup(){
        contact1 = new ContactImpl("Steve");
        contact2 = new ContactImpl("Mariah");
    }

    @Test
    public void returnIdOneHigherThanIDCounterAfterCreatingNewContact(){
        int expected = ContactImpl.getAllContactIdCounter() + 1;
        Contact newContact = new ContactImpl("Han");
        int result = newContact.getId();
        assertEquals(expected, result);
    }


    @Test
    public void returnSteveAndMariahWhenCallingGetName() throws Exception {
        String result =  contact1.getName();
        assertEquals("Steve", result);
        result = contact2.getName();
        assertEquals("Mariah", result);

    }

    @Test
    public void returnBlankWhenCallingGetNotes() throws Exception {
        String result = contact1.getNotes();
        assertEquals("", result);
    }

    @Test
    public void returnMessageWhenCallingGetNotesAfterAddingNotes() throws Exception {
        contact1.addNotes("Message");
        assertEquals("Message" + "\n", contact1.getNotes());
    }

    @Test
    // tests to make sure I can alter the static ID counter
    // any new main.java.spec.Contact created after should be
    public void createAContactWithAnID1HigherThanParamOfSetMax() {
        ContactImpl.setAllContactIdCounter(555);
        Contact newContact = new ContactImpl("Leia");
        assertEquals(556, newContact.getId());
        ContactImpl.setAllContactIdCounter(0);
        newContact = new ContactImpl("Chewie");
        assertEquals(1, newContact.getId());
    }


}