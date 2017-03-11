package impl;

import spec.Contact;
import spec.Meeting;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

/**
 * Created by nathanhanak on 12/27/16.
 *
 * A class to represent meetings
 *
 * Meetings have unique IDs, scheduled date and a list of participating contacts
 */
public class MeetingImpl implements Meeting, Serializable {

    private static int allMeetingIdCounter = 0;
    private int meetingId;
    private Calendar date;
    private Set<Contact> participants;

    /**
     * Constructor for MeetingImpl
     *
     * @param contacts : a set of type Contact for this meeting
     * @param newDate : a calendar indicating the scheduled date of the meeting
     */
    public MeetingImpl(Set<Contact> contacts, Calendar newDate) {
        allMeetingIdCounter++;
        meetingId = allMeetingIdCounter;
        participants = contacts;
        this.date = newDate;
    }

    @Override
    public final int getId() {
        return meetingId;
    }

    @Override
    public final Calendar getDate() {
        return date;
    }

    @Override
    public final Set<Contact> getContacts() {
        return participants;
    }

    /**
     * method used upon startup of application. newID is obtained from the saved file
     * and the counter is set here to last known value for normal program operation.
     * @param newId
     */
    public static void setAllMeetingIdCounter(int newId) {
        allMeetingIdCounter = newId;
    }

    /**
     * Method used when saving application. Saves the last known meetingID for storage.
     * @return int the value of the last ID given to a MeetingImpl object.
     */
    public static int getAllMeetingIdCounter() {
        return allMeetingIdCounter;
    }

}
