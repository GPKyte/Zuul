import java.util.Random;
import java.util.Scanner;
/**
 * FightScene is a type of minigame or scene that displays a conflict event separate from Game
 * The losing player is killed or game ends if the player is killed
 * 
 * @author Aaron Chauvette
 * @version 3/22/17
 */
public class FightScene
{
    // instance variables - replace the example below with your own
    private Random rng;
    private Scanner reader;
    private double playerPower;
    private double killerPower;    

    /**
     * Constructor for Minigame
     */
    public FightScene()
    {
        playerPower = 10;
        killerPower = 35;
        rng = new Random();
        reader = new Scanner(System.in);
    }
    public double playerDamage(Player hero){// Calculates how much damage the killer will take.
        return (playerPower + hero.getWeaponDamage()) * (hero.getHealth()/100);
    }
    public double killerDamage(NPC villain){// Calculates how much damage the player will take.
        return killerPower * (villain.getHealth()/100);
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
                villain.takeDamage(playerDamage(hero));
            }
            else if(user == 3){
                System.out.println("The killer catches you off guard and strikes you.");
                hero.takeDamage(killerDamage(villain));
            }
            else if(user == 4){
                System.out.println("You parry the killer's strike and they stumble. You sweep the killer's legs and land a critical strike");
                villain.takeDamage(playerDamage(hero));
            }
        }
        else if(killer == 1){
            System.out.println("The killer tries to block.");
            if(user == 1){
                System.out.println("The killer blocks your strike and counters it.");
                hero.takeDamage(killerDamage(villain));
            }
            else if(user == 2){
                System.out.println("Neither of you move.");
            }
            else if(user == 3){
                System.out.println("The killer is caught off guard by your bluff and you land a strike.");
                villain.takeDamage(playerDamage(hero));
            }
            else if(user == 4){
                System.out.println("The killer strikes you.");
                hero.takeDamage(killerDamage(villain));
            }
        }
        else if(killer == 2){
            System.out.println("The killer attempts to feint.");
            if(user == 1){
                System.out.println("The killer is caught off guard by your strike.");
                villain.takeDamage(playerDamage(hero));
            }
            else if(user == 2){
                System.out.println("You fall for the killer's bluff and take a hit.");
                hero.takeDamage(killerDamage(villain));
            }
            else if(user == 3){
                System.out.println("Neither of your bluffs succeed.");
            }
            else if(user == 4){
                System.out.println("The killer strikes you.");
                hero.takeDamage(killerDamage(villain));
            }
        }

    }
    public void killerEncounter(Player hero, NPC villain)//Starts the minigame.
    {
        boolean fight = true;
        boolean wonFight = false;
        double originalHealth = hero.getHealth();
        System.out.println("A wild killer appears!");
        while(fight){
            gameTurn(hero, villain);
            System.out.println("Your Health: " + hero.getHealth() + "  Killer's Health: " + villain.getHealth());
            if(hero.getHealth() <= 0){
                System.out.println("You died.");
                wonFight = false;
                fight = false;
                if (hero.has("kidney")){
                    hero.drop("kidney");
                    System.out.println("Kidney used for resurrection!");
                    System.out.println("You gain another chance for using the dark arts.");
                    hero.setHealth(100);
                    killerEncounter(hero, villain);
                }
            }
            else if(villain.getHealth() <= 0){
                System.out.println("You defeated the wild killer!");
                wonFight = true;
                hero.setHealth(originalHealth);
                fight = false;
            }
        } 
    }
}
