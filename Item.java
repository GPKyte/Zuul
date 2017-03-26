
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

    /**
     * Constructor for objects of class Item
     */
    public Item(String name, float weight, boolean canTake)
    {
        this.name = name;
        this.weight = weight;
        this.canTake = canTake;
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
    public void updateItem(String name, double weight, boolean canTake){
        this.name = name;
        this.weight = weight;
        this.canTake = canTake;
    }


}
