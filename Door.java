import java.util.*;

public class Door {

    // Data fields
    boolean locked; // Represents if the door is locked or not
    String name; // Represents the name of the door

    /**
     * Constructor for the Door class.
     * @param name     The name of the door
     * @param locked   Indicates if the door is initially locked or not
     */
    public Door(String name, boolean locked){
        this.name = name;
        this.locked = locked;
    }

    /**
     * Method to unlock the door.
     * Sets the 'locked' attribute of the door to false, indicating the door is unlocked.
     */
    public void unlock(){
        this.locked = false; // Set the 'locked' attribute to false, unlocking the door
    }
}
