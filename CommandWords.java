import java.util.HashMap;
/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a simple, text based adventure game.
 * 
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * @author  Michael Kölling and David J. Barnes, modified by Gavin Kyte
 * @version 2017.03.26
 */

public class CommandWords
{
    private HashMap<String, CommandWord> validCommands;
    
    /**
     * Constructor - initialise the command words.
     */
    public CommandWords(){
        validCommands = new HashMap<>();
        for (CommandWord command : CommandWord.values()){
            if (command != CommandWord.UNKNOWN){
                for (String alias : command.aliases()) {
                    validCommands.put(alias, command);
                }
            }
        }
    }

    public CommandWord getCommandWord(String commandName){
        if (validCommands.containsKey(commandName)){
            return validCommands.get(commandName);
        } else {
            return CommandWord.UNKNOWN;
        }
    }
    
    /**
     * Check whether a given String is a valid command word. 
     * @return true if it is, false if it isn't.
     */
    public boolean isCommand(String aString){
        return validCommands.containsKey(aString);
    }

    /**
     * Print all valid commands to System.out.
     */
    public void showAll(){
        for(String command: validCommands.keySet()) {
            System.out.print(command + "  ");
        }
        System.out.println();
    }
}
