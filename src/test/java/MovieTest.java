import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

class MovieTest {

    @Test
    void movieConstructorTest() {
        Movie movie = new Movie("Inception", "I123", List.of("Sci-Fi", "Action"));

        assertEquals("Inception", movie.getMovieTitle());
        assertEquals("I123", movie.getMovieId());
        assertEquals(2, movie.getMovieGenres().size());
    }

    @Test
    void setMovieTitleTest() {
        Movie movie = new Movie("Old Title", "OT123", List.of("Drama"));
        movie.setMovieTitle("New Title");

        assertEquals("New Title", movie.getMovieTitle());
    }

    @Test
    void setMovieIdTest() {
        Movie movie = new Movie("Inception", "I123", List.of("Sci-Fi"));
        movie.setMovieId("I999");

        assertEquals("I999", movie.getMovieId());
    }   

    @Test
    void setMovieGenresTest() {
        Movie movie = new Movie("Inception", "I123", List.of("Sci-Fi"));
        movie.setMovieGenres(List.of("Drama", "Mystery"));

        assertTrue(movie.getMovieGenres().contains("Drama"));
        assertEquals(2, movie.getMovieGenres().size());
    }
}
