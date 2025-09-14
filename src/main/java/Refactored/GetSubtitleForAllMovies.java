package Refactored;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GetSubtitleForAllMovies {

    private final GetMovieName getMovName = new GetMovieName();
    private final GetSubtitles getSub = new GetSubtitles();

    public boolean getSubtitlesForAllMovies(File directory, String name) {
        File movieFolder = new File(directory, name);

        try {
            if (isValidMovieDirectory(movieFolder) && shouldDownloadSubtitle(movieFolder)) {
                Optional<File> movieFile = getFirstMovieFile(movieFolder);
                movieFile.ifPresent(this::processMovie);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e);
        }

        return false;
    }

    private boolean isValidMovieDirectory(File dir) {
        return dir.isDirectory() && dir.listFiles() != null;
    }

    private boolean shouldDownloadSubtitle(File dir) {
        return hasNoSubtitleFile(dir);
    }

    private void processMovie(File movieFile) {
        String movieFilePath = movieFile.toString();
        System.out.println("\nMovie File Path:\n\t" + movieFilePath);
        String movieName = getMovName.getMovieName(movieFilePath);

        boolean success = getSub.getSubtitles(movieName, movieFilePath);
        if (!success) {
            System.out.println("\nSkipping Downloading Subtitles for the Current Movie");
            System.out.println("---------------------------------------------------------------" +
                    "---------------------------------------------------\n");
        }
    }

    private Optional<File> getFirstMovieFile(File folder) {
        File[] files = folder.listFiles();
        if (files == null)
            return Optional.empty();

        return Arrays.stream(files)
                .filter(file -> checkExtension(file, getFileExtensions()))
                .findFirst();
    }

    private boolean hasNoSubtitleFile(File folder) {
        return Arrays.stream(folder.listFiles())
                .noneMatch(f -> f.toString().endsWith(".srt"));
    }

    private static List<String> getFileExtensions() {
        return Arrays.asList(".avi", ".mp4", ".mkv", ".mpg", ".mpeg", ".mov", ".rm", ".vob", ".wmv", ".flv", ".3gp");
    }

    private static boolean checkExtension(File file, List<String> fileExtensions) {
        int index = file.toString().lastIndexOf(".");
        if (index == -1) {
            return false;
        }
        String format = file.toString().substring(index);
        return fileExtensions.contains(format);
    }
}
