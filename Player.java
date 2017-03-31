import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;

/**
 * The player is able to hold items, "go" to different rooms and fight other players
 * 
 * @author Gavin Kyte and Aaron Chauvette
 * @version 2017.3.23
 */
public class Player
{
    // instance variables
    private String name;
    private String currentRoom;
    private double health;
    private boolean hidden;
    private ArrayList<String> roomHistory;
    private HashMap<String, Item> bag;
    private Item myWeapon;
    private double weightLimit;

    /**
     * Constructor for objects of class Player
     * @param String player's name, String name of starting room
     */
    public Player(String name, String roomName){
        this.name = name;
        currentRoom = roomName;
        health = 100;
        weightLimit = 100.00;
        hidden = false;
        myWeapon = null;
        roomHistory = new ArrayList<>();
        bag = new HashMap<>();
    }
    
    public void takeDamage(double damage){
        health -= damage;
    }

    /**
     * Gets the name of the player
     * @Return String player name
     */
    public String getName(){
        return name;
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
    
    /**
     * Gets the weight limit for this player. This is how much more can be carried,
     * rather than the total. So that we can check new items before adding them.
     * @return double with remaining carry limit
     */
    public double getWeightLimit(){
        double totalWeightCarried = 0;
        for (Item i : bag.values()){
            totalWeightCarried += bag.get(i).getWeight();
        }
        return (weightLimit - totalWeightCarried);
    }
    
    /**
     * Determines if player has a item in their bag
     * @return boolean does or doesn't have item
     */
    public boolean has(String itemName){
        return (bag.get(itemName) != null);
    }
    
    /**
     * Makes a reference to item object inside of player's inventory
     * @param Item that is being added to bag
     */
    public void addItem(Item item){
        bag.put(item.getName(), item);
    }
    
    /**
     * Returns and removes the desired item from the player's bag
     * This gives the object removed so it can be stored in the current room
     * @return Item to be stored or destroyed
     */
    public Item drop(String itemName){
        Item chosenItem = bag.get(itemName);
        bag.remove(itemName);
        return chosenItem;
    }
    
    public double getHealth(){
        return health;
    }
    
    /**
     * Hides the player.
     * @return String hidden status
     */
    public String hide(){
        hidden = true;
        return " is hiding";
    }
    
    /**
     * Player comes out of hiding.
     * @return String hidden status
     */
    public String unhide(){
        hidden = false;
        return " no longer hidden.";
    }
    
    /**
     * Choose an item to use as a weapon if it is a weapon
     * @return String status of equip
     */
    public String equipWeapon(String itemName){    
        return "You have equiped " + itemName;
    }
}
