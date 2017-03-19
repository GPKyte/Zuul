import java.util.HashMap;

/**
 * Write a description of class Map here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Map
{
    // instance variables - replace the example below with your own
    private HashMap<String, Room> rooms;

    /**
     * Constructor for objects of class Map
     */
    public Map(){
        rooms = new HashMap<>();
    }
    
    public void createRooms(){
        
    }
    
    public Room getCurrentRoom(){
    
        return new Room("");
    }
}