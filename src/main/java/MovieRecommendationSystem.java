import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieRecommendationSystem {
    private List<Movie> movies;
    private List<User> users;
    private FileManager fileManager;
    private List<String> errors;

    MovieRecommendationSystem(){
        movies = new ArrayList<>();
        users = new ArrayList<>();
        fileManager = new FileManager();
        errors = new ArrayList<>();
    }

    public List<Movie> getMovies() {
        return this.movies;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public List<String> getErrors(){
        return errors;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void loadMovies(String moviesTXT) {
        List<String> movieLines = fileManager.readFile(moviesTXT);
        this.movies.clear();
        for (int i = 0; i < movieLines.size(); i += 2) {
            if (i + 1 >= movieLines.size()) break;
            
            String titleLine = movieLines.get(i);
            String genresLine = movieLines.get(i + 1);
            
            if (titleLine == null || titleLine.trim().isEmpty()){
                errors.add("ERROR: Missing movie title & Id at line " + (i + 1));
                continue;
            } 
            
            String[] titleData = titleLine.split(",");
            if (titleData.length != 2) {
                errors.add("ERROR: Incorrect movie line format at line " + (i + 1));
                continue;
            }
            
            String title = titleData[0].trim();
            String id = titleData[1].trim();
            
            if (title.isEmpty()) {
                errors.add("ERROR: Empty movie title at line " + (i + 1));
                continue;
            }
            if (id.isEmpty()) {
                errors.add("ERROR: Empty movie ID at line " + (i + 1));
                continue;
            }

            if (genresLine == null || genresLine.trim().isEmpty()) {
                errors.add("ERROR: Empty movie genres at line " + (i + 2));
                continue;
            }
            
            String[] checkGenres = genresLine.trim().split("\\s+");


            if (checkGenres.length > 1 && !genresLine.contains(",")) {
                errors.add("ERROR: Movie genres must be separated by a comma at line " + (i + 2));
                continue;
            }

            List<String> genres = new ArrayList<>();
            for (String genre : genresLine.split(",")) {
                genres.add(genre.trim());
            }
            
            movies.add(new Movie(title, id, genres));
        }
    }
    
    public void loadUsers(String usersTXT) {
        List<String> userLines = fileManager.readFile(usersTXT);
        this.users.clear();
        for (int i = 0; i < userLines.size(); i += 2) {
            if (i + 1 >= userLines.size()) break;
            
            String nameLine = userLines.get(i);
            String moviesLine = userLines.get(i + 1);
            
            if (nameLine == null || nameLine.trim().isEmpty()) {
                errors.add("ERROR: Missing user name & Id at line " + (i + 1));
                continue;
            }
            
            String[] nameData = nameLine.split(",");
            if (nameData.length != 2) {
                errors.add("ERROR: Incorrect user line format at line " + (i + 1));
                continue;
            }
            
            String name = nameData[0].trim();
            String id = nameData[1].trim();
            
            if (name.isEmpty()) {
                errors.add("ERROR: Empty user name at line " + (i + 1));
                continue;
            }
            if (id.isEmpty()) {
                errors.add("ERROR: Empty user ID at line " + (i + 1));
                continue;
            }

            if (moviesLine == null) {
                errors.add("ERROR: Empty liked movies at line " + (i + 2));
                continue;
            }

            List<String> likedMovies = new ArrayList<>();
            if(moviesLine.trim().isEmpty()){
                likedMovies.add("");
            }
            else{
                for (String movie : moviesLine.split(",")) {
                    likedMovies.add(movie.trim());
                }
            }
            
            users.add(new User(name, id, likedMovies));
        }
    }

    public void loadData(String movieTXT, String usersTXT){
        loadMovies(movieTXT);
        loadUsers(usersTXT);
    }

    public void validateData(){
        Set<String> moviesUnique = new HashSet<>();
        Set<String> userUnique = new HashSet<>();

        for (Movie movie : movies) {
            if (!Validators.validMovieTitle(movie.getMovieTitle())) {
                errors.add("ERROR: Movie Title " + movie.getMovieTitle() + " is wrong");
                return;
            }

            if (!Validators.validMovieIdLetters(movie.getMovieId(), movie.getMovieTitle())){
                errors.add("ERROR: Movie Id letters " + movie.getMovieId() + " are wrong");
                return;
            }

            if(!Validators.validMovieIdUniqueNumbers(movie.getMovieId(), movie.getMovieTitle())){
                errors.add("ERROR: Movie Id numbers " + movie.getMovieId() + " are wrong");
                return;
            }

            if(!Validators.validMovieId(movie.getMovieId(), movie.getMovieTitle(), moviesUnique)){
                errors.add("ERROR: Movie Id " + movie.getMovieId() + " is duplicated");
                return;
            }
            moviesUnique.add(movie.getMovieId());
        }

        for (User user : users) {
            if (!Validators.validUserName(user.getUserName())){
                errors.add("ERROR: User Name " + user.getUserName() + " is wrong");
                return;
            }

            if(!Validators.validUserIdFormat(user.getUserId())){
                errors.add("ERROR: User Id " + user.getUserId() + " format is wrong");
                return;
            }

            if (!Validators.validUserId(user.getUserId(), userUnique)){
                errors.add("ERROR: User Id " + user.getUserId() + " is duplicated");
                return;
            }

            
            userUnique.add(user.getUserId());
        }
    }

    public void createRecommendedMovies() {
        Set<String> movieIdsSet = new HashSet<>();
        for (Movie movie : movies) {
            movieIdsSet.add(movie.getMovieId());
        }

        for (User user : users) {
            Set<String> likedGenres = new HashSet<>();
            List<String> invalidMovies = new ArrayList<>();

            for (String likedMovieId : user.getLikedMoviesIds()) {
                if (likedMovieId == null || likedMovieId.trim().isEmpty()) {
                    continue;
                }

                if (!movieIdsSet.contains(likedMovieId)) {
                    invalidMovies.add(likedMovieId);
                } else {
                    for (Movie movie : movies) {
                        if (movie.getMovieId().equals(likedMovieId.trim())) {
                            likedGenres.addAll(movie.getMovieGenres());
                        }
                    }
                }
            }

            if (!invalidMovies.isEmpty()) {
                errors.add("ERROR: User " + user.getUserId() + " liked movie IDs not in movies: " + String.join(", ", invalidMovies));
                user.setRecommendedMoviesTitles(new ArrayList<>());
                continue;
            }

            if (!errors.isEmpty()){
                user.setRecommendedMoviesTitles(new ArrayList<>());
                continue;
            }

            Set<String> recommendedMovies = new HashSet<>();
            for (String genre : likedGenres) {
                for (Movie movie : movies) {
                    if (movie.getMovieGenres().contains(genre) && !user.getLikedMoviesIds().contains(movie.getMovieId())) {
                        recommendedMovies.add(movie.getMovieTitle());
                    }
                }
            }

            user.setRecommendedMoviesTitles(new ArrayList<>(recommendedMovies));
        }
    }

    public void writeRecommendedMovies(String RecTXT) {
        List<String> recommendedMovies = new ArrayList<>();

        if(!errors.isEmpty()){
            recommendedMovies.add(errors.get(0));
        }

        else {
            for (User user : users) {
                recommendedMovies.add(user.getUserName() + ", " + user.getUserId());
                recommendedMovies.add(String.join(", ", user.getRecommendedMoviesTitles()));
            }
        }

        fileManager.writeFile(RecTXT, recommendedMovies);
    }

    public void printUsers(){
        for(User user: users){
            System.out.println("User Name: " + user.getUserName() + ", User ID: " + user.getUserId());
            System.out.print("Liked Movies: ");
            for (String likedMovies: user.getLikedMoviesIds()){
                System.out.print(likedMovies + ", ");
            }
            System.out.println();
            System.out.print("Recommended Movies: ");
            for (String recMovies: user.getRecommendedMoviesTitles()){
                System.out.print(recMovies + ", ");
            }
            System.out.println();
        }
    }

    public void printMovies(){
        for(Movie movie: movies){
            System.out.println("Movie Title: " + movie.getMovieTitle() + ", Movie ID: " + movie.getMovieId());
            System.out.print("Genres: ");
            for (String genres: movie.getMovieGenres()){
                System.out.print(genres + ", ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        String moviesTXT = "src/main/files/movies.txt";
        String usersTXT = "src/main/files/users.txt";
        String recTXT = "src/main/files/recommendations.txt";

        File movieFile = new File(moviesTXT);
        File userFile = new File(usersTXT);

        if (!movieFile.exists()) {
            System.out.println("ERROR: The movie file doesn't exist at " + moviesTXT);
            return;
        }

        if (!userFile.exists()) {
            System.out.println("ERROR: The user file doesn't exist at " + usersTXT);
            return;
        }

        MovieRecommendationSystem recSystem = new MovieRecommendationSystem();

        recSystem.loadData(moviesTXT, usersTXT);
        recSystem.validateData();
        recSystem.createRecommendedMovies();

        if (!recSystem.errors.isEmpty()) {
            System.out.println(recSystem.errors.get(0));
            recSystem.writeRecommendedMovies(recTXT);
            return;
        }

        recSystem.writeRecommendedMovies(recTXT);
        recSystem.printMovies();
        System.out.println("=================================================");
        recSystem.printUsers();
    }
}
