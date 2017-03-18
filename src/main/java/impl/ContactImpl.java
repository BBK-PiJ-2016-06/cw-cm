package impl;

import java.io.Serializable;
import spec.Contact;


/**
 * Implementation of the Contact interface.
 */
public class ContactImpl implements Contact, Serializable {

  private static int allContactIdCounter = 0;
  private int thisContactId;
  private String name;
  private String cumulativeNotes = "";

  /**
   * Constructor for ContactImpl.
   * @param newName the name of the new contact.
   */
  public ContactImpl(String newName) {
    allContactIdCounter++;
    thisContactId = allContactIdCounter;
    this.name = newName;
  }

  /**
   * Returns the ID of the contact.
   *
   * @return the ID of the contact.
   */
  @Override
  public final int getId() {
    return thisContactId;
  }

  /**
   * Returns the name of the contact.
   *
   * @return the name of the contact.
   */
  @Override
  public final String getName() {
    return name;
  }

  /**
   * Returns our notes about the contact, if any.
   * If we have not written anything about the contact, the empty
   * string is returned.
   * @return a string with notes about the contact, maybe empty.
   */
  @Override
  public final String getNotes() {
    return cumulativeNotes;
  }

  /**
   * Add notes about the contact.
   *
   * @param note the notes to be added
   */
  @Override
  public final void addNotes(String note) {
    cumulativeNotes += note + "\n";
  }

  /**
   * getter for allContactIdCounter private field.
   * @return the current int value of allContactIdCounter
   */
  public static int getAllContactIdCounter() {
    return allContactIdCounter;
  }

  /**
   * setter for allContactIdCounter private field.
   * @param newCount the new value of allContactIdCounter
   */
  public static void setAllContactIdCounter(int newCount) {
    allContactIdCounter = newCount;
  }
}
