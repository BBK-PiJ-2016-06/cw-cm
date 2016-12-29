import java.util.Calendar;
import java.util.Set;

/**
 * Created by nathanhanak on 12/27/16.
 *
 * A class to represent meetings
 *
 * Meetings have unique IDs, scheduled date and a list of participating contacts
 */

public class MeetingImpl implements Meeting, FutureMeeting, PastMeeting {

    private static int allMeetingIdCounter = 0;
    private int meetingId;
    private Calendar date;
    private Set<Contact> participants;
    String notes = "";

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

    /**
     * Returns the id of the meeting.
     *
     * @return the id of the meeting.
     */
    public int getId() {
        return meetingId;
    }


    /**
     * Return the date of the meeting.
     *
     * @return the date of the meeting.
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * Setter for date
     *
     * parameter date to replace previous value for this meeting
     *
     * @param Calendar newDate
     */
    public void rescheduleMeeting(Calendar newDate){
        date = newDate;
    }

    /**
     * Return the details of people that attended the meeting.
     *
     * The list contains a minimum of one contact (if there were
     * just two people: the user and the contact) and may contain an
     * arbitrary number of them.
     *
     * @return the details of people that attended the meeting.
     */
    public Set<Contact> getContacts() {
        return participants;
    }

    /**
     * Returns the notes from the meeting.
     *
     * If there are no notes, the empty string is returned.
     *
     * @return the notes from the meeting.
     */
    public String getNotes() {

    };

}
