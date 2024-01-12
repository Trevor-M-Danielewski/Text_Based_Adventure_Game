import java.util.*;

/*
  Program: Text Adventure Game
  Authors: Noah Glorioso & Trevor Danielewski
  Class: COSC 236
  Date: 12/7/2023
  Description:
    This program is a text adventure game that allows the user to explore a dungeon and interact with various objects and characters.
    The user can move between rooms, pick up items, and use items to unlock doors and defeat enemies.
    The user can also interact with characters to receive hints and clues about the game.
    The game ends when the user escapes the dungeon or dies.

TODO: ERROR FIXES

*/

public class App {
    // Create a scanner object to read user input
    static Scanner in = new Scanner(System.in);

    // A series of booleans that track certain conditions
    static boolean gameEnd = false; //if game endings have been reached
    static boolean usedShim = false; //if player has used Tomas' shim
    static boolean tomasFree = false; //if Tomas has been freed
    static boolean enteredBarracks = false; //if player has entered barracks
    static boolean spoken = false; //if a character has already said their line
    static boolean spawned = false; //if something has already spawned in a room
    static boolean hasMap = false; //if the player has the map

    // Main method for running the game
    public static void main(String[] args) {

        // Create the game state object
        GameState state = new GameState();

        // Store the command system for easy reference
        CommandSystem commandSystem = state.commandSystem;

        // Control variable to manage the game loop
        boolean gameRunning = true;

        // Print a line of dashes to separate the user input from the game output
        for (int i = 0; i <= 100; i++) {
            System.out.print("-");
        }
        System.out.println();

        // Display introductory information about the game

        // Setting up the initial scenario for the player:

        commandSystem.gameOutput(" tink... tink... tink... "); // Sound effect for the goblin digging

        commandSystem.gameOutput(" "); // Empty line for better formatting

        commandSystem.gameOutput("The strange sound awakens you. Your forehead pounds with aches,\n" +
                                 "and your body feels heavy. You're cold and exhausted, but barely conscious. There's a little stream of\n" +
                                 "light that illuminates a dark cell that surrounds you.\n");

        commandSystem.gameOutput(" "); // Empty line for better formatting

        commandSystem.gameOutput("The sound grows louder.");

        commandSystem.gameOutput(" "); // Empty line for better formatting

        commandSystem.gameOutput(" TINK... TINK... TINK... "); // Sound effect for the goblin digging

        commandSystem.gameOutput(" "); // Empty line for better formatting

        commandSystem.gameOutput("You hear a small metallic clinking right next to you, and it draws your sight to your left. Looking over\n" +
                                 "you see a dirty and ragged prisoner, his face pressing against the cell bars, reaching his hand through\n" +
                                 "the slits of the bars. He's trying to grasp for small piece of metal on the cobblestone floor.\n");

        commandSystem.gameOutput("You hear a loud crash outside of your cell");

        commandSystem.gameOutput(" "); // Empty line for better formatting

        // Introducing Tomas, another character in the story:
        commandSystem.gameOutput("The prisoner notices you're awake.");
        commandSystem.gameOutput("[Prisoner]: \"Hey, I'm Tomas. I dropped that tool and can't seem to reach it, could you grab it and\n" +
                                 "hand it over so I get the hell out of this cell? My buddy's waiting on the other side of that hole\n" +
                                 "for me. Uh... don't use it on your cell door please...\"\n");

        commandSystem.gameOutput(" "); // Empty line for better formatting

        // Prompting the player with available commands:
        commandSystem.gameOutput("Type ? for commands.");

        // Main game loop
        while (gameRunning) {

            // Check the current game state, process commands, and handle dialogue
            checkCondition(state, commandSystem);

            // If the game has ended, stop the loop and close the input stream
            if (gameEnd) {
                gameRunning = false;
                commandSystem.gameOutput(" "); // Empty line for better formatting
                commandSystem.gameOutput("To restart the game, run the program again."); //allows user to read last message before window closes
                commandSystem.gameOutput(" "); // Empty line for better formatting
                commandSystem.gameOutput("Type 'quit' exit the game."); //allows user to read last message before window closes
            }

            // Get user input as an array of strings
            String[] input = getCommand();

            // Check various conditions for user input
            if (input.length < 1) {
                // Inform the user about an unknown command and suggest help
                System.out.println("Unknown command. Type ? for help.");

            } else if (input[0].equalsIgnoreCase("quit")) {
                // Quit the game if the user types "quit"
                gameRunning = false;
                System.out.println("You Have Ended Your Adventure.");
                in.close(); // Closing the input stream

            } else if (input.length == 1 && commandSystem.hasVerb(input[0])) {
                // Execute single-word commands (verbs)
                commandSystem.executeVerb(input[0]);

            } else if (input.length == 2 || input.length == 3) {
                // Execute two-word or three-word commands (verb + noun[s])
                validateAndExecuteCommand(input, commandSystem);

            } else {
                // Handle any other unknown command structures
                handleUnknownCommand(input);
            }
        }
    }

