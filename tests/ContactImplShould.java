import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Testing class for ContactImpl
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
    public void returnIdOf1ForContact1(){
        int result = contact1.getId();
        assertEquals(1, result);
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


}