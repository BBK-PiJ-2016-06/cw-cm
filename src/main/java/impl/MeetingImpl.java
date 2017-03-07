package main.java.impl;

import main.java.spec.Contact;
import main.java.spec.Meeting;

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
     * Constructor for main.java.impl.MeetingImpl
     *
     * @param contacts : a set of type main.java.spec.Contact for this meeting
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

    public static void setAllMeetingIdCounter(int newId) {
        allMeetingIdCounter = newId;
    }

    public static int getAllMeetingIdCounter() {
        return allMeetingIdCounter;
    }

}
