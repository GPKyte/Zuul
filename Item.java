
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
    private String description;
    private double weight;
    private boolean canTake;

    /**
     * Constructor for objects of class Item
     */
    public Item(String name, double weight, boolean canTake)
    {
        this.name = name;
        this.description = "This item has no description.";
        this.weight = weight;
        this.canTake = canTake;
    }
    
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public double getWeight(){
        return weight;
    }
    public boolean canTake(){
        return canTake;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setWeight(Double weight){
        this.weight = weight;
    }
    public void setDescription(String description){
        this.description = description;
    }
}
