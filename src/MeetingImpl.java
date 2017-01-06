import java.util.Calendar;
import java.util.Set;

/**
 * Created by nathanhanak on 12/27/16.
 *
 * A class to represent meetings
 *
 * Meetings have unique IDs, scheduled date and a list of participating contacts
 */

public class MeetingImpl implements Meeting {

    private static int allMeetingIdCounter = 0;
    private int meetingId;
    private Calendar date;
    private Set<Contact> participants;

    /**
     * Constructor for MeetingImpl
     *
     * @param contacts : a set of type Contact for this meeting
     * @param date : a calendar indicating the scheduled date of the meeting
     */
    public MeetingImpl(Set<Contact> contacts, Calendar date) {
        allMeetingIdCounter++;
        meetingId = allMeetingIdCounter;
        participants = contacts;
        this.date = date;
    }

    @Override
    public int getId() {
        return meetingId;
    }

    @Override
    public Calendar getDate() {
        return date;
    }

    @Override
    public Set<Contact> getContacts() {
        return participants;
    }

    /**
     * Setter for date
     *
     * parameter date to replace previous value for this meeting
     *
     * @param newDate is a Calendar
     */
    public void rescheduleMeeting(Calendar newDate){
        date = newDate;
    }

}
