import java.util.HashMap;
import java.util.Set;

/**
 * Write a description of class Player here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player
{
    // instance variables - replace the example below with your own
    private String name;
    private String currentRoom;
    private int health;
    private HashMap<String, Item> bag;

    /**
     * Constructor for objects of class Player
     */
    public Player(String name, String roomName){
        this.name = name;
        currentRoom = roomName;
    }

    /**
     * Gets current room name
     * @return     String currentRoom
     */
    public String getRoom(){
        return currentRoom;
    }
    
    /**
     * Sets current room
     * @param String room title
     */
    public void setRoom(String roomName){
        this.currentRoom = roomName;
    }
    
    /**
     * Returns contents of bag
     * @return Sring list of item names
     */
    public String getInventory(){
        String bagContents = "You're carrying:";
        for (String thing : bag.keySet()){
            bagContents += " " + thing;
        }
        return bagContents;
    }
}
