
/**
 * Items are objects that the players can pick up. They are also stored in rooms.
 * Items have a weight, title, and can either be picked up or not.
 * 
 * @author Gavin Kyte and Aaron Chauvette 
 * @version 2017.3.21
 */
public class Item
{
    // instance variables - replace the example below with your own
    private String name;
    private double weight;
    private boolean canTake;
    private String description;
    /**
     * Constructor for objects of class Item
     */
    public Item(String name, double weight, boolean canTake, String description)
    {
        this.name = name;
        this.weight = weight;
        this.canTake = canTake;
        this.description = description;
    }
    public String getName(){
        return name;
    }
    public double getWeight(){
        return weight;
    }
    public boolean canTake(){
        return canTake;
    }
    public String getDescription(){
        return description;
    }
    public void updateItem(String name, double weight, boolean canTake, String description){
        this.name = name;
        this.weight = weight;
        this.canTake = canTake;
        this.description = description;
    }


}
