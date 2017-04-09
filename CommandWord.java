/**
 * Enumeration class CommandWord - write a description of the enum class here
 * 
 * @author Gavin Kyte
 * @version 2017.04.08
 */
public enum CommandWord
{
    // A value for each command word along with its
    // corresponding user interface string.
    GO("go"), QUIT("quit q"), HELP("help"), BACK("back"), DROP("drop"), TAKE("take"),
    INVENTORY("inventory i"), LOOK("look"), UNLOCK("unlock"), 
    LOCK("lock"), EQUIP("equip"), FIGHT("fight"), HIDE("hide"), UNHIDE("unhide"), 
    USE("use"), UNKNOWN("?");
    
    // The command string.
    private String commandString;
    
    /**
     * Initialise with the corresponding command string.
     * @param commandString The command string.
     */
    CommandWord(String commandString){
        this.commandString = commandString;
    }
    
    /**
     * @return The command word as a string.
     */
    public String toString(){
        return commandString;
    }
    
    /**
     * @return Array with all names associated with command
     */
    public String[] aliases(){
        return commandString.split(" ");
    }
}