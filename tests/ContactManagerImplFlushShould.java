import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by nathanhanak on 1/9/17.
 */
public class ContactManagerImplFlushShould {

    private ContactManager myContactManager = new ContactManagerImpl();


    @Before
    public void setUp() throws Exception {
        for (int i = 0; i < 1000; i++) {
            myContactManager.addNewContact("Name + 1", "Notes " + i);
        }
    }

    @Test
    public void returnAContactListOfSameSizeWhenCreatingNewContactImplFromOldContactManagerFlush() {
        int expectedSizeOfAllContacts = myContactManager.getContacts("").size();
        myContactManager.flush();
        ContactManager contactManager2 = new ContactManagerImpl();
        assertEquals(expectedSizeOfAllContacts, contactManager2.getContacts("").size());
    }

}