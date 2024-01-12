import java.util.*;

public class GameState {

    // FORMATTING
    public static int DISPLAY_WIDTH = 100; // Constant representing display width

    // DATA FIELDS
    Location currentLocation; // Represents the current location of the player
    CommandSystem commandSystem; // Manages the commands for the game
    Player player = new Player(); // Represents the player, holds inventory and health
    List<Location> guardSpawnLocation = new ArrayList<>(); // List of locations for guards to spawn
    List<Location> shimSpawnLocation = new ArrayList<>(); // List of location for shims to spawn

    // CONSTRUCTOR
    public GameState() {

        // Initializing the command system with the current game state
        commandSystem = new CommandSystem(this);

        // Initialized randy, used for random spawning of redKey and blueKey
        Random randy = new Random();

        // CREATE LOCATIONS
        // Define various game locations with descriptions
        Location cell = new Location("cell", "You are in a cramped cell behind iron bars.");
        Location guardRoom = new Location("guard room", "You are in a small room with a table and some chairs.");
        Location messHall = new Location("mess hall", "You are in a small mess hall. There are several empty tables, set with dinner plates.");
        Location kitchen = new Location("kitchen", "You are in the kitchen. There's multiple furnaces roaring with heat, and a scent of slightly burnt chicken lingering in the air. ");
        Location tortureChamber = new Location("torture chamber", "You are in a torture chamber. There are blood stained tools littering the long tables, and buckets of old blood underneath. The foul stench is unsettling.");
        Location bathRoom = new Location("bathroom", "You are in a bathroom. There are buckets lining one wall, and showers lining the other.");
        Location courtYard = new Location("court yard", "You are outside in the court yard. The warmth of the sun feels wonderful. There are shrubs and flowers around you.");
        Location barracks = new Location("barracks", "You are in the barracks. There are aisles of bunk beds, and the sound of snores. There are guards fast asleep.");
        Location closet1 = new Location("closet1", "You are in a small closet with cleaning supplies.");
        Location closet2 = new Location("closet1", "You are in a small closet with tools and pieces of wood.");
        Location closet3 = new Location("closet3", "You are in a small closet with fur cloaks.");
        Location hallway1 = new Location("hallway1", "You are in a hallway with a few doors.");
        Location hallway2 = new Location("hallway2", "You are in a hallway with a few doors.");
        Location hallway3 = new Location("hallway3", "You are in a hallway with a few doors.");
        Location hallway4 = new Location("hallway4", "You are in a hallway with a few doors.");
        Location hallway5 = new Location("hallway5", "You are in a hallway with a few doors.");
        Location office = new Location("office", "You are in the warden's office. There is a stained oak desk with piles of papers, and behind it, a shelf with trinkets and knickknacks. ");
        Location armory = new Location("armory", "You are in a small armory. There are racks of different weapons for use by the guards.");
        Location dungeon1 = new Location("dungeon1", "You are in a cold and dark room lined with prison cells.");
        Location dungeon2 = new Location("dungeon2", "You are in a cold and dark room lined with prison cells.");
        Location dungeon3 = new Location("dungeon3", "You are in a cold and dark room lined with prison cells.");
        Location cave = new Location("cave", "You are in a dark cave. There is a dim light far ahead.");
        Location outside = new Location("outside", "        You exit the cave. The light from the sun warms you up, and a slight breeze rustles the trees and bushes\n" +
                                                   "        around you. The trees softly shake, and birds chirp in response. You release a heavy sigh of relief, and\n" +
                                                   "        stretch your arms up behind your head.\n");
        Location banditCamp = new Location("bandit camp", "You enter a camp full of bandits with Tomas and the goblin.");
        Location inn = new Location("inn", "You enter a warm inn that has a comforting atmosphere and a warm bed for you.");
        Location trapRoom = new Location("trap room", "a room with a trap. ");

        Location debug = new Location("Debug room", "This is for debugging purposes only."); // Debugging location


        // CREATE ITEMS
            // Keys
        Item blueKey = new Item("bluekey", "a key to a blue door.", 0); // Create a blue key item
        Item redKey = new Item("redkey", "a key to a red door.", 0); // Create a red key item
        Item greenKey = new Item("greenkey","a key to tomas' cell.", 0); // Create green key item

            // Weapons
        Item sword = new Item("sword", "a weapon", 8); // Create a sword item
        Item mace = new Item("mace", "a weapon", 6); // Create a mace item
        Item axe = new Item("axe", "a weapon", 7); // Create an axe item
        Item broadSword = new Item("broadsword", "a weapon", 9); // Create a broadsword item
        Item bloodyTool = new Item("bloodytool", "a weapon", 6); // Create a bloody tool item
        Item pickaxe = new Item("pickaxe", "a weapon", 5); // Create a pickaxe item

            // Tools
        Item shim = new Item("shim", "a small tool to break locks", 0); // Create a shim item for breaking locks

            // Consumables
        Item food = new Item("food", "a bowl of gruel", 0); // Create a food item for sustenance
        Item snack = new Item("snack", "a small snack that restores some health.", 0); // Create a snack item for health restoration

            // Misc
        Item fists = new Item("fists", "things on the end of arms.", 2); // Create a fist item representing physical attack strength

        // CREATE CHARACTERS
        Character tomas = new Character("tomas", "another prisoner", 15); // Create a character named Tomas, another prisoner with certain attributes
        Character guard = new Character("guard", "a guard", 15); // Create a character named Guard, representing a guard with specific attributes
        Character goblin = new Character("goblin", "a goblin", 12); // Create a character named Goblin, representing a goblin with specific attributes

        // CREATE DOORS
        Door defaultDoor = new Door("door", false); // Create a default, unlocked door
        Door lockedDoor = new Door("lockeddoor", true); // Create a default, locked door (no key, but possibly can be opened with a shim)
        Door cellDoor = new Door("celldoor", true); // Create a door leading to players' cells and Tomas' cell
        Door blueDoor = new Door("bluedoor", true); // Create a door leading to the warden's office
        Door redDoor = new Door("reddoor", true); // Create a door leading to the armory
        Door hole = new Door("hole", false); // Create a hole in the wall leading to the cave
        Door path = new Door("path", false); // Create a path that is used outside
        Door blocked = new Door("goblinblock", true); // Create a block that is used when the goblin stops player from leaving

        // Define exits and doors for each location

        // Cell exits setup
        cell.addExit(null); // Wall (No door)
        cell.addExit(null); // Wall (No door)
        cell.addExit(dungeon1); // South exit of Cell leads to the dungeon1
        cell.addExit(null); // Wall (No door)

        // Cell door setup
        cell.addDoor(null); // Wall (No door)
        cell.addDoor(null); // Wall (No door)
        cell.addDoor(cellDoor); // South door is a cellDoor
        cell.addDoor(null); // Wall (No door)

        // kitchen exits setup
        kitchen.addExit(messHall); // North exit of kitchen leads to the kitchen
        kitchen.addExit(null); // Wall (No door)
        kitchen.addExit(null); // Wall (No door)
        kitchen.addExit(null); // Wall (No door)

        // kitchen door setup
        kitchen.addDoor(defaultDoor); // North door is an unlocked door
        kitchen.addDoor(null); // Wall (No door)
        kitchen.addDoor(null); // Wall (No door)
        kitchen.addDoor(null); // Wall (No door)

        // barracks exits setup
        barracks.addExit(hallway5); // North exit of barracks leads to the hallway5
        barracks.addExit(null); // Wall (No door)
        barracks.addExit(null); // Wall (No door)
        barracks.addExit(null); // Wall (No door)

        // barracks door setup
        barracks.addDoor(defaultDoor); // North door is a defaultDoor
        barracks.addDoor(null); // Wall (No door)
        barracks.addDoor(null); // Wall (No door)
        barracks.addDoor(null); // Wall (No door)

        // courtYard exits setup
        courtYard.addExit(tortureChamber); // North exit of courtYard leads to the tortureChamber
        courtYard.addExit(messHall); // East exit of courtYard leads to the messHall
        courtYard.addExit(trapRoom); // Wall (No door)
        courtYard.addExit(hallway4); // West exit of courtYard leads to the hallway4

        // courtYard door setup
        courtYard.addDoor(defaultDoor); // North door is a defaultDoor
        courtYard.addDoor(defaultDoor); // East door is a defaultDoor
        courtYard.addDoor(defaultDoor); // Wall (No door)
        courtYard.addDoor(defaultDoor); // South door is a defaultDoor

        // bathRoom exits setup
        bathRoom.addExit(null); // Wall (No door)
        bathRoom.addExit(hallway2); // East exit of bathRoom leads to the hallway2
        bathRoom.addExit(null); // Wall (No door)
        bathRoom.addExit(null); // Wall (No door)

        // bathRoom door setup
        bathRoom.addDoor(null); // Wall (No door)
        bathRoom.addDoor(defaultDoor); // East door is a lockedDoor
        bathRoom.addDoor(null); // Wall (No door)
        bathRoom.addDoor(null); // Wall (No door)

        // closet1 exits setup
        closet1.addExit(null); // all-purpose cleaner wall (No door)
        closet1.addExit(null); // mop and broom wall (No door)
        closet1.addExit(hallway2); // South exit of closet1 leads to the hallway2
        closet1.addExit(null); // sponges and rags wall (No door)

        // closet1 door setup
        closet1.addDoor(null); // Wall (No door)
        closet1.addDoor(null); // Wall (No door)
        closet1.addDoor(defaultDoor); // South door is a defaultDoor
        closet1.addDoor(null); // Wall (No door)

        // closet2 exits setup
        closet2.addExit(null); // hand-held tool wall (No door)
        closet2.addExit(null); // power tool wall (No door)
        closet2.addExit(hallway4); // South exit of closet2 leads to the hallway4
        closet2.addExit(null); // oak 2x4 wall (No door)

        // closet2 door setup
        closet2.addDoor(null); // Wall (No door)
        closet2.addDoor(null); // Wall (No door)
        closet2.addDoor(defaultDoor); // South door is a defaultDoor
        closet2.addDoor(null); // Wall (No door)

        // closet3 exits setup
        closet3.addExit(null); // wolf cloaks Wall (No door)
        closet3.addExit(null); // bear cloaks Wall (No door)
        closet3.addExit(null); // moose cloaks Wall (No door)
        closet3.addExit(hallway5); // oak 2x4 wall (No door)

        // closet3 door setup
        closet3.addDoor(null); // Wall (No door)
        closet3.addDoor(null); // Wall (No door)
        closet3.addDoor(null); // Wall (No door)
        closet3.addDoor(defaultDoor); // West door is a defaultDoor

        // tortureChamber exits setup
        tortureChamber.addExit(guardRoom); // North exit of tortureChamber leads to the guardRoom
        tortureChamber.addExit(hallway1); // East exit of tortureChamber leads to the hallway2
        tortureChamber.addExit(hallway3); // South exit of tortureChamber leads to the hallway3
        tortureChamber.addExit(hallway2); // West exit of tortureChamber leads to the hallway2

        // tortureChamber door setup
        tortureChamber.addDoor(defaultDoor); // North door is a defaultDoor
        tortureChamber.addDoor(defaultDoor); // East door is a defaultDoor
        tortureChamber.addDoor(defaultDoor); // South door is a defaultDoor
        tortureChamber.addDoor(defaultDoor); // West door is a defaultDoor

        // Guard Room exits setup
        guardRoom.addExit(null); // Wall (No door)
        guardRoom.addExit(null); // Wall (No door)
        guardRoom.addExit(tortureChamber); // South exit of guardRoom leads to the tortureChamber
        guardRoom.addExit(null); // Wall (No door)

        // Guard Room door setup
        guardRoom.addDoor(null); // Wall (No door)
        guardRoom.addDoor(null); // Wall (No door)
        guardRoom.addDoor(defaultDoor); // South door is a defaultDoor
        guardRoom.addDoor(null); // Wall (No door)

        // Mess Hall exits setup
        messHall.addExit(null); // Wall (No door)
        messHall.addExit(hallway5); // East exit of the messHall leads to the hallway1
        messHall.addExit(kitchen); // South exit of the messHall leads to the kitchen
        messHall.addExit(courtYard); // Wall (No door)

        // Mess Hall door setup
        messHall.addDoor(null); // Wall (No door)
        messHall.addDoor(defaultDoor); // East door is a defaultDoor
        messHall.addDoor(lockedDoor); // South door is a defaultDoor
        messHall.addDoor(defaultDoor); // Wall (No door)

        // hallway1 exits setup
        hallway1.addExit(dungeon2); // North exit of hallway1 leads to dungeon2
        hallway1.addExit(dungeon1); // East exit of hallway1 leads to dungeon1
        hallway1.addExit(dungeon3); // South exit of hallway1 leads to dungeon3
        hallway1.addExit(tortureChamber); // West exit of hallway1 leads to tortureChamber

        // hallway1 door setup
        hallway1.addDoor(defaultDoor); // North door is a defaultDoor
        hallway1.addDoor(defaultDoor); // East door is a defaultDoor
        hallway1.addDoor(defaultDoor); // South door is a defaultDoor
        hallway1.addDoor(defaultDoor); // West door is a defaultDoor

        // hallway2 exits setup
        hallway2.addExit(closet1); // North exit of hallway2 leads to closet1
        hallway2.addExit(tortureChamber); // East exit of hallway2 leads to tortureChamber
        hallway2.addExit(armory); // South exit of hallway2 leads to armory
        hallway2.addExit(bathRoom); // West exit of hallway2 leads to bathRoom

        // hallway2 door setup
        hallway2.addDoor(defaultDoor); // North door is a defaultDoor
        hallway2.addDoor(defaultDoor); // East door is a defaultDoor
        hallway2.addDoor(redDoor); // South door is redDoor
        hallway2.addDoor(defaultDoor); // West door is a defaultDoor

        // hallway3 exits setup
        hallway3.addExit(tortureChamber); // North exit of hallway3 leads to tortureChamber
        hallway3.addExit(null); // Wall (No door)
        hallway3.addExit(courtYard); // South exit of hallway3 leads to courtYard
        hallway3.addExit(trapRoom); // Wall (No door)

        // hallway3 door setup
        hallway3.addDoor(defaultDoor); // North door is a defaultDoor
        hallway3.addDoor(null); // Wall (No door)
        hallway3.addDoor(defaultDoor); // South door is a defaultDoor
        hallway3.addDoor(defaultDoor); // Wall (No door)

        // hallway4 exits setup
        hallway4.addExit(closet2); // North exit of hallway4 leads to closet2
        hallway4.addExit(courtYard); // East exit of hallway4 leads to courtYard
        hallway4.addExit(office); // South exit of hallway4 leads to office
        hallway4.addExit(null); // Wall (No door)

        // hallway4 door setup
        hallway4.addDoor(defaultDoor); // North door is a defaultDoor
        hallway4.addDoor(defaultDoor); // East door is a defaultDoor
        hallway4.addDoor(blueDoor); // South door is a blueDoor
        hallway4.addDoor(null); // Wall (No door)

        // hallway5 exits setup
        hallway5.addExit(trapRoom); // Wall (No door)
        hallway5.addExit(closet3); // East exit of hallway5 leads to closet3
        hallway5.addExit(barracks); // South exit of hallway5 leads to barracks
        hallway5.addExit(messHall); // West exit of hallway5 leads to messHall

        // hallway5 door setup
        hallway5.addDoor(defaultDoor); // Wall (No door)
        hallway5.addDoor(defaultDoor); // East door is a defaultDoor
        hallway5.addDoor(defaultDoor); // South door is a defaultDoor
        hallway5.addDoor(defaultDoor); // West door is a defaultDoor

        // Office exits setup
        office.addExit(hallway4); // North exit of office leads to hallway4
        office.addExit(null); // Wall (No door)
        office.addExit(null); // Wall (No door)
        office.addExit(null); // Wall (No door)

        // Office door setup
        office.addDoor(blueDoor); // North door is a blueDoor
        office.addDoor(null); // Wall (No door)
        office.addDoor(null); // Wall (No door)
        office.addDoor(null); // Wall (No door)

        // Armory exits setup
        armory.addExit(hallway2); // Wall (No door)
        armory.addExit(null); // East exit of armory leads to hallway4
        armory.addExit(null); // Wall (No door)
        armory.addExit(null); // Wall (No door)

        // Armory door setup
        armory.addDoor(redDoor); // Wall (No door)
        armory.addDoor(null); // East door is a redDoor
        armory.addDoor(null); // Wall (No door)
        armory.addDoor(null); // Wall (No door)

        // dungeon1 exits setup
        dungeon1.addExit(cell); // North exit of dungeon1 leads to cell
        dungeon1.addExit(cave); // East exit of dungeon1 leads to cave
        dungeon1.addExit(debug); // South exit of dungeon1 leads to debug
        dungeon1.addExit(hallway1); // West exit of dungeon1 leads to hallway1

        // dungeon1 door setup
        dungeon1.addDoor(cellDoor); // North door is a cellDoor
        dungeon1.addDoor(hole); // East door is a hole
        dungeon1.addDoor(defaultDoor); // South door is a defaultDoor
        dungeon1.addDoor(defaultDoor); // East door is a defaultDoor

        // dungeon2 exits setup
        dungeon2.addExit(null); // Wall (No door)
        dungeon2.addExit(null); // Wall (No door)
        dungeon2.addExit(hallway1); // South exit of dungeon2 leads to hallway1
        dungeon2.addExit(null); // Wall (No door)

        // dungeon2 door setup
        dungeon2.addDoor(null); // Wall (No door)
        dungeon2.addDoor(null); // Wall (No door)
        dungeon2.addDoor(defaultDoor); // South door is a defaultDoor
        dungeon2.addDoor(null); // Wall (No door)

        // dungeon3 exits setup
        dungeon3.addExit(hallway1); // North exit of dungeon3 leads to hallway1
        dungeon3.addExit(null); // Wall (No door)
        dungeon3.addExit(null); // Wall (No door)
        dungeon3.addExit(null); // Wall (No door)

        // dungeon3 door setup
        dungeon3.addDoor(defaultDoor); // North door is a defaultDoor
        dungeon3.addDoor(null); // Wall (No door)
        dungeon3.addDoor(null); // Wall (No door)
        dungeon3.addDoor(null); // Wall (No door)

        // Cave exits setup
        cave.addExit(null); // Cave wall (No door)
        cave.addExit(outside); // East exit of cave leads to outside
        cave.addExit(null); // Cave wall (No door)
        cave.addExit(dungeon1); // East exit of cave leads to dungeon1

        // Cave door setup
        cave.addDoor(null); // Cave wall (No door)
        cave.addDoor(blocked); // East door is a path
        cave.addDoor(null); // Cave wall (No door)
        cave.addDoor(hole); // West door is a hole

        // Outside area exits setup
        outside.addExit(banditCamp); // North exit of outside leads to banditCamp
        outside.addExit(null); // Forrest (No door)
        outside.addExit(inn); // South exit of outside leads to inn
        outside.addExit(cave); // West exit of outside leads to cave

        // Outside area door setup
        outside.addDoor(path); // North door is a hole
        outside.addDoor(null); // Forrest (No door)
        outside.addDoor(path); // South door is a defaultDoor
        outside.addDoor(path); // West door is a path

        // [ DELETE THIS WHEN WE ARE DONE ]
        // Debug Room exits setup
        debug.addExit(dungeon1); // Exit to the dungeon1 from the Debug Room
        debug.addExit(null);    // Wall (No door)
        debug.addExit(null);    // Wall (No door)
        debug.addExit(null);    // Wall (No door)

        // Debug Room door setup
        debug.addDoor(defaultDoor); // Door leading to another area from the Debug Room
        debug.addDoor(null); // Wall (No door)        
        debug.addDoor(null); // Wall (No door)       
        debug.addDoor(null); // Wall (No door)       

        // ADD ITEMS TO EACH LOCATION
        cell.addItem(shim); // Add the 'shim' item to the 'cell'
        tortureChamber.addItem(bloodyTool); // Add the 'bloodyTool' item to the 'tortureChamber'

        armory.addItem(sword); // Add the 'sword' item to the 'armory'
        armory.addItem(mace); // Add the 'mace' item to the 'armory'
        armory.addItem(axe); // Add the 'axe' item to the 'armory'
        armory.addItem(broadSword); // Add the 'broadSword' item to the 'armory'

        for(int i = 0; i < 3; i++) {
            kitchen.addItem(food); // Add the 'food' item to the 'messHall'
        }

        // ADD ITEMS TO EACH CHARACTER
        tomas.addItem(snack);   // Give the 'tomas' character a 'snack' item
        tomas.addItem(fists);    // Give the 'tomas' character a 'fist' item

        player.addItem(fists);

        goblin.addItem(snack);  // Give the 'goblin' character a 'snack' item
        goblin.addItem(pickaxe);  // Give the 'goblin' character a 'pickaxe' item

        guard.addItem(snack);   // Give the 'guard' character a 'snack' item
        guard.addItem(sword);   // Give the 'guard' character a 'sword' item

        // ADD CHARACTERS TO LOCATIONS
        cell.addCharacter(tomas);    // Add 'tomas' character to the 'cell'
        cave.addCharacter(goblin);   // Add 'goblin' character to the 'cave'


        currentLocation = cell;      // Set the initial current location as the 'cell'

        // Define potential spawn locations for guards
        guardSpawnLocation.add(hallway1); // hallway1 as a potential guard spawn location
        guardSpawnLocation.add(hallway2); // hallway2 as a potential guard spawn location
        guardSpawnLocation.add(hallway3); // hallway3 as a potential guard spawn location
        guardSpawnLocation.add(hallway4); // hallway4 as a potential guard spawn location
        guardSpawnLocation.add(hallway5); // hallway5 as a potential guard spawn location
        guardSpawnLocation.add(messHall); // messHall as a potential guard spawn location
        guardSpawnLocation.add(office); // office as a potential guard spawn location
        guardSpawnLocation.add(kitchen); // kitchen as a potential guard spawn location
        guardSpawnLocation.add(courtYard); // tortureChamber as a potential guard spawn location
        guardSpawnLocation.add(bathRoom); // bathRoom as a potential guard spawn location

        // Define potential spawn location for guards
        shimSpawnLocation.add(hallway1); // hallway1 as a potential shim spawn location
        shimSpawnLocation.add(hallway2); // hallway2 as a potential shim spawn location
        shimSpawnLocation.add(hallway3); // hallway3 as a potential shim spawn location
        shimSpawnLocation.add(hallway4); // hallway4 as a potential shim spawn location
        shimSpawnLocation.add(hallway5); // hallway5 as a potential shim spawn location
        shimSpawnLocation.add(messHall); // messHall as a potential shim spawn location
        shimSpawnLocation.add(dungeon1); // dungeon1 as a potential shim spawn location
        shimSpawnLocation.add(dungeon2); // dungeon2 as a potential shim spawn location
        shimSpawnLocation.add(dungeon3); // dungeon3 as a potential shim spawn location
        shimSpawnLocation.add(office); // office as a potential shim spawn location
        shimSpawnLocation.add(kitchen); // kitchen as a potential shim spawn location
        shimSpawnLocation.add(courtYard); // tortureChamber as a potential shim spawn location
        shimSpawnLocation.add(bathRoom); // bathRoom as a potential shim spawn location
        shimSpawnLocation.add(tortureChamber); // tortureChamber as a potential shim spawn location
        shimSpawnLocation.add(barracks); // barracks as a potential shim spawn location
        shimSpawnLocation.add(closet1); // closest1 as a potential shim spawn location
        shimSpawnLocation.add(closet2); // closest2 as a potential shim spawn location
        shimSpawnLocation.add(closet3); // closest3 as a potential shim spawn location

        // Place the keys in their rooms
        office.addItem(greenKey);
        barracks.addItem(blueKey);

        int redKeySpawn = randy.nextInt(6)+1;
        switch (redKeySpawn) {
            case 1 :
                tortureChamber.addItem(redKey);
                break;
            case 2 :
                hallway2.addItem(redKey);
                break;
            case 3 :
                closet1.addItem(redKey);
                break;
            case 4 :
                bathRoom.addItem(redKey);
                break;
            case 5 :
                hallway3.addItem(redKey);
                break;
            case 6 :
                courtYard.addItem(redKey);
                break;
        }

        //debugging [ REMOVE DEBUG ROOM WHEN WE FINISH THE GAME ]
        debug.addItem(sword);
        debug.addItem(broadSword);
        debug.addItem(axe);
        debug.addItem(mace);
        debug.addItem(food);
        debug.addItem(blueKey);
        debug.addItem(redKey);
        debug.addItem(greenKey);
        debug.addItem(shim);
        debug.addItem(shim);
        debug.addItem(snack);
    }
}