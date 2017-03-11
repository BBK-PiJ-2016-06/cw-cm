package impl;

import spec.Contact;
import spec.PastMeeting;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

/**
 * Created by nathanhanak on 12/29/16.
 */
public class PastMeetingImpl extends MeetingImpl implements PastMeeting, Serializable {

    private String notes = "";

    public PastMeetingImpl(Set<Contact> contacts, Calendar date) {
        super(contacts, date);
    }

    public final String getNotes() {
        return notes;
    }

    public final void addNotes(String newText) {
        notes += newText;
    }


}
