package Refactored;

public class MovieNameResolver {

    private final MovieNameExtractor extractor = new MovieNameExtractor();
    private final YifySubtitleService yifyService = new YifySubtitleService();
    private final OmdbApiService omdbService = new OmdbApiService();

    public String resolveMovieName(String filePath) {
        String movieName = extractor.extract(filePath);
        if (movieName == null) {
            return null;
        }

        try {
            if (!yifyService.hasResultsFor(movieName)) {
                System.out.println("\nNo results in yifysubtitles.com for Movie: " + movieName);
                String titleFromOmdb = omdbService.getMovieTitle(movieName);
                if (titleFromOmdb != null) {
                    System.out.println("Closest matching from OMDB: " + titleFromOmdb);
                    movieName = titleFromOmdb;
                }
            }
        } catch (Exception e) {
            System.err.println("Error resolving movie name: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return movieName;
    }
}
