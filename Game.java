import java.util.HashMap;
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
    private Random randomGenerator;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        map = new HashMap<>();
        createRooms();
        parser = new Parser();
        hero = new Player("Hero", "Patient Care");
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing. Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
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
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    /**
     * The AI moves and the environment may change
     * i.e. the descriptions and presence of items may change
     */
    private void npcTurn() {
        
    }
    
    // implementations of user commands:
    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
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
    private void goRoom(Command command) 
    {
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
        }
        else {
            currentRoom = nextRoom;
            hero.setRoom(currentRoom.getTitle());
            System.out.println(currentRoom.getLongDescription());
            // moveNPCS();
        }
    }

    /**
     * Moves each NPC in the Map into an adjacent room
     *
    private void moveNPCS(){
        // Going to make this choice random eventually
        
        
        Room nextRoom = ;
    }
    */
   
   
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
    
    private void createRooms(){
        Room patientCare, basement, cafe, office;
        // Creating rooms
        patientCare = new Room("Patient Care", "A room with curtains surrounding several beds.");
        basement = new Room("Basement", "A dusty cement enclosure with strange machines and boxes scattered about.");
        cafe = new Room("Cafe", "A open space filled with table and surrounded with boothes that once served food. You smell something rancid.");
        office = new Room("Office", "A small space with file cabinets lining the back wall, there is a desk to your right.");
        
        // Setting up exit between rooms
        patientCare.setExit("west", cafe);
        cafe.setExit("down", basement);
        cafe.setExit("east", patientCare);
        cafe.setExit("south", office);        
        office.setExit("north", cafe);
        basement.setExit("up", cafe);
        
        // Adding rooms to map
        Room[] rooms = {patientCare, basement, cafe, office};
        for (Room room : rooms){
            map.put(room.getTitle(), room);
        }
    }
}

