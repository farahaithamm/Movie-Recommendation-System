import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    private FileManager fileManager;
    private static final String OUTPUT_FILE = "test_output.txt";
    private static final String READ_TEST_FILE = "test_read.txt";
    private static final String WRITE_TEST_FILE = "test_write.txt";
    private static final String INVALID_FILE = "/invalid/test.txt";

    @BeforeEach
    void setUp() throws IOException {
        fileManager = new FileManager();
        Files.write(Paths.get(READ_TEST_FILE), Arrays.asList(" Line1 ", " Line2 "));
    }

    @AfterEach
    void tearDown() {
        fileManager = null;
        try {
            Files.deleteIfExists(Paths.get(OUTPUT_FILE));
            Files.deleteIfExists(Paths.get(READ_TEST_FILE));
            Files.deleteIfExists(Paths.get(WRITE_TEST_FILE));
        } catch (IOException ignored) {}
    }

    @Test
    void readUsersFileTest() {
        List<String> expected = Arrays.asList(
            "Ahmed Hassan, 111111111",
            "I123, TM456",
            "Sarah Mohamed, 22222222B",
            "I789, TDK234",
            "Omar Ali, 33333333C",
            "A567, I123"
        );
        assertEquals(expected, fileManager.readFile("src/test/files/users.txt"));
    }

    @Test
    void readMoviesFileTest() {
        List<String> expected = Arrays.asList(
            "Inception, I123",
            "Sci-Fi, Thriller",
            "The Matrix, TM456",
            "Action, Sci-Fi",
            "Interstellar, I789",
            "Sci-Fi, Drama",
            "The Dark Knight, TDK234",
            "Action, Crime",
            "Avengers, A567",
            "Action, Adventure"
        );
        assertEquals(expected, fileManager.readFile("src/test/files/movies.txt"));
    }

    @Test
    void readRecommendationsFileTest() {
        List<String> expected = Arrays.asList(
            "Ahmed Hassan, 111111111",
            "Interstellar, The Dark Knight, Avengers",
            "Sarah Mohamed, 222222222B",
            "Inception, The Matrix, Avengers",
            "Omar Ali, 333333333C",
            "The Matrix, The Dark Knight, Interstellar"
        );
        assertEquals(expected, fileManager.readFile("src/test/files/recommendations.txt"));
    }
    
    @Test
    void readNonExistentFileTest() {
        List<String> result = fileManager.readFile("does_not_exist.txt");
        assertTrue(result.isEmpty());
    }

    @Test
    void writeThenReadTest() {
        List<String> original = Arrays.asList("Test Line 1", "Test Line 2", "Test Line 3");
        
        fileManager.writeFile(OUTPUT_FILE, original);
        List<String> read = fileManager.readFile(OUTPUT_FILE);
        
        assertEquals(original, read);
    }

    @Test
    void readFileWithSpacesTest() throws IOException {
        Files.write(Paths.get(OUTPUT_FILE), Arrays.asList("  Trimmed Line  ", "Normal Line"));
        
        List<String> result = fileManager.readFile(OUTPUT_FILE);
        
        assertEquals(2, result.size());
        assertEquals("  Trimmed Line  ", result.get(0));
        assertEquals("Normal Line", result.get(1));
    }

    @Test
    void writeFileDoesNotThrowTest() {
        List<String> data = Arrays.asList("test");
        assertDoesNotThrow(() -> fileManager.writeFile(INVALID_FILE, data));
    }

    @Test
    void readValidFileTest() {
        List<String> lines = fileManager.readFile(READ_TEST_FILE);
        assertEquals(Arrays.asList(" Line1 ", " Line2 "), lines);
    }

    @Test
    void writeAndReadFileTest() throws IOException {
        List<String> data = Arrays.asList(" Line1 ", "Line2");
        fileManager.writeFile(WRITE_TEST_FILE, data);

        List<String> result = Files.readAllLines(Paths.get(WRITE_TEST_FILE));
        assertEquals(data, result);
    }

    @Test
    void readEmptyFileTest() throws IOException {
        Files.write(Paths.get(READ_TEST_FILE), List.of());
        
        List<String> result = fileManager.readFile(READ_TEST_FILE);
        assertTrue(result.isEmpty());
    }
}
