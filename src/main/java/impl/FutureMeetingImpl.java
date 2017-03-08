package impl;

import spec.Contact;
import spec.FutureMeeting;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

/**
 * Created by nathanhanak on 1/5/17.
 */
public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting, Serializable {

    public FutureMeetingImpl(Set<Contact> contacts, Calendar date){
        super(contacts, date);
    }

}
