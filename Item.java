
/**
 * Write a description of class Item here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Item
{
    // instance variables - replace the example below with your own
    private String name;
    private float weight;
    private boolean canTake;

    /**
     * Constructor for objects of class Item
     */
    public Item(String name,float weight,boolean canTake)
    {
        this.name = name;
        this.weight = weight;
        this.canTake = canTake;
    }
    public String getName(){
        return name;
    }
    public float getWeight(){
        return weight;
    }
    public boolean canTake(){
        return canTake;
    }
    public void updateItem(String name,float weight,boolean canTake){
        this.name = name;
        this.weight = weight;
        this.canTake = canTake;
    }


}
