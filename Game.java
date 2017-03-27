import java.util.HashMap;
import java.util.Set;
import java.util.Random;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Gavin Kyte and Aaron Chauvette
 * @version 2016.03.22
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private HashMap<String, Room> map;
    private Player hero;
    private Player villain;
    private Random rng;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game(){
        map = new HashMap<>();
        prepareRooms();
        parser = new Parser();
        hero = new Player("Hero", "Patient Care");
        villain = new NPC("Villain", "Basement");
        currentRoom = map.get(hero.getRoom());
        rng = new Random();
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play(){            
        printWelcome();
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished){
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing. Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome(){
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command){
        boolean wantToQuit = false;

        if(command.isUnknown()){
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        switch (commandWord){
            case "help":
                printHelp();
                break;
            case "go":
                goRoom(command);
                moveNPC();
                break;
            case "back":
                goBack(hero);
                moveNPC();
                break;
            case "take":
                pickUp(hero, command);
                break;
            case "drop":
                putDown(hero, command);
                break;
            case "look":
                look(hero);
                break;    
            case "inventory":
                System.out.println(hero.getInventory());
                break;
			case "unlock":
				changeLockTo(hero, command, false);
				break;
			case "lock":
				changeLockTo(hero, command, true);
				break;
            case "quit":
                wantToQuit = quit(command);
                break;
            default:
                break;
        }
        
        // else command not recognised.
        return wantToQuit;
    }
    
    // Defining Commands    
    /**
     * Returns player to the previous room. This means that if this
     * is called each turn, the player will alternate between two rooms
     * DANGER: This currently would bypass locked doors and trapdoors as
     * it goes directly to the room without checking if it can be entered.
     */
    private void goBack(Player player){
        currentRoom = map.get(player.goBack());
        System.out.println(currentRoom.getShortDescription());
    }
    
    /**
     * Prints the current room's exits and items
     */
    private void look(Player player){
        currentRoom = map.get(player.getRoom());
        System.out.println(currentRoom.look());
    }
    
    
    // implementations of user commands:
    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp(){
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the hospital.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command){
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        currentRoom = map.get(hero.getRoom());
        Room nextRoom = currentRoom.getExit(direction);
        
        // Casting room as correct type
        if (nextRoom instanceof LockedRoom) { nextRoom = (LockedRoom)nextRoom;}        
        
        
        
        if (nextRoom == null) {
            System.out.println("There is no door!");
        } else if (nextRoom.meetsRequirements()) {
            currentRoom = nextRoom;
            hero.setRoom(currentRoom.getTitle());
            System.out.println(currentRoom.getLongDescription());
        } else {
            System.out.println("You cannot enter this room");
            System.out.println(currentRoom.getRequirements());
        }
    }
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command){
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /**
     * Picks up an item. I.e. removes it from a the current room if it exists and adds
     * it to the player's inventory.
     * @param Player, Command as the item name
     */
    private void pickUp(Player player, Command command){
        currentRoom = map.get(player.getRoom());
        String itemName = command.getSecondWord();
        
        if (currentRoom.contains(itemName)) {            
            double itemWeight = currentRoom.getItem(itemName).getWeight();
            double carryLimit = player.getWeightLimit();
            boolean canTake = currentRoom.getItem(itemName).canTake();
            if (canTake && (itemWeight <= carryLimit)){
                player.addItem(currentRoom.remove(itemName));
                System.out.println("You took " + itemName);
            } else if (!canTake) {
                System.out.println("This item can't be picked up");
            } else {
                System.out.println("This item is too heavy for you to pick up right now.");
            }
        } else {
            System.out.println("There is no " + itemName + " here.");
        }
    }
    
    /**
     * Puts down an item in the current room and takes it out of inventory.
     * @param Player, Command as the item name
     */
    private void putDown(Player player, Command command){
        currentRoom = map.get(player.getRoom());
        String itemName = command.getSecondWord();        
        if (player.has(itemName)) {
            currentRoom.store(player.drop(itemName));
            System.out.println("You dropped " + itemName);
        } else {
            System.out.println("You don't have \"" + itemName + "\" in your inventory.");
        }
    }
    
    /**
     * Unlocks room if it needs to be unlocked and next to player
     */
    private void changeLockTo(Player player, Command command, boolean locking){
        if (!command.hasSecondWord()){
            System.out.println("Unlock what?");
            return;
        } 
        		        
        currentRoom = map.get(player.getRoom());
		Room chosenRoom = currentRoom.getExit((command.getSecondWord()));
        boolean isNeighbor = false;
		boolean playerHasKey = false;
        if (chosenRoom == null) {
            System.out.println("There's no room to unlock there!");
            return;
        } else if (! (chosenRoom instanceof LockedRoom)) {
			System.out.println("This is not a locked room");
			return;
        }

		LockedRoom lockedRoom = (LockedRoom)chosenRoom;
        isNeighbor = true;
		String keyNeeded = lockedRoom.getKey();
        playerHasKey = player.has(keyNeeded);
		
		if (isNeighbor && playerHasKey){
			if (!locking){System.out.println(lockedRoom.unlock());
			} else {System.out.println(lockedRoom.lock());}
		}
    }
    
    
    /**
     * Moves each NPC in the Map into an adjacent room
     */
    private void moveNPC(){
        // Going to make this choice random eventually
        // DANGER!! This could be root of weird error down the line involving currentRoom
        currentRoom = map.get(villain.getRoom());
        String[] exits = currentRoom.getExitDirections();
        String randomDirection = exits[rng.nextInt(exits.length)];
        
        Room nextRoom = currentRoom.getExit(randomDirection);
        villain.setRoom(nextRoom.getTitle());
        System.out.println("Villain is in " + villain.getRoom());
        // If we need to reset current room, this next line will do so
        //currentRoom = map.get(hero.getRoom());
    }
    
    
    /**
     * Creates rooms and items, then sets up the exits for each room and stores the items
     */    
    private void prepareRooms(){
        // Creating rooms
        Room patientCare, basement, cafe, office, bathroom, middleStall;
        patientCare = new Room("Patient Care", "A room with curtains surrounding several beds.\n"
            + "The bed in the far corner has some sheets sticking out under the curtain.");
        basement = new Room("Basement", "A dusty cement enclosure with strange machines\n"
            + "and boxes scattered about.");
        cafe = new Room("Cafe", "A open space filled with tables and surrounded\n" 
            + "with boothes that once served food. You smell something rancid.");
        office = new LockedRoom("Office", "A small space with file cabinets lining the back wall,\n" 
            + "there is a desk to your right.", true);
        bathroom = new Room("Bathroom", "A pristine white room that reeks of bleach.\n"
            + "All of the stalls are closed except one in the middle");
        middleStall = new Room("Bathroom Stall", "A dead body is slumped over in the stall.\n"
            + "At his feet is an open jug of bleach. He does not appear to be breathing.");
        
        // Creating Items
        Item officeKey, bleach, bigRock;
        officeKey = new Item("key", 0.1, true);
        bleach = new Item("bleach", 12.00, true);
        bigRock = new Item("rock", 99.00, true);
                            
        // Setting up exits and items between rooms:        
        // Patient Care
        patientCare.setExit("west", cafe);
        patientCare.setExit("south", bathroom);
        // Bathroom
        bathroom.setExit("north", patientCare);
        bathroom.setExit("east", middleStall);
        middleStall.setExit("west", bathroom);
        middleStall.store(bleach);
        // Cafe
        cafe.setExit("down", basement);
        cafe.setExit("east", patientCare);
        cafe.setExit("south", office);
        // Office
        office.setExit("north", cafe);
        // Basment
        basement.setExit("up", cafe);
        basement.store(officeKey);
        basement.store(bigRock);
        
        
        // Adding rooms to map
        Room[] rooms = {patientCare, basement, cafe, office, bathroom, middleStall};
        for (Room room : rooms){
            map.put(room.getTitle(), room);
        }
    }
}

