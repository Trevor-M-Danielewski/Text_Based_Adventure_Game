// IMPORT STATEMENT
import java.util.*;

public class Character {

    // Data Fields

    String name; // Holds the name of the character
    String description; // Contains the description of the character
    int health; // Represents the health points of the character
    List<Item> inventory = new ArrayList<>(); // List that stores the items owned by the character

    // Constructors

    /**
     * Constructor to initialize the Character object.
     * @param name        The name of the character
     * @param description The description of the character
     * @param health      The health points of the character
     */
    public Character(String name, String description, int health) {
        this.name = name; // Initialize name
        this.description = description; // Initialize description
        this.health = health; // Initialize health
    }


    // Method Behaviors

    /**
     * Adds an item to the character's inventory.
     * @param item The item to be added
     */
    public void addItem(Item item){
        inventory.add(item); // Adding the provided item to the character's inventory
    }

    /**
     * Removes an item from the character's inventory.
     * @param item The item to be removed
     */
    public void removeItem(Item item){
        inventory.remove(item); // Removing the provided item from the character's inventory
    }

    /**
     * Changes the character's health by the given amount.
     * @param change The amount by which to change the health
     */
    public void changeHealth(int change){
        this.health += change; // Modifying the character's health by the specified amount
    }

    /**
     * Retrieves the character's inventory as a string.
     * @return A string representation of the character's inventory
     */
    public String getInventory(){
        String result = name + " is carrying "; // Initializing the string with the character's name and inventory description

        for(int i = 0; i < inventory.size(); i++) {
            result += inventory.get(i); // Appending each item in the inventory to the result string
            if (i < inventory.size() - 1) {
                result += ", "; // Adding a comma if it's not the last item
            }
        }

        return result; // Returning the string representation of the character's inventory
    }
}