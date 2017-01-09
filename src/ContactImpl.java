/**
 * A contact is a person we are making business with or may do in the future.
 *
 * Contacts have an ID (unique, a non-zero positive integer),
 * a name (not necessarily unique), and notes that the user
 * may want to save about them.
 */
public class ContactImpl implements Contact {

    private static int allContactIdCounter = 0;
    private int thisContactID;
    private String name;
    private String cumulativeNotes = "";

    public ContactImpl(String name){
        allContactIdCounter++;
        thisContactID = allContactIdCounter;
        this.name = name;
    }

    /**
     * Returns the ID of the contact.
     *
     * @return the ID of the contact.
     */
    @Override
    public int getId(){
        return thisContactID;
    };

    /**
     * Returns the name of the contact.
     *
     * @return the name of the contact.
     */
    @Override
    public String getName(){
        return name;
    }

    /**
     * Returns our notes about the contact, if any.
     *
     * If we have not written anything about the contact, the empty
     * string is returned.
     *
     * @return a string with notes about the contact, maybe empty.
     */
    @Override
    public String getNotes(){
        return cumulativeNotes;
    }

    /**
     * Add notes about the contact.
     *
     * @param note the notes to be added
     */
    @Override
    public void addNotes(String note){
        cumulativeNotes += note + "\n";
    };

    /**
     * getter for allContactIdCounter private field
     */
    public static int getAllContactIdCounter() {
        return allContactIdCounter;
    }

    /**
     * setter for allContactIdCounter private field
     * @param newCount the new value of allContactIdCounter
     */
    public static void setAllContactIdCounter(int newCount) {
        allContactIdCounter = newCount;
    }
}
