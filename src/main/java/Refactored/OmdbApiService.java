package Refactored;

import java.io.IOException;
import org.jsoup.Jsoup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class OmdbApiService {
    private static final String OMDB_API_KEY = "d345b81e";
    private static final String OMDB_BASE_URL = "http://www.omdbapi.com/?apikey=";

    public String getMovieTitle(String movieName) throws IOException {
        String apiUrl = OMDB_BASE_URL + OMDB_API_KEY + "&t=" + movieName.replace(" ", "+");
        String json = Jsoup.connect(apiUrl).ignoreContentType(true).execute().body();
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        if (jsonObject != null && jsonObject.has("Title")) {
            return jsonObject.get("Title").getAsString();
        }
        return null;
    }
}
