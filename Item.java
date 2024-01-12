public class Item {

    // Fields to store information about an Item
    public String name;         // Name of the item
    public String description;  // Description of the item
    int damage;                 // Damage caused by the item

    /**
     * Constructor to initialize an Item object with specified attributes.
     * @param name        The name of the item.
     * @param description The description of the item.
     * @param damage      The damage caused by the item.
     */
    public Item(String name, String description, int damage) {
        this.name = name;           // Initialize the name of the item
        this.description = description; // Initialize the description of the item
        this.damage = damage;       // Initialize the damage caused by the item
    }
}
