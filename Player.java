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
    private String name;
    private String currentRoom;
    private double health;
    private boolean hidden;
    private ArrayList<String> roomHistory;
    private HashMap<String, Item> bag;
    private Weapon myWeapon;
    private double weightLimit;

    /**
     * Constructor for objects of class Player
     * @param String player's name, String name of starting room
     */
    public Player(String name, String roomName){
        this.name = name;
        currentRoom = roomName;
        health = 100;
        weightLimit = 20.00;
        hidden = false;
        myWeapon = null;
        roomHistory = new ArrayList<>();
        bag = new HashMap<>();
    }
    
    
    
    // Player Status
    /**
     * Gets the name of the player
     * @Return String player name
     */
    public String getName(){
        return name;
    }
    
    /**
     * @return boolean
     */
    public boolean isHidden(){
        return hidden;
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
    
    
    
    // Health related
    /**
     * @return double Health of player
     */
    public double getHealth(){
        return health;
    }
    
    /**
     * Sets health to some value. Used after player wins fight
     * @param double Value of health
     */
    public void setHealth(double health){
        this.health = health;
    }
    
    /**
     * Deducts the player's health by the given value
     * @param double or int of damage
     */
    public void takeDamage(double damage){
        health -= damage;
    }

    
    
    
    // Room related
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
        if (roomHistory.size() == 0){return currentRoom;}
        setRoom(roomHistory.get(roomHistory.size()-1));        
        return currentRoom;
    }
    
    
    
    // Item related
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
            totalWeightCarried += i.getWeight();
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
        if (chosenItem.equals(myWeapon)){
            myWeapon = null;
        }
        return chosenItem;
    }
   
    
    // Weapon specific
    /**
     * Choose an item to use as a weapon if it is a weapon
     * @return String status of equip
     */
    public String equip(String itemName){
        if (has(itemName)){
            if (bag.get(itemName) instanceof Weapon) {
                myWeapon = (Weapon)bag.get(itemName);
            } else {
                return itemName + " is not a weapon";
            }
            
            return itemName + " is equipped";
        } else {
            return "You do not have "+itemName+" in your bag";
        }
    }
    
    /**
     * Gets the damage of the weapon if any are equipped
     * @return double damage points
     */
    public double getWeaponDamage(){
        double power = (myWeapon != null) ? myWeapon.getPower():0.00;
        return power;
    }
}