package impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

import spec.Contact;
import spec.FutureMeeting;

/**
 * Created by nathanhanak on 1/5/17.
 */
public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting, Serializable {

  public FutureMeetingImpl(Set<Contact> contacts, Calendar date) {
    super(contacts, date);
  }

}
