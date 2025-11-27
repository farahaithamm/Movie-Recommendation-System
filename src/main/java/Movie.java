import java.util.List;

public class Movie {
    private String movieTitle;
    private String movieId;
    private List<String> movieGenres;
    
    public Movie(String title, String id, List<String> genres) {
        this.movieTitle = title;
        this.movieId = id;
        this.movieGenres = genres;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieId() {
        return movieId;
    }

    public List<String> getMovieGenres() {
        return movieGenres;
    }

    public void setMovieTitle(String title) {
        this.movieTitle = title;
    }

    public void setMovieId(String id) {
        this.movieId = id;
    }

    public void setMovieGenres(List<String> genres) {
        this.movieGenres = genres;
    }
}
