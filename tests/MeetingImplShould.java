import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by nathanhanak on 12/27/16.
 *
 * Test Class to test Methods of MeetingImpl
 */
public class MeetingImplShould {

    private Meeting meeting1;
    private Meeting meeting2;

    @Before
    public void setUp() throws Exception {
        meeting1 = new MeetingImpl();
        meeting2 = new MeetingImpl();
    }

    @Test
    public void return1And2ForMeetingId() {
        assertEquals(1, meeting2.getId());
        assertEquals(2, meeting2.getId());
    }


    @Ignore
    @Test
    public void dateInputWhenCallingGetDate() {
        String result = meeting1.getDate();
        assertEquals("01/01/2017", result);
    }
}