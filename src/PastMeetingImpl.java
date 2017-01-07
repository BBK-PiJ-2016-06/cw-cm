import java.util.Calendar;
import java.util.Set;

/**
 * Created by nathanhanak on 12/29/16.
 */
public class PastMeetingImpl extends MeetingImpl implements PastMeeting  {

    public String notes = "";

    public PastMeetingImpl(Set<Contact> contacts, Calendar date, String notes) {
        super(contacts, date);
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void addNotes(String newText) {
        notes += newText;
    }

}
