import java.util.HashMap;
import java.util.Set;
import java.util.Random;
import java.io.*;
import java.util.Scanner;

/**
 *  This is the main class of Zuul, a test-based adventure game.
 *  It is adapted from a basic version out of this book:
 *  
 *     Objects First with Java - A Practical Introduction using BlueJ
 *     Sixth edition
 *     David J. Barnes and Michael Kölling
 *     Pearson Education, 2016
 *  
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Gavin Kyte and Aaron Chauvette
 * @version 2017.3.27
 */

public class Game
{
    private Parser parser;
    private Room currentRoom;
    private HashMap<String, Room> map;
    private HashMap<String, Player> characters;
    private Player hero;
    private NPC villain;
    private Random rng;
    private Scanner scanner;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() throws IOException {
        map = new HashMap<>();
        characters = new HashMap<>();
        scanner = new Scanner(new BufferedReader(new FileReader("worldInit.txt"))).useDelimiter("\\n");
        //prepareRooms();
        makeCharacters();
        initWorld();
        parser = new Parser();
        currentRoom = map.get(hero.getRoom());
        rng = new Random();
    }

    //Test
    public void initWorld(){
        while (scanner.hasNext()) {
            String commandLine = scanner.next();
            Scanner line = new Scanner(commandLine);
            String command = "no command on line";
            if (line.hasNext()) {
                command = line.next();
            }
            
            switch (command){
                case "//":
                    break;
                case "room":
                    // room command format
                    // room _typeOfRoom_ _title_ _shortDescription_
                    String typeOfRoom = "room";
                    String title = "defaultTitle";
                    String key = "key";
                    String description = "";                    
                    if (line.hasNext()){
                        typeOfRoom = line.next();                        
                    } else {
                        System.out.println("\"room\" command during init is missing room type");
                    }                    
                    if (line.hasNext()){
                        title = line.next();
                    } else {
                        System.out.println("\"room\" command during init is missing a title");
                    }
                    if ((typeOfRoom.equals("locked") || typeOfRoom.equals("unlocked")) && line.hasNext()){
                        key = line.next();
                    } else {
                        // nothing should happen here
                    }
                    while (line.hasNext()){
                        description += line.next() + " ";
                    }
                    
                    Room r = createRoom(typeOfRoom, title, key, description);                    
                    if (map.get(r.getTitle()) == null) {
                        map.put(r.getTitle(), r);
                    } else {
                        System.out.println("Room already exists. Please remove the copy");
                    }
                    break;
                    
                case "exit":
                    // format for exit command
                    // exit _mainRoomName_ [_direction_ _roomName_ ...]
                    String roomName = "Default";
                    if (line.hasNext()) {
                        roomName = line.next();
                    } else {
                        System.out.println("Unfinished command \"exit,\" please finish line and try again.");
                        break;
                    }
                    
                    Room mainRoom = map.get(roomName);
                    if (mainRoom == null){
                        System.out.println("On command \"exit\" during init, the mainRoom is null.");
                        System.out.println("Not setting exits for " + roomName + " because it hasn't been created.");
                        break;
                    }                    
                    while (line.hasNext()){
                        String direction = line.next();
                        roomName = "DefaultExit";
                        if (line.hasNext()){
                            roomName = line.next();
                            Room exitRoom = map.get(roomName);
                            if (roomName != null) {
                                mainRoom.setExit(direction, exitRoom);
                            } else {
                                System.out.println("On command \"exit\" during init, exit room wasn't found.");
                            }
                        } else {
                            System.out.println("On command \"exit\" during init, the exit room wasn't provided");
                        }                        
                    }                    
                    break;
                    
                case "add":
                    // add command format
                    // add _type_ _name_ _weight_ _canTake_ _<powerLevel>_ _destination_
                    String type = "item";
                    String name = "defaultName";
                    double weight = 0.00;
                    boolean canTake = true;
                    double powerLevel = 0.00;
                    String destination = null;
                    if (line.hasNext()){
                        type = line.next();
                    } else {
                        System.out.println("\"add\" command during init is missing a type");
                    }
                    if (line.hasNext()){
                        name = line.next();
                    } else {
                        System.out.println("\"add\" command during init is missing a name");
                    }
                    if (line.hasNextDouble()){
                        weight = line.nextDouble();
                    } else {
                        System.out.println("\"add\" command during init is missing a double for weight");
                    }
                    if (line.hasNextBoolean()){
                        canTake = line.nextBoolean();
                    } else {
                        System.out.println("\"add\" command during init is missing a boolean for canTake");
                    }
                    if (line.hasNextDouble() && type.equals("weapon")){
                        powerLevel = line.nextDouble();
                    } else {
                        // nothing should happen here
                    }
                    if (line.hasNext()){
                        destination = line.next();
                    } else {
                        System.out.println("\"add\" command during init is missing a destination name");
                    }
                    
                    Item i = createItem(type, name, weight, canTake, powerLevel);
                    if (map.get(destination) != null) {
                        map.get(destination).store(i);
                    } else {
                        System.out.println(i.getName() + " cannot be stored because " + destination + "doesn't exist");
                    }                    
                    break;
                    
                case "no command on line":
                    break;
                    
                default:
                    System.out.println("Command not recognized in init file");
                    break;
            }
        }
    }
    
