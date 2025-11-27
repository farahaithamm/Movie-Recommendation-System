import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MovieRecommendationSystemTest {

    private MovieRecommendationSystem system;
    
    private final String movieTestTXT = "src/test/files/testMovies.txt";
    private final String userTestTXT = "src/test/files/testUsers.txt";
    private final String recTestTXT = "src/test/files/testRecommendations.txt";

    @BeforeEach
    void setUp() {
        system = new MovieRecommendationSystem();
    }

    @AfterEach
    void tearDown() throws IOException {
        system = null;
    }

    @Test
    void loadMoviesWithMissingLinesTest() throws IOException {
        List<String> moviesData = Arrays.asList("Movie1, M001");
        Files.write(Paths.get(movieTestTXT), moviesData);

        system.loadMovies(movieTestTXT);

        assertTrue(system.getMovies().isEmpty());
    }

    @Test
    void loadMoviesWithMissingIdTest() throws IOException {
        List<String> moviesData = Arrays.asList("Movie1", "Action");
        Files.write(Paths.get(movieTestTXT), moviesData);

        system.loadMovies(movieTestTXT);

        assertTrue(system.getMovies().isEmpty());
    }

    @Test
    void loadMoviesWithMissingNameTest() throws IOException {
        List<String> moviesData = Arrays.asList(", M123", "Action");
        Files.write(Paths.get(movieTestTXT), moviesData);

        system.loadMovies(movieTestTXT);

        assertTrue(system.getMovies().isEmpty());
    }

    @Test
    void loadMoviesWithCorrectInputTest() throws IOException {
        List<String> moviesData = Arrays.asList("Movie1, M001", "Action");
        Files.write(Paths.get(movieTestTXT), moviesData);

        system.loadMovies(movieTestTXT);

        assertEquals(1, system.getMovies().size());
    }

    @Test
    void loadUserWithMissingLinesTest() throws IOException {
        List<String> usersData = Arrays.asList("Alice, 123456789");
        Files.write(Paths.get(userTestTXT), usersData);

        system.loadUsers(userTestTXT);

        assertTrue(system.getUsers().isEmpty());
    }

    @Test
    void loadUserWithMissingIdTest() throws IOException {
        List<String> usersData = Arrays.asList("Alice", "I123");
        Files.write(Paths.get(userTestTXT), usersData);

        system.loadUsers(userTestTXT);

        assertTrue(system.getUsers().isEmpty());
    }

    @Test
    void loadUserWithMissingNameTest() throws IOException {
        List<String> usersData = Arrays.asList(", 123456789", "I123");
        Files.write(Paths.get(userTestTXT), usersData);

        system.loadUsers(userTestTXT);

        assertTrue(system.getUsers().isEmpty());
    }

    @Test
    void loadUserWithCorrectInputTest() throws IOException {
        List<String> usersData = Arrays.asList("Alice, 123456789", "I123");
        Files.write(Paths.get(userTestTXT), usersData);

        system.loadUsers(userTestTXT);

        assertEquals(1, system.getUsers().size());
    }

    @Test
    void testLoadDataWithValidInputs() throws IOException {
        List<String> moviesData = Arrays.asList(
            "The Matrix, TM201",
            "Action, Sci-Fi",
            "Titanic, T102",
            "Romance, Drama"
        );
        List<String> usersData = Arrays.asList(
            "Alice, 12345678X",
            "TM201"
        );
        Files.write(Paths.get(movieTestTXT), moviesData);
        Files.write(Paths.get(userTestTXT), usersData);

        system.loadData(movieTestTXT, userTestTXT);

        assertEquals(2, system.getMovies().size());
        assertEquals(1, system.getUsers().size());
    }


    @Test
    void testValidateData() throws IOException {
        // User likes all movies
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "The Matrix, TM456",
            "Action, Sci-Fi"
        ));

        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I123, TM456"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        assertTrue(system.getUsers().get(0).getRecommendedMoviesTitles().isEmpty());

        // User with no liked movies
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "The Matrix, TM456",
            "Action, Sci-Fi"
        ));

        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            ""
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        assertTrue(system.getUsers().get(0).getRecommendedMoviesTitles().isEmpty());

        // Invalid Username
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "The Matrix, TM456",
            "Action, Sci-Fi"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahm1ed Hassan, 111111111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        assertTrue(system.getUsers().get(0).getRecommendedMoviesTitles().isEmpty());

        // Invalid User Id
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "Inception Two, IT123",
            "Action, Thriller"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 1111A1111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        assertTrue(system.getUsers().get(0).getRecommendedMoviesTitles().isEmpty());

        // Invalid movie title
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "inception, I123",
            "Sci-Fi, Thriller",
            "The Matrix, TM456",
            "Action, Sci-Fi"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        assertTrue(system.getUsers().get(0).getRecommendedMoviesTitles().isEmpty());

        // Invalid Movie Id
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I223",
            "Sci-Fi, Thriller"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        assertTrue(system.getUsers().get(0).getRecommendedMoviesTitles().isEmpty());

        // Duplicate movie IDs
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "Interstellar, I123",
            "Action, Thriller"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        assertTrue(system.getUsers().get(0).getRecommendedMoviesTitles().isEmpty());

        // Duplicate user IDs
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I123",
            "Sarah Mohamed, 111111111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        assertTrue(system.getUsers().get(0).getRecommendedMoviesTitles().isEmpty());

        // Correct users & movies format
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "Inception Two, IT124",
            "Action, Thriller"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "IT124",
            "Sarah Mohamed, 222222222",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        assertFalse(system.getUsers().get(0).getRecommendedMoviesTitles().isEmpty());
        assertFalse(system.getUsers().get(1).getRecommendedMoviesTitles().isEmpty());

    }

    @Test
    void writeRecommendationsTest() throws IOException {
        // User likes all movies
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "The Matrix, TM456",
            "Action, Sci-Fi"
        ));

        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I123, TM456"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        List<String> results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("Ahmed Hassan, 111111111",
            ""
        ));

        // User with no liked movies
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "The Matrix, TM456",
            "Action, Sci-Fi"
        ));

        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            ""
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("Ahmed Hassan, 111111111",
            ""
        ));

        // Invalid Username
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "The Matrix, TM456",
            "Action, Sci-Fi"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahm1ed Hassan, 111111111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: User Name Ahm1ed Hassan is wrong"));

        // Invalid User Id
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "The Matrix, TM456",
            "Action, Sci-Fi"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 1111A1111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: User Id 1111A1111 format is wrong"));

        // Liked Movie Not in Movies List
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "The Matrix, TM456",
            "Action, Sci-Fi"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I456"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: User 111111111 liked movie IDs not in movies: I456" ));

        // Invalid movie title
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "inception, I123",
            "Sci-Fi, Thriller",
            "The Matrix, TM456",
            "Action, Sci-Fi"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Movie Title inception is wrong"));

        // Invalid Movie Id Letters
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "The Matrix, M456",
            "Action, Sci-Fi"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Movie Id letters M456 are wrong"));

        // Invalid Movie Id Number
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I223",
            "Sci-Fi, Thriller",
            "The Matrix, TM456",
            "Action, Sci-Fi"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Movie Id numbers I223 are wrong"));

        // Duplicate movie IDs
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "Interstellar, I123",
            "Action, Thriller"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Movie Id I123 is duplicated"));

        // Duplicate user IDs
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "The Matrix, TM456",
            "Action, Sci-Fi"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I123",
            "Sarah Mohamed, 111111111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: User Id 111111111 is duplicated"));

        // Correct users & movies format
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "Inception Two, IT124",
            "Action, Thriller"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "IT124",
            "Sarah Mohamed, 222222222",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("Ahmed Hassan, 111111111",
            "Inception",
            "Sarah Mohamed, 222222222",
            "Inception Two"
        ));
    }

    @Test
    void writeRecommendationsWithMultipleErrorsTest() throws IOException {
        // Invalid Username & User Id
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "The Matrix, TM456",
            "Action, Sci-Fi"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahm1ed Hassan, 11111A111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        List<String> results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: User Name Ahm1ed Hassan is wrong"));

        // Invalid User Id & Movie Title
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "inception, I123",
            "Sci-Fi, Thriller",
            "The Matrix, TM456",
            "Action, Sci-Fi"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 1111A1111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Movie Title inception is wrong"));

        // Invalid Movie Title & Movie Id
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "inception, I223",
            "Sci-Fi, Thriller",
            "The Matrix, TM456",
            "Action, Sci-Fi"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Movie Title inception is wrong"));

        // Invalid Movie Id Letters & Movie ID Numbers
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "The Matrix, M446",
            "Action, Sci-Fi"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Movie Id letters M446 are wrong"));

        // Invalid Movie Id Number & Duplicated Movie Id
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I223",
            "Sci-Fi, Thriller",
            "Interstellar, I223",
            "Action, Thriller"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Movie Id numbers I223 are wrong"));

        // Duplicate movie IDs & Users IDs
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "Interstellar, I123",
            "Action, Thriller"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I123",
            "Sarah Mohamed, 111111111",
            "I123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Movie Id I123 is duplicated"));
    }

    @Test
    void ceateRecommendedMoviesTest() {
        Movie movie1 = new Movie("Inception", "I123", Arrays.asList("Sci-Fi", "Thriller"));
        Movie movie2 = new Movie("The Matrix", "TM456", Arrays.asList("Action", "Sci-Fi"));
        Movie movie3 = new Movie("Interstellar", "I789", Arrays.asList("Sci-Fi", "Drama"));
        Movie movie4 = new Movie("The Dark Knight", "TDK234", Arrays.asList("Action", "Crime"));
        Movie movie5 = new Movie("The Conjuring", "TC987", Arrays.asList("Horror", "Thriller"));
        Movie movie6 = new Movie("Avengers", "A567", Arrays.asList("Adventure"));

        User user1 = new User("Ahmed Hassan", "111111111", Arrays.asList("I123"));  
        User user2 = new User("Sarah Mohamed", "22222222B", Arrays.asList("I789", "TDK234")); 
        User user3 = new User("Omar Ali", "33333333C", Arrays.asList("A567")); 
        User user4 = new User("Mona Samy", "15256987L", Arrays.asList()); 
        User user5 = new User("Farah Haitham", "987654321", Arrays.asList("TC987", "TM456"));
        User user6 = new User("Malak Alaa", "12356734E", Arrays.asList("I123", "TM456", "I789", "TDK234", "TC987", "A567"));

        system.setMovies(Arrays.asList(movie1, movie2, movie3, movie4, movie5, movie6));
        system.setUsers(Arrays.asList(user1, user2, user3, user4, user5, user6));
        system.createRecommendedMovies();

        // User1: Sci-Fi, Action
        assertEquals(3, user1.getRecommendedMoviesTitles().size());
        assertTrue(user1.getRecommendedMoviesTitles().contains("Interstellar"));
        assertTrue(user1.getRecommendedMoviesTitles().contains("The Matrix"));
        assertTrue(user1.getRecommendedMoviesTitles().contains("The Conjuring"));

        // User2: Sci-Fi, Action, Drama, Crime
        assertEquals(2, user2.getRecommendedMoviesTitles().size());
        assertTrue(user2.getRecommendedMoviesTitles().contains("Inception"));
        assertTrue(user2.getRecommendedMoviesTitles().contains("The Matrix"));

        // User3: Adventure
        assertEquals(user3.getRecommendedMoviesTitles(), Arrays.asList());
        assertEquals(0, user3.getRecommendedMoviesTitles().size());

        // User4: No Likes
        assertEquals(user4.getRecommendedMoviesTitles(), Arrays.asList());
        assertEquals(0, user4.getRecommendedMoviesTitles().size());

        // User5: Horror, Thriller, Sci-Fi, Action
        assertEquals(3, user5.getRecommendedMoviesTitles().size());
        assertTrue(user5.getRecommendedMoviesTitles().contains("Inception"));
        assertTrue(user5.getRecommendedMoviesTitles().contains("Interstellar"));
        assertTrue(user5.getRecommendedMoviesTitles().contains("The Dark Knight"));

        // User4: No Likes
        assertEquals(user6.getRecommendedMoviesTitles(), Arrays.asList());
        assertEquals(0, user6.getRecommendedMoviesTitles().size());
    }


    @Test
    void loadDataWithInvalidLinesTest() throws IOException {
        // =============== Movies ===================

        // Missing Movie Nmae & Id Line
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "The Dark Knight, TDK123",
            "Action",
            "",
            "Sci-Fi, Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Mona, 12345678X",
            "TDK123"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        List<String> results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Missing movie title & Id at line 3"));

        // Missing Movie Name without ,
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "TDK123",
            "Action",
            "Interstellar, I123",
            "Sci-Fi, Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Mona, 12345678X",
            "TDK123"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Incorrect movie line format at line 1"));

        // Missing Movie Id without ,
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "The Dark Knight",
            "Action",
            "Interstellar, I123",
            "Sci-Fi, Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Mona, 12345678X",
            "TDK123"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Incorrect movie line format at line 1"));

        // Missing , Between Movie Title & Id
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "The Dark Knight TDK123",
            "Action",
            "Interstellar, I123",
            "Sci-Fi, Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Mona, 12345678X",
            "TDK123"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Incorrect movie line format at line 1"));

        // Empty Movie Name
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            ", TDK123",
            "Action",
            "Interstellar, I123",
            "Sci-Fi, Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Mona, 12345678X",
            "TDK123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Empty movie title at line 1"));

        // Missing Movie Id
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "The Dark Knight, TDK123",
            "Action",
            "Interstellar, ",
            "Sci-Fi, Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Mona, 12345678X",
            "TDK123"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Empty movie ID at line 3"));

        // =============== Users ===================

        // Missing User Name & Id Line
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "The Dark Knight, TDK123",
            "Action",
            "Interstellar, I123",
            "Sci-Fi, Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "",
            "TDK123"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Missing user name & Id at line 1"));

        // Missing User Name without ,
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "The Dark Knight, TDK123",
            "Action",
            "Interstellar, I123",
            "Sci-Fi, Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "12345678X",
            "TDK123"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Incorrect user line format at line 1"));

        // Missing User Id without ,
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "The Dark Knight, TDK123",
            "Action",
            "Interstellar, I123",
            "Sci-Fi, Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Mona",
            "TDK123"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Incorrect user line format at line 1"));

        // Missing , Between Genres
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "The Dark Knight, TDK123",
            "Action",
            "Interstellar, I123",
            "Sci-Fi Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Mona, 12345678X",
            "TDK123"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Movie genres must be separated by a comma at line 4"));

        // Missing , Between User Name & Id
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "The Dark Knight, TDK123",
            "Action",
            "Interstellar, I123",
            "Sci-Fi, Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Mona 12345678X",
            "TDK123"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Incorrect user line format at line 1"));

        // Empty User Name
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "The Dark Knight, TDK123",
            "Action",
            "Interstellar, I123",
            "Sci-Fi, Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            ", 12345678X",
            "TDK123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Empty user name at line 1"));

        // Missing User Id
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "The Dark Knight, TDK123",
            "Action",
            "Interstellar, I123",
            "Sci-Fi, Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Mona, ",
            "TDK123"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Empty user ID at line 1"));

    }

    @Test
    void loadDataWithMultipleInvalidLinesTest() throws IOException {
        // Missing Movie Nmae & Id Line and Missing User Name
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "The Dark Knight, TDK123",
            "Action",
            "",
            "Sci-Fi, Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            ", 12345678X",
            "TDK123"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        List<String> results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Missing movie title & Id at line 3"));

        // Missing Movie Id & User Name
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "The Dark Knight, TDK123",
            "Action",
            "Interstellar, ",
            "Sci-Fi, Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            ", 12345678X",
            "TDK123"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Empty movie ID at line 3"));

        // Missing 2 Movie Ids
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "The Dark Knight, ",
            "Action",
            "Interstellar, ",
            "Sci-Fi, Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            ", 12345678X",
            "TDK123"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Empty movie ID at line 1"));

        // Empty User Name & Empty User Id
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "The Dark Knight, TDK123",
            "Action",
            "Interstellar, I123",
            "Sci-Fi, Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            ", ",
            "TDK123"
        ));
        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Empty user name at line 1"));

        // Missing User1 Id & User2 Name
        system = new MovieRecommendationSystem();
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "The Dark Knight, TDK123",
            "Action",
            "Interstellar, I123",
            "Sci-Fi, Drama"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Mona, ",
            "TDK123",
            " ,123456789",
            "I123"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList("ERROR: Empty user ID at line 1"));

    }

    @Test
    void writeRecommendedMoviesTest()  throws IOException {
        Files.write(Paths.get(movieTestTXT), Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "The Matrix, TM456",
            "Action, Sci-Fi",
            "Interstellar, I789",
            "Sci-Fi, Drama",
            "The Dark Knight, TDK234",
            "Action, Crime",
            "The Conjuring, TC987",
            "Horror, Thriller",
            "Avengers, A567",
            "Adventure"
        ));
        Files.write(Paths.get(userTestTXT), Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I123",
            "Sarah Mohamed, 22222222B",
            "I789, TDK234",
            "Omar Ali, 33333333C",
            "A567",
            "Mona Samy, 15256987L",
            "",
            "Farah Haitham, 987654321",
            "TC987, TM456",
            "Malak Alaa, 12356734E",
            "I123, TM456, I789, TDK234, TC987, A567"
        ));

        system.loadData(movieTestTXT, userTestTXT);
        system.validateData();
        system.createRecommendedMovies();
        system.writeRecommendedMovies(recTestTXT);
        List <String> results = Files.readAllLines(Paths.get(recTestTXT));
        assertEquals(results, Arrays.asList(
            "Ahmed Hassan, 111111111",
            "Interstellar, The Matrix, The Conjuring",
            "Sarah Mohamed, 22222222B",
            "The Matrix, Inception",
            "Omar Ali, 33333333C",
            "",
            "Mona Samy, 15256987L",
            "",
            "Farah Haitham, 987654321",
            "The Dark Knight, Interstellar, Inception",
            "Malak Alaa, 12356734E",
            ""
        ));
    }
}
