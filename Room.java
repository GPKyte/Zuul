import java.util.Set;
import java.util.HashMap;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Room 
{
    private String description;
    private String title;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private HashMap<String, Item> itemList;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String title, String description){
        this.title = title;
        this.description = description;
        exits = new HashMap<>();
        itemList = new HashMap<>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor){
        exits.put(direction, neighbor);
    }

    /**
     * Return title of room
     * @return Room's title
     */
    public String getTitle(){
        return title;
    }
    
    /**
     * Get content description for looking around room
     * @return String two lines with exits and items in room
     */
    public String look(){
        return getExitString() + "\n" + listItems();
    }
    
    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription(){
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription(){
        return "You are in " + title + ".\n" + description + "\n" + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString(){
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Provides list of exits determined by the keys of exits HashMap
     * @return Array containing available exit directions
     */
    public String[] getExitDirections(){
        String[] allExits = new String[exits.size()];
        int i = 0;
        for (String exit : exits.keySet()){
            allExits[i] = exit;
            i++;
        }
        return allExits;
    }
        
    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction){
        return exits.get(direction);
    }
    
    /**
     * Returns list of items contained in room.
     * @return String listing items in room.
     */
    public String listItems(){
        String listOfItems = title + " has:"; 
        for (String itemName : itemList.keySet()){
            listOfItems += " " + itemName;
        }
        return listOfItems;
    }
    
    /**
     * Tells whether the room contains an object or not
     * @return boolean contains item or not
     */
    public boolean contains(String itemName){
        return (itemList.get(itemName) != null);
    }
    
    /**
     * Adds an item to the room if it is not already in the room
     * @param Item to be stored
     */
    public void store(Item aItem){
        if (itemList.get(aItem.getName()) == null){
            itemList.put(aItem.getName(), aItem);
        } else {
            System.out.println("There is already a " + aItem.getName() + " in " + title);
        }
    }
    
    public Item remove(String itemName){
        Item chosenItem = itemList.get(itemName);
        itemList.remove(itemName);
        return chosenItem;
    }
}

