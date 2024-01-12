import java.io.*;
import java.util.*;
import javax.swing.*;

public class CommandSystem {

    // Represents the current state of the game
    private GameState state;

    // ArrayList to store all the verbs that the game knows about.
    // (This list is parallel to the verbDescription list)
    private List<String> verbs = new ArrayList<String>();

    // ArrayList to store descriptions corresponding to the verbs the game knows about.
    // (This list is parallel to the verbs list)
    private List<String> verbDescription = new ArrayList<String>();

    // ArrayList to store all the nouns the game knows about.
    private List<String> nouns = new ArrayList<String>();

    // Map that is used in some commands
    ImageIcon mapImage;

    // Constructor for the CommandSystem class
    // Initializes the CommandSystem object with the provided GameState
    public CommandSystem(GameState state) {
        // Assigns the provided GameState object to the 'state' attribute
        this.state = state;

        try {
            createMap();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Add verbs and descriptions
        addVerb("?", "Show this help screen."); // Command to display the help screen
        addVerb("look", "Inspect your current area or a specific person/object. Example: look book"); // Command to inspect the surroundings or an object
        addVerb("l", "Same as the look command."); // Shortened command for inspecting
        addVerb("quit", "Quit the game."); // Command to exit or quit the game
        addVerb("move", "Move in a cardinal direction. (N, S, E, W)"); // Command to move in a direction
        addVerb("take", "Pick up an item."); // Command to pick up an item
        addVerb("inventory", "Display the player's inventory and health."); // Command to view the player's inventory and health
        addVerb("i", "Same as the inventory command."); // Shortened command for inventory
        addVerb("use", "Use an item."); // Command to use an item
        addVerb("give", "Give an item to another character. (format: give [item] [character])"); // Command to give an item to a character
        addVerb("attack", "Deal damage to another character. (format: attack [character])"); // Command to attack another character


        // Add nouns related to cardinal directions
        addNoun("n"); // North
        addNoun("north");
        addNoun("s"); // South
        addNoun("south");
        addNoun("e"); // East
        addNoun("east");
        addNoun("w"); // West
        addNoun("west");

        // Add nouns related to weapons
        addNoun("fists");
        addNoun("sword");
        addNoun("mace");
        addNoun("axe");
        addNoun("broadsword");
        addNoun("pickaxe");
        addNoun("bloodytool");

        // Add nouns related to doors
        addNoun("celldoor");
        addNoun("bluedoor");
        addNoun("reddoor");
        addNoun("lockeddoor");

        // Add nouns related to keys
        addNoun("bluekey");
        addNoun("redkey");
        addNoun("greenkey");

        // Add nouns related to characters
        addNoun("tomas"); // Tomas - Character name
        addNoun("goblin"); // Goblin - Goblin character
        addNoun("guard"); // Guard - Enemy character

        // Add nouns related to consumables
        addNoun("food");
        addNoun("snack");

        // Add nouns related to tools
        addNoun("shim"); // Tool used for specific purposes
        addNoun("map");
    }


    // METHOD BEHAVIOR

    public void createMap() throws IOException {
        // Map that is used in commands
        mapImage = new ImageIcon(System.getProperty("user.dir") + "\\data\\dungeonmap.jpg");
    }

    /**
     * Controls the behavior when a command represents a Verb action.
     * @param verb The action verb extracted from the command
     */
    public void executeVerb(String verb) {
        switch (verb) {
            case "l":
            case "look":
                // Displays the description of the current room stored in the state object
                gameOutput("You look around.");
                gameOutput(state.currentLocation.getDesc());
                break;
            case "?":
                // Displays the help information for the game
                this.printHelp();
                break;
            case "i":
            case "inventory":
                // Displays the player's health and inventory items if any
                gameOutput("Health: " + state.player.health);
                if (state.player.inventory.isEmpty()) {
                    if (App.hasMap){ //if empty inventory but player has map
                        gameOutput("map");
                        break; //prevent next line from printing
                    }
                    // Notifies the player that their inventory is empty
                    gameOutput("You have nothing in your inventory.");
                } else {
                    // Lists the items in the player's inventory
                    gameOutput("Inventory:");
                    if (App.hasMap){ //if player has map
                        gameOutput("map");
                    }
                    for (Item item : state.player.inventory) {
                        gameOutput(item.name);
                    }
                }
                break;
        }
    }

    /**
     * Controls the execution result when a command consists of a verb followed by a noun.
     * Handles specific actions based on the provided verb and noun.
     *
     * @param verb The action verb extracted from the command
     * @param noun The object noun following the verb
     */
    public void executeVerbNoun(String verb, String noun) {

        int i = 0;
        int k = 0;

        // Initialize an empty string to store the result
        String resultString = "";

        // Switch statement to handle different verbs
        outerswitch: switch (verb) { //labelled as outerswitch incase of need to break within loop
            case "l":
            case "look":
                // Call the lookAt method based on the noun and store the result
                resultString = lookAt(noun);
                break;

            case "take" :
                i = 0;
                // Add the item to the inventory and remove the item from the room it was in
                if (state.currentLocation.itemsHere.isEmpty()){
                    gameOutput("there is no " + noun + " here.");
                }
                for (Item item : state.currentLocation.itemsHere) {
                    i++;
                    if (item.name.equalsIgnoreCase(noun)) {
                        state.player.addItem(item);
                        gameOutput("You took the " + noun + "!");
                        state.currentLocation.removeItem(item);
                        break;
                    } else if (i == state.currentLocation.itemsHere.size()){
                        gameOutput("There is no " + noun + " here.");
                    }
                }
                break;

            case "move":
                int index = -1;
                String direction = "";
                List<Character> characterHolder = new ArrayList<>(); //list to hold characters you are moving with

                // Switch statement to handle movement in different directions based on the noun
                switch (noun) {
                    case "north":
                    case "n": // Move north (index 0)
                        index = 0;
                        direction = "north";
                        break;

                    case "east":
                    case "e": // Move east (index 1)
                        index = 1;
                        direction = "east";
                        break;

                    case "south":
                    case "s": // Player chooses to move south (index 2)
                        index = 2;
                        direction = "south";
                        break;

                    case "west":
                    case "w": // Move west (index 3) direction
                        index = 3;
                        direction = "west";
                        break;
                }

                if (index == -1){
                    break;
                }

                for (Character character : state.currentLocation.charactersHere) { // iterates through characters in room
                    if (character.name.equals("guard")) { // if guard is found, disallows movement
                        gameOutput("You cannot leave, there is a guard here!");
                        break outerswitch;
                    }
                }
                // Check several conditions on intended exit
                if (state.currentLocation.getExits(index) == null) {
                    // Inform the player about the inability to move due to the absence of a door
                    gameOutput("You can't move here. There is no door");
                } else if (state.currentLocation.doors.get(index).locked) {
                    // Inform the player about the locked door preventing movement
                    gameOutput("You can't move here. The door is locked");
                } else {
                    // Change the current location if the exit exists and output the description
                    for (Character character : state.player.charactersWith){
                        characterHolder.add(character); //adds all in charactersWith to characterHolder
                    }
                    for (Character character : characterHolder){
                        state.currentLocation.charactersHere.remove(character); //removes all in characterHolder from currentLocation
                    }
                    state.currentLocation = state.currentLocation.getExits(index);
                    for (Character character : characterHolder){ //adds all characters in charactersHolder to new location
                        state.currentLocation.addCharacter(character);
                    }
                    // Output message for successful movement and new location description
                    gameOutput("You move " + direction + ".");
                    gameOutput(state.currentLocation.description);
                    // Resets conditions to track entry into a new room
                    App.spoken = false;
                    App.spawned = false;
                }
                break;

            case "use":
                // Handling the "use" verb
                switch (noun){
                    case "food":
                        // Check if the player's inventory is empty
                        if(state.player.inventory.isEmpty()) {
                            // Output a message indicating the absence of food in the inventory
                            gameOutput("You do not have any food. Your inventory is empty.");
                            break;
                        }

                        i = 0;
                        for (Item item : state.player.inventory) {
                            // Increment the counter 'i' for each item in the player's inventory
                            i++;

                            // Check if the current item's name matches the noun (ignoring case) and the player's health is below 20
                            if (item.name.equalsIgnoreCase(noun) && state.player.health < 20) {
                                // Increase the player's health by 5
                                state.player.changeHealth(5);

                                // Ensure the player's health doesn't exceed the maximum of 20
                                if (state.player.health > 20) {
                                    state.player.health = 20; // Set health to the maximum of 20 if exceeded
                                }

                                // Remove the consumed food item from the player's inventory
                                state.player.removeItem(item);

                                // Display a message indicating that the player ate food
                                gameOutput("You ate food.");

                                // Display the player's updated health status
                                gameOutput("Your health is now " + state.player.health + "!");

                                // Exit the loop as the action has been performed
                                break;
                            } else if (i == state.player.inventory.size()) {
                                // If no matching food item was found in the inventory, notify the player
                                gameOutput("You do not have any food");

                                
                                break;
                            }
                        }
                        break;

                    case "snack":
                        // Check if the player's inventory is empty
                        if(state.player.inventory.isEmpty()) {
                            // Output a message indicating the absence of snack in the inventory
                            gameOutput("You do not have any snacks. Your inventory is empty.");
                            break;
                        }

                        i = 0;
                        for (Item item : state.player.inventory) {
                            // Increment the counter 'i' for each item in the player's inventory
                            i++;

                            // Check if the current item's name matches the noun (ignoring case) and the player's health is below 20
                            if (item.name.equalsIgnoreCase(noun) && state.player.health < 20) {
                                // Increase the player's health by 2
                                state.player.changeHealth(2);

                                // Ensure the player's health doesn't exceed the maximum of 20
                                if (state.player.health > 20) {
                                    state.player.health = 20; // Set health to the maximum of 20 if exceeded
                                }

                                // Remove the consumed food item from the player's inventory
                                state.player.removeItem(item);

                                // Display a message indicating that the player ate food
                                gameOutput("You ate a snack.");

                                // Display the player's updated health status
                                gameOutput("Your health is now " + state.player.health + "!");

                                // Exit the loop as the action has been performed
                                break;
                            } else if (i == state.player.inventory.size()) {
                                // If no matching food item was found in the inventory, notify the player
                                gameOutput("You do not have any snacks");


                                break;
                            }
                        }
                        break;

                    case "map": //if player uses map
                        if (App.hasMap){
                            // Create a JLabel and set the ImageIcon as its icon
                            JLabel label = new JLabel(mapImage);

                            // Create a JFrame and add the JLabel to it
                            JFrame frame = new JFrame();
                            frame.getContentPane().add(label);

                            // Set default close operation and make the frame visible
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            frame.pack();
                            frame.setLocationRelativeTo(null); // Center the frame
                            frame.setVisible(true);

                            gameOutput("You look at the map.");
                        }
                        break;

                    default: // Error Handling
                        gameOutput("You cannot use " + noun + " like that.");
                        break;
                }
                break;

            case "attack":

                if (state.currentLocation.charactersHere.isEmpty()){
                    gameOutput("That person is not here.");
                    break;
                }



                //iterate through the players inventory and choose weapon with the highest damage
                Item weapon = new Item("weapon","the weapon chosen for the attack.", 0);
                for (Item item : state.player.inventory){
                    if(item.damage > weapon.damage){
                        weapon = item;
                    }
                }

                k = 0;
                // Iterate through each character in the current location
                for (Character character : state.currentLocation.charactersHere){
                    k++;
                    try {
                        // Check if the character's name matches the specified noun for attack
                        if (character.name.equalsIgnoreCase(noun)) {
                            // Reduce the character's health by the damage caused by the item
                            character.changeHealth(-weapon.damage);
                            // Output the attack message indicating the damage dealt
                            gameOutput("You attack " + noun + " with a " + weapon.name +" dealing " + weapon.damage + " damage!");
                            // Display a response from the attacked character
                            gameOutput("\"ARGH!\"");

                            // Check if the character's health dropped to or below zero after the attack
                            if (character.health <= 0) {
                                // Throw an IllegalStateException to indicate the character is no longer in a valid state
                                throw new IllegalStateException();
                            }

                            // Display the counter-attack message from the character
                            gameOutput(noun + " attacks you with a " + character.inventory.get(1).name +
                                    " dealing " + character.inventory.get(1).damage + " damage!");
                            // Reduce the player's health by the damage caused by the character's counter-attack
                            state.player.changeHealth(-character.inventory.get(1).damage);
                        } else if (k == state.currentLocation.charactersHere.size()) {
                            // If the character is not found at the current location, display a message
                            gameOutput(noun + " is not here.");
                        }
                    } catch (IllegalStateException e) {
                        // Exception caught when character's health drops to or below zero

                        // Check if the character's health is zero or below
                        if (character.health <= 0) {
                            // Display messages indicating the character is defeated
                            System.out.println("\nYou killed the " + character.name + "!");
                            System.out.println("\nA " + character.inventory.get(0).name + " was dropped!" );

                            // Add the character's inventory item to the current location
                            state.currentLocation.addItem(character.inventory.get(0));
                            // Remove the defeated character from the current location
                            state.currentLocation.removeCharacter(character);
                        }

                        // Break out of the loop after handling the defeated character
                        break;
                    }
                }
                break;
        }

        // Display the result of the command execution in the game output
        gameOutput(resultString);
    }

    /**
     * Controls the result when a command is a Verb followed by two nouns.
     * @param verb  The action verb in the command
     * @param noun1 The first noun following the verb
     * @param noun2 The second noun following the verb
     */
    public void executeVerbNounNoun(String verb, String noun1, String noun2) {
        int i = 0; // Initialize counter i
        int k = 0; // Initialize counter k

        // This method executes commands that consist of a Verb followed by two Nouns.
        switch (verb){
            case "use" :
                // Check the first noun when the command is "use"
                switch (noun1){
                    case "bluekey" :
                        i = 0; // Initialize counter i to 0
                        // Loop through the player's inventory items
                        outerloop: for(Item item : state.player.inventory) {
                            i++; // Increment counter i for each item in the inventory
                            if (state.player.inventory.isEmpty()) {   // Check if the player's inventory is empty
                                gameOutput("You do not have a bluekey."); // Display a message when the inventory is empty
                                break; 
                            } else if (item.name.equalsIgnoreCase("bluekey")) { // Check if the item is a bluekey (case insensitive)
                                switch (noun2) {
                                    case "bluedoor": // Handle the case when the second noun is a "bluedoor"
                                        if (state.currentLocation.name.equalsIgnoreCase("hallway4")) { // Check if the player is in hallway 4
                                            for (Door door : state.currentLocation.doors) {
                                                if (door.name.equalsIgnoreCase("bluedoor")) { // Check if the door is a "bluedoor"
                                                    door.unlock(); // Unlock the "bluedoor"
                                                    for (Item key : state.player.inventory) { // Iterate through the player's inventory
                                                        if (key.name.equalsIgnoreCase("bluekey")) { // Check if the item is a "bluekey"
                                                            state.player.removeItem(key); // Remove the used "bluekey" from the player's inventory
                                                            gameOutput("The door is unlocked."); // Display a message indicating successful unlocking
                                                            break outerloop;
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            gameOutput("There is no bluedoor here."); // Display a message when there is no "bluedoor" in the current location
                                        }
                                        break;
                                    case "celldoor":
                                    case "reddoor": // Handle the case when the second noun is a "reddoor"
                                        k = 0; // Initialize counter k to 0
                                        for (Door door : state.currentLocation.doors) {
                                            k++; // Increment counter k for each door iteration
                                            if (door == null) { // Check if the door is null
                                                if (k == 4) { // Execute when encountering the last null door
                                                    gameOutput("There is no " + noun2 + " here."); // Display a message for the absence of the specified door
                                                }
                                                continue; // Continue to the next iteration if the door is null
                                            } else if (door.name.equalsIgnoreCase(noun2)) { // Execute when the door exists but doesn't match the key
                                                gameOutput("The key does not unlock that door"); // Display a message for mismatched key-door combination
                                                break; 
                                            } else if (k == 4) { // Execute when it is the last element and the door hasn't been found
                                                gameOutput("There is no " + noun2 + " here."); // Display a message for the absence of the specified door
                                                break; 
                                            }
                                        }
                                        break;
                                    default: // Handle cases when a key is used on something other than a door
                                        gameOutput("You cannot use a key on that."); // Display a message indicating the key cannot be used on the specified object
                                        break;
                                }
                            } else if (i == state.player.inventory.size()) { // Execute when the bluekey is not present, but only at the last element
                                gameOutput("You do not have a bluekey."); // Display a message when the bluekey is not found in the inventory
                                break; 
                            }
                        }
                        break;
                    case "redkey" :
                        i = 0; // Initialize counter i to 0
                        outerloop: for (Item item : state.player.inventory) {
                            i++; // Increment counter i for each item in the inventory
                            if (state.player.inventory.isEmpty()) { // Check if the player's inventory is empty
                                gameOutput("You do not have a redkey."); // Display a message when the inventory is empty
                                break; 
                            } else if (item.name.equalsIgnoreCase("redkey")) { // Execute code when the "redkey" is present in the inventory
                                switch (noun2) {
                                    case "reddoor":
                                        if (state.currentLocation.name.equalsIgnoreCase("hallway2")) { // Check if the player is in hallway2
                                            for (Door door : state.currentLocation.doors) {
                                                if (door.name.equalsIgnoreCase("reddoor")) { // Check if the door is a "reddoor"
                                                    door.unlock(); // Unlock the "reddoor"
                                                    for (Item key : state.player.inventory) { // Iterate through the player's inventory
                                                        if (key.name.equalsIgnoreCase("redkey")) { // Check if the item is a "redkey"
                                                            state.player.removeItem(key); // Remove the used "redkey" from the player's inventory
                                                            gameOutput("The door is unlocked."); // Display a message indicating successful unlocking
                                                            break outerloop; 
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            gameOutput("There is no reddoor here."); // Display a message when there is no "reddoor" in the current location
                                        }
                                        break; 
                                    case "celldoor":
                                    case "bluedoor": // Handle cases when the second noun is "bluedoor"
                                        k = 0; // Initialize counter k to 0
                                        for (Door door : state.currentLocation.doors) {
                                            k++; // Increment counter k for each door iteration
                                            if (door == null) { // Check if the door is null
                                                if (k == 4) { // Execute when encountering the last null door
                                                    gameOutput("There is no " + noun2 + " here."); // Display a message for the absence of the specified door
                                                }
                                                continue; // Continue to the next iteration if the door is null
                                            } else if (door.name.equalsIgnoreCase(noun2)) { // Execute if the door exists but doesn't match the key
                                                gameOutput("The key does not unlock that door"); // Display a message for mismatched key-door combination
                                                break; 
                                            } else if (k == 4) { // Execute when it is the last element and the door hasn't been found
                                                gameOutput("There is no " + noun2 + " here."); // Display a message for the absence of the specified door
                                                break; 
                                            }
                                        }
                                        break;
                                    default: // Handle cases when the second noun is neither "bluedoor" nor "celldoor"
                                        gameOutput("You cannot use a key on that."); // Display a message indicating the key cannot be used on the specified object
                                        break;
                                }
                            } else if (i == state.player.inventory.size()) { // Execute when the redkey is not present, but only at the last element
                                gameOutput("You do not have a redkey."); // Display a message when the redkey is not found in the inventory
                                break; 
                            }
                        }
                        break;
                    case "shim" :
                        i = 0; // Initialize counter i to 0
                        if (state.player.inventory.isEmpty()) { // Check if the player's inventory is empty
                            gameOutput("You do not have a shim."); // Display a message when the inventory is empty
                            break; 
                        }
                        outerloop: for (Item item : state.player.inventory) {
                            i++; // Increment counter i for each item in the inventory
                            if (item.name.equalsIgnoreCase("shim")) { // Check if "shim" is in the inventory
                                switch (noun2) { // Start switch for the second noun when "shim" is in the inventory
                                    case "reddoor":
                                    case "bluedoor": // Handle cases when the player tries to use "shim" on doors with assigned keys
                                        k = 0; // Initialize counter k to 0
                                        for (Door door : state.currentLocation.doors) { // Loop to check if the specified door is in the room
                                            k++; // Increment counter k for each door iteration
                                            if (door == null) {
                                                if (k == 4) {
                                                    gameOutput("There is no " + noun2 + " here."); // Display a message for the absence of the specified door
                                                }
                                                continue; // Continue to the next iteration if the door is null
                                            } else if (door.name.equalsIgnoreCase(noun2)) { // Execute when the door matches the second noun
                                                if (!door.locked) { // Check if the door is already unlocked
                                                    gameOutput(noun2 + " is already unlocked."); // Display a message when the door is already unlocked
                                                    break outerloop; 
                                                }
                                                // Random chance to unlock the door using the "shim"
                                                Random random = new Random();
                                                int rand = random.nextInt(2) + 1; // Randomly generate a number (1 or 2)
                                                if (rand == 1) { // 50/50 chance
                                                    door.unlock(); // Unlock the door
                                                    gameOutput("You successfully forced the lock, but the shim broke."); // Display a success message
                                                    state.player.inventory.remove(item); // Remove the "shim" from the player's inventory as it breaks
                                                    break outerloop; 
                                                } else {
                                                    gameOutput("You fail to force the lock, and the shim breaks."); // Display a failure message
                                                    state.player.inventory.remove(item); // Remove the "shim" from the player's inventory as it breaks
                                                    break outerloop; 
                                                }
                                            } else if (k == state.currentLocation.doors.size()) { // Execute when the door hasn't been found in the current location
                                                gameOutput("There is no " + noun2 + " here."); // Display a message for the absence of the specified door
                                                break; 
                                            }
                                        }
                                        break;
                                    case "lockeddoor":
                                    case "celldoor":
                                        k = 0; // Initialize counter k to 0
                                        for (Door door : state.currentLocation.doors) { // Loop to check if the specified door is in the current location
                                            k++; // Increment counter k for each door iteration
                                            if (door == null) { // Check if the door is null to avoid null errors
                                                if (k == 4) { // Execute when encountering the last null door
                                                    gameOutput("There is no " + noun2 + " here."); // Display a message for the absence of the specified door
                                                }
                                            } else if (door.name.equalsIgnoreCase(noun2)) { // Execute when the specified door is found
                                                if (!door.locked) {
                                                    gameOutput(noun2 + " is already unlocked."); // Display a message when the door is already unlocked
                                                    break outerloop; 
                                                }
                                                door.unlock(); // Unlock the door
                                                gameOutput("You successfully force the lock, but the shim breaks."); // Display a success message
                                                state.player.inventory.remove(item); // Remove the "shim" from the player's inventory as it breaks
                                                break outerloop; 
                                            } else if (k == state.currentLocation.doors.size()) { // Execute when the last door exists and the specified door is not found
                                                gameOutput("There is no " + noun2 + " here."); // Display a message for the absence of the specified door
                                            }
                                        }
                                        break;
                                }
                            } else if (i == state.player.inventory.size()) {
                                gameOutput("You do not have a shim."); // Display a message when the player does not have a "shim" in their inventory
                                break; 
                            }

                        }
                        break;
                    case "greenkey":
                        gameOutput("That key doesn't work on this door.");
                        break;

                    default: // Error Handling
                        gameOutput("You cannot use " + noun1 + " like that.");
                        break;
                }
                break;
            case "give":
                switch (noun1) {
                    case "tomas": // Check if the first noun refers to a character
                    case "goblin":
                    case "guard":
                        gameOutput("You cannot give a person to an item. (The give command should be formatted 'give [item] [character]'");
                        break;
                    default:
                        if (state.player.inventory.isEmpty()) {
                            gameOutput("You do not have a " + noun1 + ", your inventory is empty."); // Display a message when the inventory is empty
                            break;
                        }
                        i = 0; // Initialize counter i to 0
                        outerloop: for (Item item : state.player.inventory) { // Loop to check if the item is in the inventory
                            i++; // Increment counter i for each item iteration
                            if (item.name.equalsIgnoreCase(noun1)) { // Execute when the item is found
                                k = 0; // Initialize counter k to 0
                                for (Character character : state.currentLocation.charactersHere) { // Loop to check if the intended recipient is in the room
                                    k++; // Increment counter k for each character iteration
                                    if (character.name.equalsIgnoreCase(noun2)) { // Execute when the intended recipient is found
                                        gameOutput("You give the " + noun1 + " to " + noun2); // Display a message indicating the giving action
                                        character.addItem(item); // Add the item to the character's possessions
                                        state.player.removeItem(item); // Remove the item from the player's inventory
                                        break outerloop; 
                                    } else if (!character.name.equalsIgnoreCase(noun2) && k == state.currentLocation.charactersHere.size()) { // Execute when character not found and on the last character
                                        gameOutput(noun2 + " is not here."); // Display a message when the intended recipient is not in the room
                                        break outerloop;
                                    }
                                }
                            } else if (i == state.player.inventory.size()) { // Execute when it reaches the last item in the inventory
                                gameOutput("You do not have a " + noun1 + "."); // Display a message when the item is not found in the inventory
                            }
                        }
                        break;
                }
        }
    }

    /**
     * Handles the action of looking at a specific noun in the game environment.
     *
     * @param noun The noun to look at
     * @return A description of the looked-at noun or a message if it's not present
     */
    public String lookAt(String noun) {
        if (noun.equals("map") && App.hasMap){
            // Create a JLabel and set the ImageIcon as its icon
            JLabel label = new JLabel(mapImage);

            // Create a JFrame and add the JLabel to it
            JFrame frame = new JFrame();
            frame.getContentPane().add(label);

            // Set default close operation and make the frame visible
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null); // Center the frame
            frame.setVisible(true);

            return "You look at the map";
        }

        // Check items in the current location
        for (Item item : state.currentLocation.itemsHere) {
            if (item.name.equalsIgnoreCase(noun)) {
                return item.description; // Return item description if found in the current location
            }
        }

        // Check characters in the current location
        for (Character character : state.currentLocation.charactersHere) {
            if (character.name.equalsIgnoreCase(noun)) {
                return character.description; // Return character description if found in the current location
            }
        }

        return "That item/person is not here."; // If the noun is not found in the current location
    }

    /**
     * Prints out the help menu.
     * Iterates through all verbs and their descriptions, printing a list of available commands for the user.
     */
    public void printHelp() {
        int DISPLAY_WIDTH = GameState.DISPLAY_WIDTH; // Width of the display
        String s1 = "";
        while (s1.length() < DISPLAY_WIDTH)
            s1 += "-"; // Create a string line of '-' characters to serve as a visual separator

        String s2 = "";
        while (s2.length() < DISPLAY_WIDTH) {
            if (s2.length() == (DISPLAY_WIDTH / 2 - 10)) {
                s2 += " Commands "; // Title for the command section in the help menu
            } else {
                s2 += " "; // Fill the string with spaces until the title position
            }
        }

        // Display the help menu structure with separators and command section title
        System.out.println("\n\n" + s1 + "\n" + s2 + "\n" + s1 + "\n");

        // Loop through verbs and their descriptions to print the help menu
        for (String v : verbs) {
            // Printing each verb along with its formatted description
            System.out.printf("%-8s  %s", v, formatMenuString(verbDescription.get(verbs.indexOf(v))));
        }
    }

    /**
     * Formats a long string to fit the help menu display.
     * Splits the long string into multiple lines if it exceeds the display width.
     * @param longString The long string to be formatted
     * @return The formatted string for the help menu
     */
    public String formatMenuString(String longString) {
        String result = "";
        Scanner chop = new Scanner(longString); // Scanner to tokenize the long string
        int charLength = 0; // Track the total length of characters

        while (chop.hasNext()) {
            String next = chop.next(); // Get the next token from the long string
            charLength += next.length(); // Add the length of the token to the character count
            result += next + " "; // Append the token to the result string

            // Check if the accumulated characters exceed the display width, and break lines accordingly
            if (charLength >= (GameState.DISPLAY_WIDTH - 30)) {
                result += "\n          "; // New line for better formatting
                charLength = 0; // Reset character count for the new line
            }
        }
        chop.close(); // Close the scanner
        return result + "\n\n"; // Return the formatted string with additional spaces
    }

    /**
     * Default game output method.
     * This method serves as an alias for the other gameOutput method.
     * It defaults to performing both bracketing and width formatting.
     *
     * @param longString The message to be displayed in the game
     */
    public void gameOutput(String longString) {
        gameOutput(longString, true, true); // Call the gameOutput method with default bracketing and width formatting
    }

    /**
     * Displays game output based on provided options.
     *
     * @param longString   The string to be displayed
     * @param addBrackets  Whether to add brackets around the string
     * @param formatWidth  Whether to format the string to a specific width
     */
    public void gameOutput(String longString, boolean addBrackets, boolean formatWidth) {
        // Adding brackets around the string if required
        if (addBrackets) {
            longString = addNounBrackets(longString);
        }

        // Formatting the string to a specific width if required
        if (formatWidth) {
            longString = formatWidth(longString);
        }

        // Printing the formatted string to the console
        System.out.println(longString);
    }

    /**
     * Formats a string to a specific display width.
     * Used for getting descriptions from items/locations and printing them to the screen.
     * Special characters like [nl] indicate a new line in the string.
     * @param longString The string to be formatted
     * @return The formatted string with line breaks as needed
     */
    public String formatWidth(String longString) {

        Scanner chop = new Scanner(longString); // Initialize a scanner to chop the string
        String result = ""; // Initialize the result string
        int charLength = 0; // Initialize the character length counter
        boolean addSpace = true; // Initialize the flag to add a space

        while (chop.hasNext()) {
            // Get the next word in the string
            String next = chop.next();

            // Update the character length count
            charLength += next.length() + 1;

            // Handle special newline characters [nl]
            if (next.contains("[nl]")) {
                int secondHalf = next.indexOf("[nl]") + 4;

                // Adjust character length for new line
                if (secondHalf < next.length()) {
                    charLength = secondHalf;
                } else {
                    charLength = 0;
                    addSpace = false; // Do not add space if this ended with a newline character
                }

                // Replace [nl] with a newline character
                next = next.replace("[nl]", "\n");
            }

            // Append the word to the result
            result += next;

            // Add a space unless a special case occurred
            if (addSpace)
                result += " ";

            // Prepare for adding a space after the word
            addSpace = true;

            // Add a newline if the character length exceeds the display width
            if (charLength >= GameState.DISPLAY_WIDTH) {
                result += "\n";
                charLength = 0;
            }
        }
        chop.close(); // Close the scanner
        return result; // Return the formatted string
    }

    /**
     * Adds brackets around whole words that are included in the `nouns` list,
     * ignoring case, and also deals with any that have punctuation after them.
     *
     * @param longString the string to check for nouns
     * @return the modified string with brackets around the nouns
     */
    public String addNounBrackets(String longString) {
        String[] words = longString.split("\\s+"); // Split the input string into words
        for (int i = 0; i < words.length; i++) {
            String word = words[i].replaceAll("\\p{Punct}+$", ""); // Remove trailing punctuation from the word
            String punct = words[i].substring(word.length()); // Extract punctuation after the word
            for (String noun : nouns) { // Iterate through the list of nouns
                if (word.equalsIgnoreCase(noun)) { // Check if the word matches any noun in the list (case insensitive)
                    words[i] = "[" + word + "]" + punct; // Add brackets around the matched noun and restore the punctuation
                    break;
                }
            }
        }
        return String.join(" ", words); // Join the modified words back into a string
    }

    /**
     * Adds a noun to the noun list.
     * Informs the command system that this noun can be interacted with.
     *
     * @param string The noun to be added
     */
    public void addNoun(String string) {
        // Check if the noun is not already present in the list (case-insensitive)
        if (!nouns.contains(string.toLowerCase())) {
            // Add the noun to the list in lowercase to avoid case sensitivity
            nouns.add(string.toLowerCase());
        }
    }

    /**
     * Adds a new verb to the command system along with its description.
     * This method registers new commands within the system for recognition.
     *
     * @param verb        The verb to be added to the command system
     * @param description The description explaining the verb's functionality
     */
    public void addVerb(String verb, String description) {
        // Add the verb to the list in lowercase to ensure uniformity and avoid case sensitivity issues
        verbs.add(verb.toLowerCase());
        // Add the verb's description to the description list in lowercase for consistency
        verbDescription.add(description.toLowerCase());
    }


    /**
     * Checks if a verb is registered in the game.
     *
     * @param string The verb to check
     * @return True if the verb is registered, otherwise false
     */
    public boolean hasVerb(String string) {
        // Check if the verb is present in the list (case-insensitive)
        return verbs.contains(string.toLowerCase());
    }

    /**
     * Checks if a noun is registered in the game.
     *
     * @param string The noun to check
     * @return True if the noun is registered, otherwise false
     */
    public boolean hasNoun(String string) {
        // Check if the noun is present in the list (case-insensitive)
        return nouns.contains(string.toLowerCase()); // Returns true if the noun exists in the list, otherwise false
    }

}