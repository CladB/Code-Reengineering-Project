package Refactored;

import java.io.File;

public class Main {
    private static String movieFolderPath = null;
    private static String movieFilePath = null;
    private static String language = "English";
    private static String movieName = null;

    public static void Usage() {
        System.out.println("\nUsage: ");
        System.out.println("------");
        System.out.println("\nCommand to get Subtitle with Movie Name: ");
        System.out.println("\tjava -jar <JAR_FILE_PATH> -m <MOVIE NAME>");
        System.out.println("\nExample:\n\tjava -jar \"C:/Users/admin/downloads/subtitle-downloader.jar\" -m \"Inception\"");
        System.out.println("\n\t\tOR");
        System.out.println("\nCommand to get Subtitles with Movie File Path: ");
        System.out.println("\tjava -jar <JAR_FILE_PATH> -mP <MOVIE_FILE_PATH>");
        System.out.println("\nExample:\n\tjava -jar \"C:/Users/admin/downloads/subtitle-downloader.jar\" -mP \"E:/Movies/Final Destination/Final.Destination.2009.mp4\"");
        System.out.println("\n\t\tOR");
        System.out.println("\nCommand to get Subtitles for all the Movies in separate folders present inside a Folder:");
        System.out.println("\tjava -jar <JAR_FILE_PATH> -mD <MOVIES_FOLDER_PATH>");
        System.out.println("\nExample:\n\tjava -jar \"C:/Users/admin/downloads/subtitle-downloader.jar\" -mD \"E:/Movies\"");
        System.out.println("\nOptional Parameters:");
        System.out.println("\t-lang Language of the Subtitle. By Default set to English");

    }

    public static void main(String[] args) {
        ParseArgument parpar = new ParseArgument(args);
        GetSubtitles getSub = new GetSubtitles();
        GetMovieName getMovName = new GetMovieName();
        GetSubtitleForAllMovies getSubAll = new GetSubtitleForAllMovies();
        parpar.processArguments();

        if (movieName != null) {
            if (!getSub.getSubtitles(movieName, movieFilePath)) {
                System.out.println("\nEXITING THE PROGRAM!!!");
                System.exit(1);
            }
        } else if (movieFilePath != null) {

            System.out.println("Movie File Path:\n\t" + movieFilePath);
            String movieName = getMovName.getMovieName(movieFilePath);

            if (!getSub.getSubtitles(movieName, movieFilePath)) {
                System.out.println("\nEXITING THE PROGRAM!!!");
                System.exit(1);
            }
        } else if (movieFolderPath != null) {
            File folder = new File(movieFolderPath);
            if (folder.isDirectory()){
                boolean success = getSubAll.getSubtitlesForAllMovies(new File(movieFolderPath), movieFolderPath);
                if (!success){
                    System.out.println("EXITING THE PROGRAM");
                    System.exit(1);
                }
            } else {
                System.out.println("Invalid folder path provided: " + movieFolderPath);
                System.out.println("Please provide a valid directory for Movie Folder Path.");
                System.exit(1);
            }
        } else {
            System.out.println(
                    "Please provide one of the following: \n1) Movie Name\n2) Movie File Path\n3) Movie Folder Path");
            Usage();
        }
    }

    public static String getMovieFolderPath() {
        return movieFolderPath;
    }

    public static String getMovieFilePath() {
        return movieFilePath;
    }

    public static String getLanguage() {
        return language;
    }

    public static String getMovieName() {
        return movieName;
    }

    public static void setMovieFolderPath(String movieFolderPath) {
        Main.movieFolderPath = movieFolderPath;
    }

    public static void setMovieFilePath(String movieFilePath) {
        Main.movieFilePath = movieFilePath;
    }

    public static void setLanguage(String language) {
        Main.language = language;
    }

    public static void setMovieName(String movieName) {
        Main.movieName = movieName;
    }

}

