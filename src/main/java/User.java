import java.util.List;

public class User {
    private String userName;
    private String userId;
    private List<String> likedMoviesIds;
    private List<String> recommendedMoviesTitles;

    public User(String name, String id, List<String> likedMovies) {
        this.userName = name;
        this.userId = id;
        this.likedMoviesIds = likedMovies;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getLikedMoviesIds() {
        return likedMoviesIds;
    }

    public List<String> getRecommendedMoviesTitles() {
        return recommendedMoviesTitles;
    }

    // Setters
    public void setUserName(String name) {
        this.userName = name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLikedMoviesIds(List<String> likedMovies) {
        this.likedMoviesIds = likedMovies;
    }

    public void setRecommendedMoviesTitles(List<String> recommendedMovies) {
        this.recommendedMoviesTitles = recommendedMovies;
    }
}
