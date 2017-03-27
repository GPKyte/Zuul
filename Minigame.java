import java.util.Random;
import java.util.Scanner;
/**
 * Write a description of class Minigame here.
 * 
 * @author Aaron Chauvette
 * @version 3/22/17
 */
public class Minigame
{
    // instance variables - replace the example below with your own
    private Random rng;
    private double playerHealth;
    private double killerHealth;
    private Scanner reader;
    private double playerPower;
    private double killerPower;
    

    /**
     * Constructor for Minigame
     */
    public Minigame(Player hero, NPC villain)
    {
        playerHealth = hero.getHealth();
        killerHealth = villain.getHealth();
        playerPower = 3;
        killerPower = 3;
        rng = new Random();
        reader = new Scanner(System.in);
    }
    public double playerDamage(Player hero){// Calculates how much damage the killer will take.
        return playerPower * (hero.getHealth()/10);
    }
    public double killerDamage(NPC villain){// Calculates how much damage the player will take.
        return killerPower * (villain.getHealth()/10);
    }
    public int killerTurn()//Decides the killer's move.
    {
        return rng.nextInt(3);
    } 
    public int playerTurn()//Player picks a move.
    {
        System.out.println("What will you do?");
        System.out.println("[1] Strike   [2] Block   [3] Feint   [4] Parry");
        int playerMove = 0;
        while(playerMove == 0){
            String selectedMove = reader.nextLine();
            if(selectedMove.equals("1")){
                System.out.println("You prepare to strike.");
                playerMove = 1;
            }
            else if(selectedMove.equals("2")){
                System.out.println("You get ready to block.");
                playerMove = 2;
            }
            else if(selectedMove.equals("3")){
                System.out.println("You attempt to feint.");
                playerMove = 3;
            }
            else if(selectedMove.equals("4")){
                System.out.println("You try to parry and incoming attack.");
                playerMove = 4;
            }
            else{
                System.out.println("Invalid move. Choose 1, 2, 3, or 4");
            }
        }
        return playerMove;
    }
    public void gameTurn(Player hero, NPC villain)//outcome of turn.
    {
        int user = playerTurn();
        int killer = killerTurn();
        if(killer == 0){
            System.out.println("The killer takes a strike at you.");
            if(user == 1){
                System.out.println("Both attacks miss.");
            }
            else if(user == 2){
                System.out.println("You block the attack and land a counter-attack.");
                villain.damageNPC(playerDamage(hero));
            }
            else if(user == 3){
                System.out.println("The killer catches you off guard and strikes you.");
                hero.damagePlayer(killerDamage(villain));
            }
            else if(user == 4){
                System.out.println("You parry the killer's strike and they stumble. You sweep the killer's leg and make a critical strike");
                villain.damageNPC(2 * playerDamage(hero));
            }
        }
        else if(killer == 1){
            System.out.println("The killer tries to block.");
            if(user == 1){
                System.out.println("The killer blocks your strike and counters it.");
                hero.damagePlayer(killerDamage(villain));
            }
            else if(user == 2){
                System.out.println("Neither of you move.");
            }
            else if(user == 3){
                System.out.println("The killer is caught off guard by your bluff and you land a strike.");
                villain.damageNPC(playerDamage(hero));
            }
            else if(user == 4){
                System.out.println("The killer strikes you.");
                hero.damagePlayer(killerDamage(villain));
            }
        }
        else if(killer == 2){
            System.out.println("The killer attempts to feint.");
            if(user == 1){
                System.out.println("The killer is caught off guard by your strike.");
                villain.damageNPC(playerDamage(hero));
            }
            else if(user == 2){
                System.out.println("You fall for the killer's bluff and take a hit.");
                hero.damagePlayer(killerDamage(villain));
            }
            else if(user == 3){
                System.out.println("Neither of your bluffs succeed.");
            }
            else if(user == 4){
                System.out.println("The killer strikes you.");
                hero.damagePlayer(killerDamage(villain));
            }
        }

    }
    public void fight(Player hero, NPC villain)//Starts the minigame.
    {
        boolean fight = true;
        System.out.println("A wild killer appears!");
        while(fight){
            gameTurn(hero,villain);
            System.out.println("Your Health: " + hero.getHealth() + "  Killer's Health: " + villain.getHealth());
            if(playerHealth <= 0){
                System.out.println("You died.");
                fight = false;
            }
            else if(killerHealth <= 0){
                System.out.println("You defeated the wild killer!");
                fight = false;
            }
        }
    }
}
