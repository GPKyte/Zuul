import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;

/**
 * Write a description of class Player here.
 * 
 * @author Gavin Kyte and Aaron Chauvette
 * @version 2017.3.23
 */
public class Player
{
    // instance variables - replace the example below with your own
    private String name;
    private String currentRoom;
    private int health;
    private ArrayList<String> roomHistory;
    private HashMap<String, Item> bag;

    /**
     * Constructor for objects of class Player
     */
    public Player(String name, String roomName){
        this.name = name;
        currentRoom = roomName;
        health = 100;
        roomHistory = new ArrayList<>();
        bag = new HashMap<>();
    }

    /**
     * Gets current room name
     * @return String title of current room
     */
    public String getRoom(){
        return currentRoom;
    }
    
    /**
     * Sets current room
     * @param String room title
     */
    public void setRoom(String roomName){
        roomHistory.add(currentRoom);
        currentRoom = roomName;        
    }
    
    /**
     * If there is a room visited history, will change the current room to
     * the previous room name.
     * @return String name of room to move into
     */
    public String goBack(){
        setRoom(roomHistory.get(roomHistory.size()-1));
        // Uncomment the following lines of code to change method from going
        // between same rooms, or from undoing roomHistory:
        // roomHistory.remove(roomHistory.size()-1);
        // roomHistory.remove(roomHistory.size()-1);
        // Using two removes since setRoom will add the room you go back to.        
        
        return currentRoom;
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
    
    public boolean has(String itemName){
        return (bag.get(itemName) != null);
    }
    
    public void addItem(Item item){
        bag.put(item.getName(), item);
    }
    
    public Item drop(String itemName){
        Item chosenItem = bag.get(itemName);
        bag.remove(itemName);
        return chosenItem;
    }
}
