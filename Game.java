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
    private Random randomGenerator;
        
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
        randomGenerator = new Random();
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
                break;
            case "back":
                goBack(hero);
                break;
            case "take":
                pickUp(hero, command);
                break;
            case "drop":
                putDown(hero, command);
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

        if (nextRoom == null) {
            System.out.println("There is no door!");
        } else {
            currentRoom = nextRoom;
            hero.setRoom(currentRoom.getTitle());
            System.out.println(currentRoom.getLongDescription());
            moveNPC();
        }
    }
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
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
    private void pickUp(Player character, Command command){
        currentRoom = map.get(character.getRoom());
        String itemName = command.getSecondWord();
        character.addItem(currentRoom.remove(itemName));
    }
    
    /**
     * Puts down an item in the current room and takes it out of inventory.
     * @param Player, Command as the item name
     */
    public void putDown(Player character, Command command){
        currentRoom = map.get(character.getRoom());
        String itemName = command.getSecondWord();
        currentRoom.store(character.drop(itemName));
    }
    
    /**
     * Moves each NPC in the Map into an adjacent room
     */
    private void moveNPC(){
        // Going to make this choice random eventually
        // DANGER!! This could be root of weird error down the line involving currentRoom
        currentRoom = map.get(villain.getRoom());
        String[] exits = currentRoom.getExitDirections();
        String randomDirection = exits[randomGenerator.nextInt(exits.length)];
        
        Room nextRoom = currentRoom.getExit(randomDirection);
        villain.setRoom(nextRoom.getTitle());
        currentRoom = map.get(hero.getRoom());
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
        office = new Room("Office", "A small space with file cabinets lining the back wall,\n" 
            + "there is a desk to your right.");
        bathroom = new Room("Bathroom", "A pristine white room that reeks of bleach.\n"
            + "All of the stalls are closed except one in the middle");
        middleStall = new Room("Bathroom Stall", "A dead body is slumped over in the stall.\n"
            + "At his feet is an open jug of bleach. He does not appear to be breathing.");
        
        // Creating Items
        Item officeKey, bleach;
        officeKey = new Item("Office Key", 0, true);
        bleach = new Item("Bleach", 4, true);
                    
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
        
        // Adding rooms to map
        Room[] rooms = {patientCare, basement, cafe, office, bathroom};
        for (Room room : rooms){
            map.put(room.getTitle(), room);
        }
    }
}

