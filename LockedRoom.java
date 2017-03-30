
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
    
    public boolean meetsRequirements(){
        return !isLocked;
    }

    public String getRequirements(){
        return roomReqs;
    }

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

    public void setKey(String key){
        this.keyToUnlock = key;
    }
    
    public String getKey(){
        return keyToUnlock;
    }        
        
    public void setRequirements(String newReqs){
        this.roomReqs = newReqs;
    }
}