    /**
     * Displays map and room items
     */
    public void displayMapFull(){
        System.out.println("MAP");
        System.out.println("-----------------------------");
        for (Room r : map.values()){
            String exits = "Exits: ";
            for (String direction : r.getExitDirections()){
                exits += direction + " ";
            }
            System.out.println("-----------------------------");
            System.out.println(r.getTitle());
            System.out.println("    " + exits);
            System.out.println("    " + r.listItems());
        }
        System.out.println("-----------------------------");
        System.out.println("-----------------------------");
    }
    
    /**
     * Casts room into correct type
     */
    public Room createRoom(String type, String title, String key, String description){
        Room r = null;
        if (type.equals("locked")){
            r = new LockedRoom(title, description, true);
        } else if (type.equals("unlocked")){
            r = new LockedRoom(title, description, false);
        } else {
            r = new Room(title, description);
        }
        
        if (r instanceof LockedRoom){
            LockedRoom room = (LockedRoom)r;
            room.setKey(key);
            return room;
        } else {
            return r;
        }
    }
    
    /**
     * Creates a specific type of item, necessary during init to avoid code duplication
     * @param String type, String name, double weight, boolean canTake, double powerLevel
     */
    private Item createItem(String type, String name, double weight, boolean canTake, double powerLevel){
        Item i = null;
        if (type.equals("weapon")){
            i = new Weapon(name, weight, canTake, powerLevel);
        } else {
            i = new Item(name, weight, canTake);
        }
        
        if (i instanceof Weapon){
            Weapon item = (Weapon)i;
            return item;
        } else {
            return i;
        }
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
                moveNPC();
                break;
            case "lock":
                changeLockTo(hero, command, true);
                moveNPC();
                break;
            case "hide":
                hide(hero);
                moveNPC();
                break;
            case "fight":
                fight(hero, command);
                // When fight determines GameOver, uncomment this:
                // wantToQuit = fight(hero, command);
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
     * Prints the current room's exit directions and items contained
     */
    private void look(Player player){
        currentRoom = map.get(player.getRoom());
        System.out.println(currentRoom.look());
    }
    
    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp(){
        System.out.println("You are lost. You are alone.");
        System.out.println("You wander around the hospital.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * @param Command user input for chosen room
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
     * Hides the player from the other characters. If the room supports hiding,
     * the player won't get into a fight with them while they are hiding.
     * Looking for a place to hide does let the NPC move though, so if the
     * room doesn't have any place, the player may be found.
     * @param Player usually main character
     */
    private void hide(Player player){
        System.out.println(player.getName() + player.hide());
    }    
    private void unhide(Player player){
        System.out.println(player.getName() + player.unhide());
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
        } else {
            return true;  // signal that we want to quit
        }
    }
    
    /**
     * The player has won if he meets all the conditions.
     * Prints out the winning message and ends the play loop.
     * @return boolean quit the game
     */
    private boolean win(){
        // Not sure when this is triggered or when it will meet the right conditions
        return true;
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
     * Locks or unlocks the room if it is next to player
     * @param Player main character usually, Command user input, boolean "Are you locking room?"
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
     * Puts the given characters into a fight scene
     * @param Player the main character, Command contains the player to fight
     */
    private void fight(Player player, Command command){
        if (!command.hasSecondWord()){
            System.out.println("Fight who?");
            return;
        }
        
        String opponentName = command.getSecondWord();
        NPC opponent = (NPC)characters.get(opponentName);
        if (characters.get(opponentName) == null) {
            System.out.println("You cannot fight \"" + opponentName + "\" here.");
            return;
        } else if (opponent != player){
            triggerFight(player, opponent);
        }
    }
    
    /**
     * Checks whether the hero has encountered a killer and triggers a fight if so.
     * @param Player being controlled
     */
    private void checkFightTrigger(Player player){
        String playerRoom = player.getRoom();
        for (String name : characters.keySet()){
            Player character = characters.get(name);
            if (character instanceof NPC) {
                // This accounts for the character being the hero (a Player)
                NPC opponent = (NPC)character;
                boolean inSameRoom = playerRoom.equals(opponent.getRoom());
                if (inSameRoom && opponent.isAggro()) {triggerFight(player, opponent);}
            }
        }
    }
    
    /**
     * Triggers fight with the two parameters given. Usually the Player and NPC
     * @Param Player the hero/MC, NPC the villain or interactable friendly
     */
    private void triggerFight(Player player, NPC opponent){
        FightScene fight = new FightScene();
        fight.killerEncounter(player, opponent);
    }
    
    /**
     * Moves each NPC in the Map into an adjacent room if it is available
     */
    private void moveNPC(){
        // Going to make this choice random eventually
        // DANGER!! This could be root of weird error down the line involving currentRoom
        currentRoom = map.get(villain.getRoom());
        String[] exits = currentRoom.getExitDirections();
        if (exits.length == 0) {System.out.println("Villain has been trapped"); return;}
        
        int choice = rng.nextInt(exits.length);
        String randomDirection = exits[choice];
        
        Room nextRoom = currentRoom.getExit(randomDirection);
        if (nextRoom != null && nextRoom.meetsRequirements()) {
            villain.setRoom(nextRoom.getTitle());
        } else {
            // Villain can't get in room and is yelling
            System.out.println("You hear someone yelling in frustration somewhere nearby.");
        }
        
        // For testing, shows where villain is
        System.out.println("Villain is in " + villain.getRoom());
        // If we need to reset current room, this next line will do so
        //currentRoom = map.get(hero.getRoom());
        checkFightTrigger(hero);
    }
    
    /**
     * Makes the characters involved in the game
     * This includes the hero and villain, but can have side characters as well.
     */
    private void makeCharacters(){
        hero = new Player("Hero", "Lobby");
        villain = new NPC("Villain", "Basement", true);
        
        characters.put(hero.getName(), hero);
        characters.put(villain.getName(), villain);
    }    
    
    /**
     * Creates rooms and items, then sets up the exits for each room and stores the items
     */    
    private void prepareRooms(){
        // Creating rooms
        Room patientCare, basement, cafe, office, bathroom, middleStall, powerPlant, roof, breakRoom, elevator, mainOffice, ER, janitorCloset, kitchen, courtYard, giftShop, parkingLot, lobby, lab, XRay, hallway, stairs, mainEntrance;
        patientCare = new Room("Patient Care", "A room with curtains surrounding several beds.\n"
            + "The bed in the far corner has some sheets sticking out under the curtain.");
        basement = new Room("Basement", "A dusty cement enclosure with strange machines\n"
            + "and boxes scattered about.");
        cafe = new Room("Cafe", "A open space filled with tables and surrounded\n" 
            + "with booths that once served food. You smell something rancid.");
        office = new LockedRoom("Office", "A small space with file cabinets lining the back wall,\n" 
            + "there is a desk to your right.", true);
        bathroom = new Room("Bathroom", "A pristine white room that reeks of bleach.\n"
            + "All of the stalls are closed except one in the middle");
        middleStall = new Room("Bathroom Stall", "A dead body is slumped over in the stall.\n"
            + "At his feet is an open jug of bleach. He does not appear to be breathing.");
        roof = new Room("Roof top", "description");
        powerPlant = new Room("Power Plant", "description");
        breakRoom = new Room("Break Room", "description");
        elevator = new LockedRoom("Elevator", "description", true);
        mainOffice = new Room("Main Office", "description");
        ER = new Room("ER", "description");
        janitorCloset = new Room("Janitor's Closet", "description");
        courtYard = new Room("Court Yard", "description");
        kitchen = new Room("Kitchen", "description");
        giftShop = new Room("Gift Shop", "description");
        parkingLot = new LockedRoom("Parking Lot", "description", true);
        lobby = new Room("Lobby", "description");
        lab = new Room("Lab", "description");
        XRay = new Room("XRay room", "description");
        hallway = new Room("Hallway", "description");
        stairs = new Room("Stairs", "description");
        mainEntrance = new Room("Main Entrance", "description");
        
        // Creating Items
        Item officeKey, bleach, bigRock, keyCard, keg, fileCabinet, playerFile, fuseBox;
        
        officeKey = new Item("key", 0.1, true);
        bleach = new Item("bleach", 12.00, true);
        bigRock = new Item("rock", 99.00, true);
        keyCard = new Item("key card", .1, true);
        keg = new Item("keg", 9999, false);
        fileCabinet = new Item("file cabinet", 9999, false);
        playerFile = new Item("Your file", 1, true);
        fuseBox = new Item("fuse box", 9999, false);
<<<<<<< HEAD
        
        // Creating Weapons
        Weapon pipe, scaple, knife, peculiarBlade;
        pipe = new Weapon("pipe", 6.00, true, 25);
        scaple = new Weapon("scaple", .3, true, 15);
        knife = new Weapon("knife", 3, true, 35);
        peculiarBlade = new Weapon("peculiar blade", 10, true, 50);
        
                            
        // Setting up exits and items between rooms:        
        // Patient Care
        patientCare.setExit("west", cafe);
        patientCare.setExit("south", bathroom);
        // Bathroom
        bathroom.setExit("north", patientCare);
        bathroom.setExit("east", middleStall);
        bathroom.store(pipe);
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
        
        // other
        powerPlant.store(fuseBox);
        kitchen.store(knife);
        roof.store(peculiarBlade);
        ER.store(scapel);
        breakRoom.store(keyCard);
        lab.store(cabinetKey);
        // Adding rooms to map
        Room[] rooms = {patientCare, basement, cafe, office, bathroom, middleStall, powerPlant, roof, breakRoom, elevator, mainOffice, ER, janitorCloset, kitchen, courtYard, giftShop, parkingLot, lobby, lab, XRay, hallway, stairs, mainEntrance};
        for (Room room : rooms){
            map.put(room.getTitle(), room);
        }
    }
}

