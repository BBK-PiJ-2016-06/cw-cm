import java.util.Calendar;
import java.util.Set;

/**
 * Created by nathanhanak on 12/29/16.
 */
public class PastMeetingImpl extends MeetingImpl implements Meeting, PastMeeting  {

    String notes = "";

    public PastMeetingImpl(Set<Contact> contacts, Calendar date, String notes) {
        super(contacts, date);
        this.notes = notes;
    }

    /**
     * Returns the notes from the meeting.
     *
     * If there are no notes, the empty string is returned.
     *
     * @return the notes from the meeting.
     */
    @Override
    public String getNotes() {
        return notes;
    }

}
