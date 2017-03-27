
/**
 * A specific type of item that can be used during battle.
 * It must be equiped, but otherwise is stored normally.
 * 
 * @author Gavin Kyte and Aaron Chauvette
 * @version (a version number or a date)
 */
public class weapon extends Item
{
    // instance variables - replace the example below with your own
    private double power;

    /**
     * Creates a weapon
     */
    public weapon(String name, double weight, boolean canTake, double power)
    {
        super(name,weight,canTake);
        this.power = power;
    }

    /**
     * Gets power of weapon
     * @return double power modifier of weapon
     */
    public double getPower(){
        return power;
    }
}
