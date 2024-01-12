import java.util.*;

public class Location {
    // Data Fields
    String name; // Name of the location
    String description; // Description of the location

    // Lists to hold items, exits, doors, and characters in this location
    List<Item> itemsHere = new ArrayList<>(); // Items present in this location
    List<Location> exits = new ArrayList<>(); // Locations that can be accessed from this location
    List<Door> doors = new ArrayList<>();     // Doors in this location, parallel list with exits
    List<Character> charactersHere = new ArrayList<>(); // Characters present in this location

    /**
     * Constructor to initialize the Location object.
     * @param name        The name of the location
     * @param description The description of the location
     */
    public Location(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters & Setters

    // Getter to retrieve description including items and characters in this location
    public String getDesc() {
        String result = description; // Initialize the result with the location's description
        if (!charactersHere.isEmpty() || !itemsHere.isEmpty()) { // Check if characters or items are present in this location
            result += " Looking around, you see ";
        }

        // If there are items in this location, add their names to the description
        if (!itemsHere.isEmpty()) {
            result += "a ";
            for (int i = 0; i < itemsHere.size(); i++) {
                if (i == itemsHere.size() - 1 && itemsHere.size() > 1) {
                    result = result.substring(0, result.length() - 3);
                    result += " and a ";
                }
                result += itemsHere.get(i).name + ", a ";
            }
            result = result.substring(0, result.length() - 4) + ". "; // Truncate excess characters in the description
        }

        // If there are characters in this location, add their names to the description
        if (!charactersHere.isEmpty()) {
            if (!itemsHere.isEmpty()) {
                result += " You also see ";
            }
            for (int i = 0; i < charactersHere.size(); i++) {
                if (i == charactersHere.size() - 1 && charactersHere.size() > 1) {
                    result = result.substring(0, result.length() - 2);
                    result += ", and ";
                }
                result += charactersHere.get(i).name + ", ";
            }
            result = result.substring(0, result.length() - 2) + ". ";
        }

        // Add the available exits and if they are locked
        String[] exitCardinal = {"north", "east", "south", "west"};
        List<String> doorHolder = new ArrayList<>();
        result += " There is ";
        for (int i = 0; i < 4; i++) {
            if (exits.get(i) != null) {
                if (doors.get(i).locked) {
                    doorHolder.add("a locked " + doors.get(i).name + " to the " + exitCardinal[i] + ", ");
                }
                if (!doors.get(i).locked) {
                    if (doors.get(i).name.equals("hole")) {
                        doorHolder.add("a hole in the wall to the " + exitCardinal[i] + ", ");
                    } else if (doors.get(i).name.equals("path")) {
                        doorHolder.add(("a path to the " + exitCardinal[i] + " "));
                    } else {
                        doorHolder.add("an unlocked door to the " + exitCardinal[i] + ", ");
                    }
                }
            }
        }

        //adds contents of doorHolder to string result
        int i = 0;
        for (String string : doorHolder) {
            i++;
            if (doorHolder.isEmpty()) { // Execute when doorHolder list is empty
                break;
            } else if (doorHolder.size() == 1) { // Execute when doorHolder list has a size of 1
                result += string;
            } else if (i != doorHolder.size()) { // Execute when this is not the last element in doorHolder
                result += string;
            } else {
                result += "and " + string;
            }
        }
        result = result.substring(0, result.length() - 2) + ". ";

        return result;
    }

    // Method Behaviors

    /**
     * Retrieves an item by its name.
     *
     * @param objectName The name of the item to retrieve.
     * @return The item object found by its name, or null if not found.
     */
    public Item getItem(String objectName) {
        return null; // Placeholder return value
    }

    /**
     * Not yet implemented. Retrieves a character (not yet implemented).
     *
     * @return Null as the method is not yet implemented.
     */
    public Character getCharacter() {
        return null; // Placeholder return value - Not yet implemented
    }

    /**
     * Returns an exit location by its index.
     *
     * @param index The index of the exit location.
     * @return The exit location at the specified index.
     */
    public Location getExits(int index) {
        return this.exits.get(index);
    }

    /**
     * Adds an item to this location.
     *
     * @param item The item to be added to the location.
     */
    public void addItem(Item item) {
        itemsHere.add(item);
    }

    /**
     * Removes an item from this location.
     *
     * @param item The item to be removed from the location.
     */
    public void removeItem(Item item) {
        itemsHere.remove(item);
    }

    /**
     * Adds a character to this location.
     *
     * @param character The character to be added to the location.
     */
    public void addCharacter(Character character) {
        charactersHere.add(character);
    }

    /**
     * Removes a character from this location.
     *
     * @param character The character to be removed from the location.
     */
    public void removeCharacter(Character character) {
        charactersHere.remove(character);
    }

    /**
     * Adds an exit location.
     *
     * @param location The exit location to be added.
     */
    public void addExit(Location location) {
        exits.add(location);
    }

    /**
     * Adds doors to the current location.
     *
     * @param door The door to be added.
     */
    public void addDoor(Door door) {
        doors.add(door);
    }

}