    /**
     * Get user input from the console and split it into an array of strings.
     *
     * @return An array containing user input split by spaces
     */
    public static String[] getCommand() {
        // Print a line of dashes to separate the user input from the game output
        for (int i = 0; i <= 50; i++) {
            System.out.print("-");
        }
        System.out.println();

        // Prompt the user to input a command
        System.out.print("What would you like to do? >  ");

        // Read the user input
        String input = in.nextLine();
        System.out.println(); // Add an empty line for better formatting

        // Convert the input to lowercase and split it by spaces
        return input.toLowerCase().split("\\s+");
    }

    /**
     * Handles unknown or unrecognized commands.
     *
     * @param input The array of strings representing user input
     */
    public static void handleUnknownCommand(String[] input) {
        // Check if the user input contains more than one word
        if (input.length > 1) {
            // If multiple words entered, concatenate them into a single string
            String userInput = String.join(" ", input);
            unknownCommand(userInput); // Call the unknownCommand function with the concatenated user input
        } else {
            // If only one word entered, treat it as an unknown command
            unknownCommand(input[0]); // Call the unknownCommand function with the single word
        }
    }

    /**
     * Validates and executes commands based on the number of words entered.
     *
     * @param input         The array of strings representing user input
     * @param commandSystem The CommandSystem object responsible for command handling
     */
    public static void validateAndExecuteCommand(String[] input, CommandSystem commandSystem) {
        // Check if the entered command has a valid verb
        if (!commandSystem.hasVerb(input[0])) {
            unknownCommand(input[0]); // Notify about unknown verb
        } else if (!commandSystem.hasNoun(input[1])) {
            unknownCommand(input[1]); // Notify about unknown noun
        } else {
            // Command has both a valid verb and noun
            if (input.length == 2) {
                commandSystem.executeVerbNoun(input[0], input[1]); // Execute the command with a verb and a noun
            } else if (!commandSystem.hasNoun(input[2])) {
                unknownCommand(input[2]); // Notify about unknown noun
            } else {
                // Command has a verb and two valid nouns
                commandSystem.executeVerbNounNoun(input[0], input[1], input[2]); // Execute the command with a verb and two nouns
            }
        }
    }

    /**
     * Displays a message for unknown commands.
     *
     * @param input The string representing the unknown command
     */
    public static void unknownCommand(String input) {
        if (Math.random() < .01) // 1% chance for a humorous response
            System.out.println("What the hell does '" + input + "' mean? Type ? for help.");
        else
            System.out.println("I don't understand '" + input + "'. Type ? for help.");
    }

