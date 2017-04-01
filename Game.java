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
    private Random rng;
    private Room currentRoom;
    private Player hero;
    private NPC villain;
    private HashMap<String, Room> map;
    private HashMap<String, Player> characters;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() throws IOException {
        rng = new Random();
        parser = new Parser();
        map = new HashMap<>();
        characters = new HashMap<>();
        makeCharacters();
        worldInit(new Scanner(new BufferedReader(new FileReader("worldInit.txt"))).useDelimiter("\\n"));
        currentRoom = map.get(hero.getRoom());
    }
    
    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play(){            
        printWelcome();
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished && (hero.getHealth() > 0)){
            Command command = parser.getCommand();
            System.out.println();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing. Good bye.");
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
     * @return Room some type of room
     * @param Type of room, Title of room, Key (if relevant), Description of room
     */
    private Room createRoom(String type, String title, String key, String description){
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
     * @return Item of varying type
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
     * Print out the opening message for the player.
     */
    private void printWelcome(){
        System.out.println();
        System.out.println("You wake up in a hospital bed. You're mouth is parched, and you smell bleach in the air.");
        System.out.println("Looking down you see a torn newspaper.");
        System.out.println("It reads \"...ILLER STILL ON THE R...WELCOME TO ZUUL...type \"help\" if you need it\"");
        System.out.println("After standing up, you take a look around");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true if the command ends the game, false otherwise.
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
            case "i":
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
            case "unhide":
                unhide(hero);
                moveNPC();
                break;
            case "use":
            case "interact":
                interact(command);
                break;
                
            case "equip":
                equip(command);
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
     * Equips a weapon if it is in the player's inventory
     * @param A Command that indicates what to equip
     */
    private void equip(Command command){
        String itemName = "";
        if (!command.hasSecondWord()){
            System.out.println("Equip what?");
            return;
        }
        itemName = command.getSecondWord();
        System.out.print(hero.equip(itemName));        
    }
    
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
        System.out.println(currentRoom.getTitle());
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
    
    private void interact(Command command){
        if (!command.hasSecondWord()){
            System.out.println("Do what?");
            return;
        }
        
        String obj = command.getSecondWord();
        switch (obj){
            case "keg":
                if (currentRoom.contains("keg")){
                    drunkenTransport();
                } else {
                    System.out.println("No keg here");
                }
                break;
            case "FuseBox":
                if (currentRoom.contains("FuseBox")){
                    turnOnPower();
                } else {
                    System.out.println("No FuseBox here");
                }                
                break;
            case "cabinet":
                if (currentRoom.contains("cabinet")){
                    getCaseFiles();
                } else {
                    System.out.println("No cabinet here");
                }    
                break;
            default:
                System.out.println("I don't understand...");
                break;
        }
    }
    
    private void drunkenTransport(){
        currentRoom = map.get("Basement");
        // Bad convention here, the name could change and this could led to problems
        hero.addItem(new Item("ControlKey", 0.01, true));
        
        System.out.println("You drink from the keg and gag at the rancid taste.");
        System.out.println("The room starts spinning and dims to black. What did you drink?");
        System.out.println("...");
        System.out.println("Mouth dry, eyes itchy, back stiff, you wake up on a concrete floor.");
        System.out.println(currentRoom.getLongDescription());
    }
    
    private void turnOnPower(){
        LockedRoom lab = (LockedRoom) map.get("Lab");
        lab.setKey("LabKeyCard");
        LockedRoom elevator = (LockedRoom) map.get("Elevator");
        elevator.unlock();
    }
    
    private void getCaseFiles(){
        
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
            unhide(hero);
        } else {
            System.out.println("You cannot enter this room");
            System.out.println(nextRoom.getRequirements());
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
        if (!player.isHidden()){
            System.out.println(player.getName() + player.hide());
        } else {
            System.out.println("You are already hidding.");
        }
        
    }
    private void unhide(Player player){
        if (player.isHidden()){
            System.out.println(player.getName() + player.unhide());
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
        } else {
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
            System.out.println("There is no "+itemName+" here.");
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
            System.out.println("You don't have \""+itemName+"\" in your inventory.");
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
            System.out.println("You cannot fight \""+opponentName+"\" here.");
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
                if (!player.isHidden() && inSameRoom && opponent.isAggro()) {triggerFight(player, opponent);}
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
    
    // Environment actions
    /**
     * The player has won if he meets all the conditions.
     * Prints out the winning message and ends the play loop.
     * @return boolean quit the game
     */
    private boolean winGame(){
        // Not sure when this is triggered or when it will meet the right conditions
        return true;
    }
    
    /**
     * Moves each NPC in the Map into an adjacent room if it is available
     * Then checks for a fight
     */
    private void moveNPC(){
        Room npcRoom = map.get(villain.getRoom());
        String[] exits = npcRoom.getExitDirections();
        if (exits.length == 0) {System.out.println("Villain is stuck"); return;}
        
        int choice = rng.nextInt(exits.length);
        String randomDirection = exits[choice];
        
        Room nextRoom = npcRoom.getExit(randomDirection);
        if (nextRoom != null && nextRoom.meetsRequirements()) {
            villain.setRoom(nextRoom.getTitle());
        } else {
            // Villain can't get in room and is making noise
            System.out.println("*banging noise nearby*");
        }
        
        // For testing, shows where villain is
        //System.out.println("Villain is in " + villain.getRoom());
        checkFightTrigger(hero);
    }
    
    /**
     * Makes the characters involved in the game
     * This includes the hero and villain, but can have side characters as well.
     */
    private void makeCharacters(){
        hero = new Player("Hero", "PatientCare");
        villain = new NPC("Villain", "Lobby", true);
        
        characters.put(hero.getName(), hero);
        characters.put(villain.getName(), villain);
    }
    
    /**
     * Create the world map by filling it with rooms
     * Setting their exits
     * And making items to put in each.
     * Prints to terminal if anything is wrong in the init file
     * 
     * @param Scanner made from the .txt file containing map info
     */
    private void worldInit(Scanner initFile){
        int lineNumber = 0;
        while (initFile.hasNext()) {
            lineNumber++;
            String commandLine = initFile.next();
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
                        System.out.println("@"+lineNumber+": \"room\" command during init is missing room type");
                    }                    
                    if (line.hasNext()){
                        title = line.next();
                    } else {
                        System.out.println("@"+lineNumber+": \"room\" command during init is missing a title");
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
                        System.out.println("@"+lineNumber+": Room already exists. Please remove the copy");
                    }
                    break;
                    
                case "exit":
                    // format for exit command
                    // exit _mainRoomName_ [_direction_ _roomName_ ...]
                    String roomName = "Default";
                    if (line.hasNext()) {
                        roomName = line.next();
                    } else {
                        System.out.println("@"+lineNumber+": Unfinished command \"exit,\" please finish line and try again.");
                        break;
                    }
                    
                    Room mainRoom = map.get(roomName);
                    if (mainRoom == null){
                        System.out.println("@"+lineNumber+": On command \"exit\" during init, the mainRoom ("+roomName+") is null.");
                        System.out.println("Not setting exits for "+roomName+" because it hasn't been created.");
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
                                System.out.println("@"+lineNumber+": On command \"exit\" during init, exit room ("+roomName+") doesn't exist.");
                            }
                        } else {
                            System.out.println("@"+lineNumber+": On command \"exit\" during init, the exit room wasn't provided");
                        }                        
                    }                    
                    break;
                    
                case "add":
                    // add command format
                    // add _type_  _destination_ _name_ _weight_ _canTake_ _<powerLevel>_
                    String type = "item";
                    String destination = null;
                    String name = "defaultName";
                    double weight = 0.00;
                    boolean canTake = true;
                    double powerLevel = 1.00;
                    
                    if (line.hasNext()){
                        type = line.next();
                    } else {
                        System.out.println("@"+lineNumber+": \"add\" command during init is missing a type");
                    }
                    if (line.hasNext()){
                        destination = line.next();
                    } else {
                        System.out.println("@"+lineNumber+": \"add\" command during init is missing a destination name");
                    }
                    if (line.hasNext()){
                        name = line.next();
                    } else {
                        System.out.println("@"+lineNumber+": \"add\" command during init is missing a name");
                    }
                    if (line.hasNextDouble()){
                        weight = line.nextDouble();
                    } else {
                        System.out.println("@"+lineNumber+": \"add\" command during init is missing a double for weight");
                    }
                    if (line.hasNextBoolean()){
                        canTake = line.nextBoolean();
                    } else {
                        System.out.println("@"+lineNumber+": \"add\" command during init is missing a boolean for canTake");
                    }
                    if (line.hasNextDouble() && type.equals("weapon")){
                        powerLevel = line.nextDouble();
                    } else {
                        // nothing should happen here
                    }
                                        
                    Item i = createItem(type, name, weight, canTake, powerLevel);
                    if (map.get(destination) != null) {
                        map.get(destination).store(i);
                    } else {
                        System.out.println("@"+lineNumber+": "+i.getName()+" cannot be stored because "+destination+"doesn't exist");
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
}

