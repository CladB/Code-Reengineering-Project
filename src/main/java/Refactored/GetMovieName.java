package Refactored;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GetMovieName {
    
    public  String getMovieName(String movieFilePath) {
        Pattern movieFilePathPattern = Pattern.compile("(?:.*[\\\\\\/])?(.*)(?:[\\s\\.\\(\\[]\\d{4}[\\.\\)\\]\\s]).*");
        Matcher movieNameMatcher = movieFilePathPattern.matcher(movieFilePath);
        if (!movieNameMatcher.find()) {
            return null;
        }
        
        String movieName = movieNameMatcher.group(1).replace(".", " ");
        String yifySubtitleUrl = "https://www.yifysubtitles.com/search?q=" + movieName.replace(" ", "+");

        try {
            Document document = Jsoup.connect(yifySubtitleUrl).get();
            Element element = document.select("div.container > div.row > div > div[style=\"text-align:center;\"]")
                    .first();

            if (element != null && "no results".equals(element.text())) {
      
                String OmdbUrl = "http://www.omdbapi.com/?apikey=d345b81e&t=" + movieName.replace(" ", "+");
                String omdbJsonData = Jsoup.connect(OmdbUrl).ignoreContentType(true).execute().body();
                JsonObject jsonObject = new Gson().fromJson(omdbJsonData, JsonObject.class);
                if (jsonObject == null || jsonObject.get("Title") == null) {
                    return null;
                }
                System.out.println(
                        "\nNo results in yifysubtitles.com for Movie: " + movieName + "\n" + "Closest matching ");
                movieName = jsonObject.get("Title").getAsString();
            }
        } catch (IOException e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
        return movieName;
    }

}