    /**
     * Checks the current game state and handles various game conditions.
     *
     * @param state         The GameState object representing the current game state
     * @param commandSystem The CommandSystem object responsible for command handling
     */
    public static void checkCondition(GameState state, CommandSystem commandSystem) {
        int i = 0; //counter used in for each loops

        // Checks if the player's health is zero or below; triggers game over if true
        if (state.player.health <= 0) {
            commandSystem.gameOutput("=====================YOU DIED====================="); // Outputs a message indicating the player's death
            gameEnd = true; // Sets the game flag to indicate that the game has concluded
        }

        // Iterate through each shim spawn location
        for (Location spawnLocation : state.shimSpawnLocation) {
            // Check if the current player location matches a shim spawn location and something hasn't already spawned
            if (state.currentLocation.equals(spawnLocation) && !spawned) {
                try {

                    // prevents shim from spawning if one is already in player inventory
                    for (Item item : state.player.inventory){
                        if (item.name.equals("shim")){
                            throw new IllegalStateException();
                        }
                    }

                    // prevents shim from spawning if one is already in currentLocation
                    for (Item item : state.currentLocation.itemsHere){
                        if (item.name.equals("shim")){
                            throw new IllegalStateException();
                        }
                    }

                    // Generates a 5% chance of a shim spawning in the current room
                    if (Math.random() < 0.05) { //Default: 0.05
                        //marks temporarily that this room has been spawned in
                        spawned = true;

                        // Create a shim item
                        Item shim = new Item("shim", "a shim", 0);

                        // Add the shim to the current location
                        state.currentLocation.addItem(shim);

                        commandSystem.gameOutput(" "); // Empty line for better formatting

                        // Display the shim spawn hint message
                        commandSystem.gameOutput("There's something shiny on the floor...");

                        commandSystem.gameOutput(" "); // Empty line for better formatting

                    }
                } catch (IllegalStateException e) {
                    // Catching an IllegalStateException and breaking out of the try block
                    break;
                }
            }
        }

        // Iterate through each guard spawn location
        for (Location spawnLocation : state.guardSpawnLocation) {
            // Check if the current player location matches a guard spawn location
            if (state.currentLocation.equals(spawnLocation) && !spawned) {
                try {
                    // Check if another character is present in the current location
                    if (!state.currentLocation.charactersHere.isEmpty()) {
                        // Throw an exception if another character is found in the current location
                        throw new IllegalStateException();
                    }

                    // Generates the chances of a guard spawning in the current room based on the player's health
                    double guardChance;
                    double foodChance;
                    if (state.player.health >= 12) {
                        guardChance = 0.20; // set to 0.20
                        foodChance = 0.20; // set to 0.20
                    } else {
                        guardChance = 0.10; // set to 0.10
                        foodChance = 0.30; // set to 0.30
                    }

                    // Generates a chance of a guard spawning in the current room
                    if (Math.random() < guardChance) {
                        //marks temporarily that this room has been spawned in
                        spawned = true;

                        // Create a guard character
                        Character guard = new Character("guard", "a guard", 12);

                        commandSystem.gameOutput(" "); // Empty line for better formatting

                        // Display guard spawn message
                        commandSystem.gameOutput("A " + guard.name + " walked into " + state.currentLocation.name + "!");

                        // Add the guard to the current location
                        state.currentLocation.addCharacter(guard);

                        // Generate a chance of the guard having a health restore item
                        if (Math.random() < foodChance) {
                            // Add food to the guard's inventory
                            Item food = new Item("food", "food", 0);
                            guard.addItem(food);

                        } else {
                            // Add a snack for the guard's inventory
                            Item snack = new Item("snack", "a snack", 0);
                            guard.addItem(snack);
                        }

                        // Generate a random number to determine the guard's weapon
                        Random random = new Random();
                        int randomNumber = random.nextInt(4) + 1;

                        // Assigns a random weapon to the guard based on the generated number
                        switch (randomNumber) {
                            case 1:
                                int randomNumSword = random.nextInt(3) + 7; // Random number between 7 and 9

                                // Create a sword item with a strength of a randomize number and add it to the guard's items
                                Item sword = new Item("sword", "a weapon", randomNumSword);
                                guard.addItem(sword);
                                break;

                            case 2:
                                int randomNumMace = random.nextInt(3) + 4; // Random number between 4 and 6

                                // Create a mace item with a strength of a randomize number and add it to the guard's items
                                Item mace = new Item("mace", "a weapon", randomNumMace);
                                guard.addItem(mace);
                                break;

                            case 3:
                                int randomNumAxe = random.nextInt(3) + 6; // Random number between 6 and 8

                                // Create an axe item with a strength of a randomize number and add it to the guard's items
                                Item axe = new Item("axe", "a weapon", randomNumAxe);
                                guard.addItem(axe);
                                break;

                            case 4:
                                int randomNumBroadSword = random.nextInt(3) + 9; // Random number between 9 and 11

                                // Create a broadsword item with a strength of a randomize number and add it to the guard's items
                                Item broadSword = new Item("broadsword", "a weapon", randomNumBroadSword);
                                guard.addItem(broadSword);
                                break;
                        }
                    }
                } catch (IllegalStateException e) {
                    // Catching an IllegalStateException and breaking out of the try block
                    break;
                }
            }
        }

        // Events based on the current location
        switch (state.currentLocation.name) {
            case "cell":
                for (Character character : state.currentLocation.charactersHere) {
                    if (character.inventory.size() < 3) { //prevents out of bounds error if nothing at index 2
                        break;
                    }
                    if (character.name.equals("tomas")) { //finds Tomas in the room
                        if (character.inventory.get(2).name.equals("shim")) { //runs when player gives tomas a shim
                            if (!usedShim) { //runs if player hasn't left cell (original shim, celldoor locked)

                                commandSystem.gameOutput(" "); // Empty line for better formatting

                                commandSystem.gameOutput("tomas: \"Hey thanks! See ya!\"");

                                commandSystem.gameOutput(" "); // Empty line for better formatting

                                commandSystem.gameOutput("He takes the shim and breaks out of his cell. He follows his goblin friend into the hole in\n" +
                                                         "the wall, leaving you to rot in your cell.\n");

                                commandSystem.gameOutput(" "); // Empty line for better formatting

                                gameEnd = true;
                                break;

                            } else { //runs if player has left cell, so they have to give tomas key, not shim

                                commandSystem.gameOutput(" "); // Empty line for better formatting

                                commandSystem.gameOutput("tomas: \"Damn, it won't work on my lock. You'll have to find the key, it should be green.");

                                commandSystem.gameOutput(" "); // Empty line for better formatting

                                character.inventory.remove(character.inventory.get(2));
                                break;
                            }
                        }
                        if (character.inventory.get(2).name.equals("greenkey") && !tomasFree) { //runs if Tomas has greenkey

                            commandSystem.gameOutput(" "); // Empty line for better formatting

                            commandSystem.gameOutput("tomas: \"Hey, thanks for the key. Now I can unlock this door and we can get the hell out of here.\"\n");

                            state.player.charactersWith.add(character); //adds tomas to the charactersWith array, so he can travel with player
                            tomasFree = true;
                            break;
                        }
                    }
                }

                if (usedShim && !tomasFree && !spoken) { //test for when player returns to cell, has tomas complain if he is there

                    commandSystem.gameOutput("tomas: \"You're back already? Did you get a key for my cell?\"");

                    commandSystem.gameOutput(" "); // Empty line for better formatting

                    spoken = true;

                }

                if (!state.currentLocation.doors.get(2).locked && !usedShim) { //checks to see if player uses tomas' shim, and has him talk if so

                    commandSystem.gameOutput(" "); // Empty line for better formatting

                    commandSystem.gameOutput("tomas: \"Hey, what the hell? I said not to do that! That was my last one! You got to get me out of\n" +
                            "this place! Go find the key, it should be green.\"");

                    commandSystem.gameOutput(" "); // Empty line for better formatting

                    commandSystem.gameOutput("tomas: \"OH WAIT! Before you go, take this... \""); // Empty line for better formatting

                    commandSystem.gameOutput(" "); // Empty line for better formatting

                    commandSystem.gameOutput("You took a map!"); // Empty line for better formatting

                    commandSystem.gameOutput(" "); // Empty line for better formatting

                    commandSystem.gameOutput("tomas: \"My buddy was able to get that map of the dungeon. I guess you'll get more use out of it than me now. The red X marks my cell.\"");

                    commandSystem.gameOutput(" "); // Empty line for better formatting

                    usedShim = true; //boolean to make sure this only runs once, won't run again if player returns to cell
                    spoken = true; //stops Tomas from saying his next line before player comes back
                    hasMap = true; //marks that player has the map

                }
                break;

            case "cave":
                Door path = new Door("path", false); // create the path door to be unlocked

                if (!tomasFree && !spoken && !state.currentLocation.charactersHere.isEmpty()) { //goblins response if you try to leave without tomas and the goblin is still there (not dead)
                    commandSystem.gameOutput("Standing in the cave you see a small goblin with a lantern and carrying a pickaxe. He looks at you with a confused look on his face.\n");

                    commandSystem.gameOutput(" "); // Empty line for better formatting

                    commandSystem.gameOutput(" goblin: \"Who the hell are you? You're not tomas! I'm not letting you through here without him.\"\n");

                    spoken = true;
                }

                if (!state.currentLocation.charactersHere.isEmpty()) {
                    i = 0;
                    for (Character character : state.currentLocation.charactersHere) { //iterates through
                        i++; //counter for loop iteration

                        if (character.name.equals("goblin")) { //breaks loop if goblin is found
                            break;
                        } else if (i == state.currentLocation.charactersHere.size()) { //unblocks path if goblin is not found (he's dead)
                            state.currentLocation.doors.set(1, path); //sets blocked path to path
                        }
                    }
                } else {
                    state.currentLocation.doors.set(1, path);
                }

                if (tomasFree) { //goblins response if you leave with tomas

                    commandSystem.gameOutput(" "); // Empty line for better formatting

                    commandSystem.gameOutput("goblin: \"tomas, there you are! Finally. Let's get you out of here.\"");

                    commandSystem.gameOutput(" "); // Empty line for better formatting

                    state.currentLocation.doors.set(1, path);
                    state.player.charactersWith.add(state.currentLocation.charactersHere.get(0)); //add goblin (always begins at index 0) to charactersWith
                }
                break;

            case "guard room":
                // If the player is in the guard room, describe an encounter with guards and set player health to 0 (signifying death)
                state.player.changeHealth(-10000000); // Set player health to 0, signifying death

                commandSystem.gameOutput(" "); // Empty line for better formatting

                commandSystem.gameOutput("There are a handful of guards in the room, and they spring to their feet to attack.\n" +
                                         "\"One of them slashes at you with their sword, spilling your guts, and another stabs their sword\n" +
                                         "through your chest.\n" +
                                         "\n");

                commandSystem.gameOutput(" "); // Empty line for better formatting

                commandSystem.gameOutput("=============================YOU DIED============================="); // Outputs a mess age indicating the player's death

                commandSystem.gameOutput(" "); // Empty line for better formatting

                gameEnd = true; // Sets the game flag to indicate that the game has concluded
                break;

            case "trap room":
                Random rand = new Random();
                int chance = rand.nextInt(3) + 1;
                switch (chance) {
                    case 1:
                        commandSystem.gameOutput(" "); // Empty line for better formatting

                        commandSystem.gameOutput("You enter an empty room, after a moment, you hear the sound of stones grinding. The floor\n" +
                                                 "collapses beneath you and you fall into a pit of spikes!\n");

                        commandSystem.gameOutput(" "); // Empty line for better formatting

                        commandSystem.gameOutput("=============================YOU DIED============================="); // Outputs a mess age indicating the player's death
                        gameEnd = true; // Sets the game flag to indicate that the game has concluded
                        break;

                    case 2:
                        commandSystem.gameOutput(" "); // Empty line for better formatting

                        commandSystem.gameOutput("You enter an empty room, after a moment, you hear the door lock behind you. The walls begin\n" +
                                                 "closing in on you. Unable to stop them, you feel the walls crush your skeleton.\n");

                        commandSystem.gameOutput(" "); // Empty line for better formatting

                        commandSystem.gameOutput("=============================YOU DIED============================="); // Outputs a mess age indicating the player's death
                        gameEnd = true; // Sets the game flag to indicate that the game has concluded
                        break;

                    case 3:
                        commandSystem.gameOutput(" "); // Empty line for better formatting

                        commandSystem.gameOutput("You enter an empty room and without realizing step on a pressure plate.\n" +
                                                 "Poison darts shoot from the walls, and stings all over your body. You collapse, your limbs\n" +
                                                 "begin to numb, and your vision slowly fades to eternal darkness.\n");

                        commandSystem.gameOutput(" "); // Empty line for better formatting

                        commandSystem.gameOutput("=============================YOU DIED============================="); // Outputs a mess age indicating the player's death
                        gameEnd = true; // Sets the game flag to indicate that the game has concluded
                        break;
                }
                break;

            case "barracks":
                if (!enteredBarracks) {
                    commandSystem.gameOutput("You enter the barracks and see several guards sleeping in their cots. If you are not careful, you might wake them up!\n");
                    enteredBarracks = true;
                } else if (Math.random() < 0.33) {
                    commandSystem.gameOutput(" "); // Empty line for better formatting

                    commandSystem.gameOutput("You woke up the guards. They spring to their feet, grabbing their weapons. " +
                            "You are tackled to the ground by one of them, and another stabs his sword through your chest.");

                    commandSystem.gameOutput(" "); // Empty line for better formatting

                    commandSystem.gameOutput("=============================YOU DIED============================="); // Outputs a mess age indicating the player's death
                    gameEnd = true; // Sets the game flag to indicate that the game has concluded
                }
                break;

            case "bandit camp": // If the player is at the bandit camp, display an ending message and end the game
                commandSystem.gameOutput(" "); // Empty line for better formatting

                if (!state.player.charactersWith.isEmpty()) { //if tomas and the goblin are with the player
                    commandSystem.gameOutput("The bandits in the camp crowd around the goblin, Tomas, and you. They are whispering to each other,\n" +
                            "and have faces of disbelief. You were able to help their friend, Tomas, break out of the unfair\n" +
                            "imprisonment by the guards.\n");

                    commandSystem.gameOutput(" "); // Empty line for better formatting

                    commandSystem.gameOutput("There begins a commotion of cheers and laughter! A few bandits grab\n" +
                            "their instruments and begin playing an upbeat tune. The others guide you all to the campfire,\n" +
                            "and begin to prepare a feast in celebrating of the return of a friend.\n");
                } else { //if Tomas and the goblin are not with the player
                    commandSystem.gameOutput("You head north, eventually happening upon what appears to be a small bandit camp. A small group of bandits" +
                            " approaches you. They demand your money, but soon relent, seeing that you have nothing to offer them. Instead, they offer you food" +
                            " and company. With nowhere else to go, you hesitantly agree.");

                    commandSystem.gameOutput(" "); // Empty line for better formatting

                    commandSystem.gameOutput("As the night goes on, you get to know several of the bandits. Seeing no other prospects, you take their offer" +
                            " when they invite you to join their group. Luckily, they were looking for new members, as they recently lost two of their best members," +
                            " Tomas and his friend the goblin. As you fall asleep on a bedroll, you dream of where your new career will take you.");
                }

                commandSystem.gameOutput(" "); // Empty line for better formatting

                commandSystem.gameOutput("You have beaten the game!");
                gameEnd = true; // Mark the game as ended
                break;

            case "inn": // If the player is at the inn, display an ending message and end the game
                commandSystem.gameOutput(" "); // Empty line for better formatting

                if (!state.player.charactersWith.isEmpty()) { //if player is with Tomas and goblin
                    commandSystem.gameOutput("You leave Tomas and the goblin to their own journey. You head south and spot a friendly looking\n" +
                            "inn, and decide to rest for the night. As you enter the inn you are greeted with a warmth of a\n" +
                            "fire and the smell of a delicious stew.\n");
                } else { //if player is not with Tomas and goblin
                    commandSystem.gameOutput("You head south and spot a friendly looking\n" +
                            "inn, and decide to rest for the night. As you enter the inn you are greeted with a warmth of a\n" +
                            "fire and the smell of a delicious stew.\n");
                }

                commandSystem.gameOutput(" "); // Empty line for better formatting

                commandSystem.gameOutput("There are other travellers sipping their beer, enjoying\n" +
                                         "their meals, and quietly humming to the bard's melodic tune. You find a seat at the bar and\n" +
                                         "order a bowl of hot stew with a pint of beer. You sit back and let out a heavy sigh of relief.\n" +
                                         "Your new adventure of freedom is about to begin in the morning.\n");

                commandSystem.gameOutput(" "); // Empty line for better formatting

                commandSystem.gameOutput("You have beaten the game!");

                commandSystem.gameOutput(" "); // Empty line for better formatting

                gameEnd = true; // Mark the game as ended
                break;

            default:
                break;
        }
    }
}