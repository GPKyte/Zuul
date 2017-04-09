
/**
 * Abstract class MainGame - main class to be used in .jar file
 * when using the game outside of BlueJ
 * 
 * @author Gavin Kyte
 * @version 2017.04.08
 */
public abstract class MainGame
{
    /**
     * Main method to play outside of BlueJ
     * @param none
     */
    public static void main(String[] args){
        Game zuul = new Game();
        zuul.play();
    }
}
