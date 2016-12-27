import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
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
        result = contact2.getId();
        assertEquals(2, result);
    }

    @Test
    public void getId() throws Exception {

    }

    @Test
    public void getName() throws Exception {

    }

    @Test
    public void getNotes() throws Exception {

    }

    @Test
    public void addNotes() throws Exception {

    }


}