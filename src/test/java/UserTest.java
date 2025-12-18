import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

class UserTest {

    @Test
    void userConstructorTest() {
        User user = new User("Ahmed Mohamed", "123456789", Arrays.asList("I123", "TDK234"));

        assertEquals("Ahmed Mohamed", user.getUserName());
        assertEquals("123456789", user.getUserId());
        assertEquals(2, user.getLikedMoviesIds().size());
    }

    @Test
    void setUserNameTest() {
        User user = new User("Ahmed Mohamed", "123456789", Arrays.asList());
        user.setUserName("Ali Mohamed");

        assertEquals("Ali Mohamed", user.getUserName());
    }

    @Test
    void setUserIdTest() {
        User user = new User("Ahmed Mohamed", "123456789", Arrays.asList());
        user.setUserId("987654321");

        assertEquals("987654321", user.getUserId());
    }

    @Test
    void setLikedMoviesIdsTest() {
        User user = new User("Ahmed Mohamed", "123456789", Arrays.asList());
        user.setLikedMoviesIds(Arrays.asList("I123"));

        assertEquals(1, user.getLikedMoviesIds().size());
        assertTrue(user.getLikedMoviesIds().contains("I123"));
    }

    @Test
    void setRecommendedMoviesTitlesTest() {
        User user = new User("Ahmed Mohamed", "123456789", Arrays.asList());
        user.setRecommendedMoviesTitles(Arrays.asList("Inception", "Batman"));

        assertEquals(2, user.getRecommendedMoviesTitles().size());
        assertTrue(user.getRecommendedMoviesTitles().contains("Batman"));
    }
}
