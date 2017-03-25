
/**
 * Write a description of class weapon here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class weapon extends Item
{
    // instance variables - replace the example below with your own
    private double power;

    /**
     * Constructor for objects of class weapon
     */
    public weapon(String name,double weight,double power,boolean canTake)
    {
        super(name,weight,canTake);
        this.power = power;
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public double getPower()
    {
        return power;
    }
}
