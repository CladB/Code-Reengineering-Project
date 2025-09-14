package Refactored;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class YifySubtitleService {
    private static final String YIFY_BASE_URL = "https://www.yifysubtitles.com/search?q=";

    public boolean hasResultsFor(String movieName) throws IOException {
        String searchUrl = YIFY_BASE_URL + movieName.replace(" ", "+");
        Document document = Jsoup.connect(searchUrl).get();
        Element resultElement = document.select("div.container > div.row > div > div[style=\"text-align:center;\"]").first();
        return !(resultElement != null && "no results".equalsIgnoreCase(resultElement.text()));
    }
}
