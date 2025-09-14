package Refactored;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieNameExtractor {
    public String extract(String filePath) {
        Pattern pattern = Pattern.compile("(?:.*[\\\\\\/])?(.*)(?:[\\s\\.\\(\\[]\\d{4}[\\.\\)\\]\\s]).*");
        Matcher matcher = pattern.matcher(filePath);
        if (matcher.find()) {
            return matcher.group(1).replace(".", " ");
        }
        return null;
    }
}
