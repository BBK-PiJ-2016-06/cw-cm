package main.java.impl;

import main.java.spec.Contact;
import main.java.spec.FutureMeeting;

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
