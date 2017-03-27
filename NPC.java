
/**
 * A type of player that is controlled by the game rather than the user
 * They may attack or be attacked by the player.
 * 
 * @author Gavin Kyte and Aaron Chauvette
 * @version 2017.3.26
 */
public class NPC extends Player
{
    private boolean isAggro; // Will they attack on sight?
    private double health;
    
    /**
     * Constructor for objects of class NPC
     */
    public NPC(String name, String room, boolean isAggro){
        super(name, room);
        this.isAggro = isAggro;
        this.health = 100;
    }
    public double getHealth(){
        return health;
    }
    public void damageNPC(double damage){
        health -= damage;
    }
    /**
     * Tells if the NPC will attack on sight or not
     * @return boolean will attack
     */
    public boolean isAggro(){
        return this.isAggro;
    }
}
