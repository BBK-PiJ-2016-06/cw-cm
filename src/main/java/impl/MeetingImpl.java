package impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

import spec.Contact;
import spec.Meeting;

/**
 * An implementation of the Meeting interface. Contains all objects needed
 * to store meeting information. Contains no indication of time in name.
 * Created by nathanhanak on 12/27/16.
 */
public class MeetingImpl implements Meeting, Serializable {

  private static int allMeetingIdCounter = 0;
  private int meetingId;
  private Calendar date;
  private Set<Contact> participants;

  /**
   * Constructor for MeetingImpl.
   *
   * @param contacts : a Set of type Contact for this meeting.
   * @param newDate : a Calendar indicating the scheduled date of the meeting.
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
   * @param newId the Id of the last Meeting created before last flush
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
