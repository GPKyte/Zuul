
/**
 * A locked room needs a key of some sort to open it. It can be locked, unlocked,
 * and inherits from the base Room class.
 * 
 * @author Gavin Kyte
 * @version 2017.3.26
 */
public class LockedRoom extends Room
{
    private boolean isLocked;
    private String keyToUnlock = "key";
    private String roomReqs;

    /**
     * Constructor for objects of class LockedRoom
     */
    public LockedRoom(String title, String description, boolean isLocked){
        super(title, description);
        this.isLocked = isLocked;
        roomReqs = "You need the " + keyToUnlock + " to unlock this room.";
    }
    
    
    // Requirements
    /**
     * Tells whether the room can be entered
     * @return boolean whether the room can be entered
     */
    public boolean meetsRequirements(){
        return !isLocked;
    }
    /**
     * Shows what needs to be done to get in
     * @return String returns the name of the key to unlock room
     */
    public String getRequirements(){        
        roomReqs = "You need the " + keyToUnlock + " to unlock this room.";
        return roomReqs;
    }        
    /**
     * Used during events to change room requiremets
     * @param String description of new reqs
     */
    public void setRequirements(String newReqs){
        this.roomReqs = newReqs;
    }

    
    // Change state
    /**
     * Unlocks the room if the player is holding the key object
     * that has been linked to this room.
     * @return String with lock status of room
     */
    public String unlock(){
        isLocked = false;
        return "Room unlocked";
    }
    /**
     * Locks the room if the player is holding the key object
     * that has been linked to this room.
     * @return String with lock status of room
     */
    public String lock(){
        isLocked = true;
        return "Room locked.";
    }
    
    
    // Key
    /**
     * Sets the name of the key/thing needed to open the room
     * @param String name of key or global status
     */
    public void setKey(String key){
        this.keyToUnlock = key;
    }
    /**
     * Gives the name of key/thing needed to open room
     * @return String name of key
     */
    public String getKey(){
        return keyToUnlock;
    }
}