import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Testing class for ContactImpl
 */


public class ContactImplShould {

    @Before
    public void ContactImplSetup(){
        Contact contact1 = new ContactImpl("Steve");
        Contact contact2 = new ContactImpl("Mariah");
    }

    @Test
    public void returnIdOf1ForContact1(){
        int result = contact1.getID();
        assertEquals(1, result);
    }


}