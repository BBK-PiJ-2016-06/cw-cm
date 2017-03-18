package impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

import spec.Contact;
import spec.PastMeeting;

/**
 * A class to represent a Meeting which took place in the past.
 * Created by nathanhanak on 12/29/16.
 */
public class PastMeetingImpl extends MeetingImpl implements PastMeeting, Serializable {

  private String notes = "";

  /**
   * Constructor for PastMeetingImpl
   * @param contacts a Set of attendees for this meeting.
   * @param date the date on which this meeting occurred.
   */
  public PastMeetingImpl(Set<Contact> contacts, Calendar date) {
    super(contacts, date);
  }

  /**
   * Returns all current notes for this particular meeting.
   * @return notes A String containing all notes.
   */
  public final String getNotes() {
    return notes;
  }

  /**
   * Adds additional notes to this meeting at any time.
   * @param newText a String of new text to be added.
   */
  public final void addNotes(String newText) {
    notes += newText;
  }

}
