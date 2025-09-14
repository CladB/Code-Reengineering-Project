package Refactored;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetSubtitles {
    private static final Scanner scanner = new Scanner(System.in);
    private static Elements elements;

    public boolean getSubtitles(String movieName, String movieFilePath) {
        movieName = promptForMovieName(movieName);
        if (movieName == null) return false;
       
        elements = searchMovie(movieName);
        if (elements == null || elements.isEmpty()) return false;

        String movieUrl = chooseMovie(movieName);
        if (movieUrl == null) return false;

        String subtitleUrl = findSubtitleURL(movieUrl, Main.getLanguage());
        if (subtitleUrl == null) {
            System.out.println("\nSubtitle doesn't exist for \"" + movieName + "\" in language \"" + Main.getLanguage() + "\".");
            return false;
        }

        String movieFolder = (movieFilePath != null) ? getMovieFolder(movieFilePath) : System.getProperty("user.dir");
        if (movieFolder == null) {
            System.out.println("Failed to locate folder for: " + movieFilePath);
            return false;
        }

        String downloadSubtitleUrl = subtitleUrl.replace("/subtitles", "/subtitle") + ".zip";
        downloadAndExtractSubtitle(downloadSubtitleUrl, movieFolder);

        System.out.println("\nSuccessfully downloaded subtitle for: " + movieName);
        return true;
    }

    private String promptForMovieName(String movieName) {
        if (movieName == null || movieName.trim().isEmpty()) {
            System.out.println("Enter movie keyword (press ENTER to skip): ");
            movieName = scanner.nextLine().trim();
            if (movieName.isEmpty()) return null;
        }
        return movieName;
    }

    private Elements searchMovie(String movieName) {
        Elements results = getSearchResults(movieName);
        if (results == null || results.isEmpty()) {
            System.out.println("No results found for: " + movieName);
            return null;
        }
        return results;
    }

    private String chooseMovie(String movieName) {
        if (elements.size() == 1) {
            return getMovieURL(elements.get(0));
        }

        System.out.println("Multiple results found for: " + movieName);
        AtomicInteger index = new AtomicInteger(0);
        elements.forEach(e -> System.out.println((index.incrementAndGet()) + ". " + getMovieAndYear(e)));

        System.out.print("Select a movie (0 to skip): ");
        int choice = scanner.nextInt();
        if (choice <= 0 || choice > elements.size()) {
            System.out.println("Invalid selection.");
            return null;
        }

        return getMovieURL(elements.get(choice - 1));
    }

    private String findSubtitleURL(String movieUrl, String language) {
        Document doc = getHTMLContent(movieUrl);
        if (doc == null) return null;
        Elements links = doc.select("a[href*=" + language + "]");
        return links.isEmpty() ? null : links.get(0).attr("abs:href");
    }

    private void downloadAndExtractSubtitle(String url, String folder) {
        try {
            URLConnection conn = new URL(url).openConnection();
            InputStream in = conn.getInputStream();
            File zip = File.createTempFile("subtitle", ".zip", new File(folder));
            try (FileOutputStream fos = new FileOutputStream(zip)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
            }

            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zip))) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (!entry.getName().endsWith(".srt")) continue;

                    File outFile = new File(folder, entry.getName());
                    try (FileOutputStream out = new FileOutputStream(outFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }
                    }
                    zis.closeEntry();
                }
            }
            zip.delete();
        } catch (IOException e) {
            System.err.println("Error downloading/extracting subtitle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Utility methods (unchanged)
    private static Elements getSearchResults(String movieName) {
        final String url = "https://www.yifysubtitles.com/search?q=" + movieName.replace(" ", "+");
        return getHTMLContent(url).select("div.media-body");
    }

    private static Document getHTMLContent(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            System.err.println("Failed to load HTML: " + e.getMessage());
            return null;
        }
    }

    private static String getMovieURL(Element element) {
        return element.select("a").attr("abs:href");
    }

    private static String getMovieAndYear(Element element) {
        return element.select("a").text();
    }

    private static String getMovieFolder(String path) {
        return path.contains("/") ? path.substring(0, path.lastIndexOf("/")) : null;
    }
}
