import java.util.Set;
import java.util.HashMap;

/**
 * Class Room is a room in an adventure game
 * 
 * This is used in an application name Zuul,
 * based off of a textbook example described
 * in the README of this project
 * 
 * @author  Gavin Kyte and Aaron Chauvette
 * @version 2017.04.08
 */

public class Room 
{
    private String description;
    private String title;
    private HashMap<String, Room> exits;
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

    
    
    // Room info
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
        // Puts the description on newline unless it is blank
        if (!description.equals("")){
            description = "\n " + description;
        }
        
        // Creates long description
        String longDesc = "You are in the " + title + ". " + description + "\n" + getExitString();
        if (itemList.size() > 0){
            longDesc += "\n" + listItems();
        }
        return longDesc;
    }
    

    
    // Exit related
    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor){
        exits.put(direction, neighbor);
    }    
    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return String of room's exits
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
     * Provides array of exits determined by the keys of exits HashMap
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
    
    
    
    // Item specific
    /**
     * Returns list of items contained in room.
     * @return String listing items in room.
     */
    public String listItems(){
        String listOfItems = title + " has:";
        
        if (itemList.size() > 0){
            for (String itemName : itemList.keySet()){
                listOfItems += " " + itemName;
            }
        } else {
            listOfItems += " nothing";
        }
        return listOfItems;
    }    
    /**
     * Tells whether the room contains an object or not
     * @param String name of item
     * @return boolean contains item or not
     */
    public boolean contains(String itemName){
        return (itemList.get(itemName) != null);
    }    
    /**
     * Returns the item without removing it from room
     * @param String name of item
     * @return Item if it exists/is in room
     */
    public Item getItem(String itemName){
        Item item = null;
        if (contains(itemName)) {item = itemList.get(itemName);}
        return item;
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
    /**
     * Removes item from the room and returns it
     * @return Item with name requested
     */
    public Item remove(String itemName){        
        Item chosenItem = null;
        if (itemList.containsKey(itemName)){
            itemList.get(itemName);
            itemList.remove(itemName);
        }
        return chosenItem;
    }
    
    
    
    // Special cases and reqs
    /**
     * Returns whether the room meets the
     * requirements to enter
     * @return boolean saying if you can enter
     */
    public boolean meetsRequirements(){
        return true;
    }
    /**
     * Returns the requirements to enter.
     * There shouldn't be requirements to enter Room class,
     * but the child classes might have some
     * @return String for troubleshooting
     */
    public String getRequirements(){
        return "This room has no requirements or isn't fully implemented. You should not see this message";
    }
}

