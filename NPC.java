
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
    private boolean canMove;
    
    /**
     * Constructor for objects of class NPC
     */
    public NPC(String name, String room, boolean isAggro){
        super(name, room);
        this.isAggro = isAggro;
        this.canMove = true;
    }
    
    /**
     * Tells if the NPC will attack on sight or not
     * @return boolean will attack
     */
    public boolean isAggro(){
        return this.isAggro;
    }
    
    public boolean moves(){
        return this.canMove;
    }
    
    public void pacify(){
        this.isAggro = false;
        this.canMove = false;
    }
    
    public void takeDamage(double damage){
        super.takeDamage(damage);
    }
}
