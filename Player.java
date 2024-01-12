import java.util.*;

public class Player {

    // Data Fields

    // A list to store items in the player's inventory
    List<Item> inventory = new ArrayList<>();

    // A list to store characters that are following the player
    List<Character> charactersWith = new ArrayList<>();

    // The player's health value, initialized to 10
    int health = 10;

    // Constructor
    public Player() {
        // Constructor intentionally left empty as no additional initialization is required
    }

    // Method Behaviors

    /**
     * Method to add an item to the player's inventory.
     * @param item The item to be added to the player's inventory
     */
    public void addItem(Item item) {
        inventory.add(item); // Adds the specified item to the player's inventory
    }

    /**
     * Method to remove an item from the player's inventory.
     * @param item The item to be removed from the player's inventory
     */
    public void removeItem(Item item) {
        inventory.remove(item); // Removes the specified item from the player's inventory
    }

    /**
     * Method to change the player's health by a specified value.
     * @param changeValue The value by which to change the player's health
     */
    public void changeHealth(int changeValue) {
        this.health += changeValue; // Changes the player's health by the specified value
    }
}
