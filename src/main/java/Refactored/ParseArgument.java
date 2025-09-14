package Refactored;

import java.util.Arrays;
import java.util.List;

public class ParseArgument {
    //commandoptions
    private List arguments;
    public static ParseArgument ArgumentsParse(String[] args) {
        return new ParseArgument(args);
    }
    // Constructor dokumentasi ya? Kata pak joddy yang java log doang (?)
    public ParseArgument(String[] args) {
        arguments = Arrays.asList(args);
    }
    
    // Check whether the Option Exists
    boolean hasOption(String option) {
        return arguments.contains(option);
    }

    // Check whether the Option and Value Exists
    boolean hasOptionAndValue(String option) {
        return arguments.contains(option) && arguments.indexOf(option) + 1 < arguments.size();
    }

    // Return the Option's value if it exists
    String valueOf(String option) {
        if (arguments.indexOf(option) + 1 < arguments.size()) {
            return (String) arguments.get(arguments.indexOf(option) + 1);
        } else {
            return null;
        }
    }

    // Example usage of Main.Usage()
    public void processArguments() {
        if (hasOption("-help")) {
            Main.Usage(); // Call the Usage method from the Main class
            System.exit(0);
        }

        if (hasOptionAndValue("-m")) {
            Main.setMovieName(valueOf("-m"));        }
        // If the user needs subtitle for a different language other than English
        if (hasOptionAndValue("-lang")) {
            Main.setLanguage(valueOf("-lang"));
        }

        // Get the movieFilePath or movieFolderPath to Download subtitles
        if (hasOptionAndValue("-mP")) {
            Main.setMovieFilePath(valueOf("-mP"));
        }

        if (hasOptionAndValue("-mD")) {
            Main.setMovieFolderPath(valueOf("-mD"));

        }

    }
}
