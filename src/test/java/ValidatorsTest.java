import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorsTest {

    private Set<String> existingUserIds;
    private Set<String> existingMovieIds;

    @BeforeEach
    void setUp() {
        existingUserIds = new HashSet<>();
        existingUserIds.add("123456789");
        existingMovieIds= new HashSet<>();
        existingMovieIds.add("EDR145");
    }

    // ========== validUserName Tests ==========

    @Test
    void validUserNullNameTest() {
        assertFalse(Validators.validUserName(null));
    }

    @Test
    void validUserEmptyNameTest() {
        assertFalse(Validators.validUserName(""));
        assertFalse(Validators.validUserName("   "));
    }

    @Test
    void validUserNameStartsWithSpaceTest() {
        assertFalse(Validators.validUserName(" John"));
        assertFalse(Validators.validUserName(" John Doe"));
    }

    @Test
    void validUserNameSingleWordTest() {
        assertTrue(Validators.validUserName("John"));
        assertTrue(Validators.validUserName("Mary"));
        assertTrue(Validators.validUserName("jOhN"));
    }

    @Test
    void validUserNameMultipleWordsTest() {
        assertTrue(Validators.validUserName("John Doe"));
        assertTrue(Validators.validUserName("Mary Jane Watson"));
        assertTrue(Validators.validUserName("john"));
        assertTrue(Validators.validUserName("john doe"));
        assertTrue(Validators.validUserName("JoHn DoE"));
        assertTrue(Validators.validUserName("MaRy JaNe"));
    }

    @Test
    void validUserNameContainsNumbersTest() {
        assertFalse(Validators.validUserName("John123"));
        assertFalse(Validators.validUserName("John 123"));
    }

    @Test
    void validUserNameContainsSpecialCharactersTest() {
        assertFalse(Validators.validUserName("John-Doe"));
        assertFalse(Validators.validUserName("John@Doe"));
    }

    // ========== validUserId Tests ==========

    @Test
    void validUserIdNullTest() {
        assertFalse(Validators.validUserIdFormat(null));
        assertFalse(Validators.validUserId(null, existingUserIds));
    }

    @Test
    void validUserIdEmptyTest() {
        assertFalse(Validators.validUserIdFormat(""));
        assertFalse(Validators.validUserId("", existingUserIds));
        assertFalse(Validators.validUserIdFormat("   "));
        assertFalse(Validators.validUserId("   ", existingUserIds));
    }

    @Test
    void validUserIdWithOnlyDigitsTest() {
        assertTrue(Validators.validUserIdFormat("122256789"));
        assertTrue(Validators.validUserIdFormat("111111111"));
        assertTrue(Validators.validUserIdFormat("212526298"));
        assertTrue(Validators.validUserId("122256789", existingUserIds));
        assertTrue(Validators.validUserId("111111111", existingUserIds));
        assertTrue(Validators.validUserId("212526298", existingUserIds));
    }

    @Test
    void validUserIdWithLetterAtEndTest() {
        assertTrue(Validators.validUserIdFormat("12345678A"));
        assertTrue(Validators.validUserIdFormat("12345678z"));
        assertTrue(Validators.validUserId("12345678A", existingUserIds));
        assertTrue(Validators.validUserId("12345678z", existingUserIds));
    }

    @Test
    void validUserIdInvalidFormatTest() {
        assertFalse(Validators.validUserIdFormat("A23456789"));
        assertFalse(Validators.validUserIdFormat("1234567AB"));
        assertFalse(Validators.validUserIdFormat("12345-789"));
        assertFalse(Validators.validUserIdFormat("12345678"));
        assertFalse(Validators.validUserIdFormat("1234567890"));
        assertFalse(Validators.validUserIdFormat("12345"));
        assertFalse(Validators.validUserId("A23456789", existingUserIds));
        assertFalse(Validators.validUserId("1234567AB", existingUserIds)); 
        assertFalse(Validators.validUserId("12345-789", existingUserIds));
        assertFalse(Validators.validUserId("12345678", existingUserIds));
        assertFalse(Validators.validUserId("1234567890", existingUserIds));
        assertFalse(Validators.validUserId("12345", existingUserIds));
    }

    @Test
    void validUserIdDuplicateIdTest() {
        assertTrue(Validators.validUserIdFormat("123456789"));
        assertFalse(Validators.validUserId("123456789", existingUserIds));
    }

    // ========== validMovieTitle Tests ==========

    @Test
    void validMovieTitleNullTest() {
        assertFalse(Validators.validMovieTitle(null));
    }

    @Test
    void vValidMovieTitleEmptyTest() {
        assertFalse(Validators.validMovieTitle(""));
        assertFalse(Validators.validMovieTitle("   "));
    }

    @Test
    void validMovieTitleWithSpaceTest() {
        assertFalse(Validators.validMovieTitle(" The Matrix"));
        assertFalse(Validators.validMovieTitle("The Matrix "));
    }

    @Test
    void validMovieTitleSingleWordTest() {
        assertTrue(Validators.validMovieTitle("Matrix"));
        assertTrue(Validators.validMovieTitle("Inception"));
        assertFalse(Validators.validMovieTitle("matrix"));
        assertTrue(Validators.validMovieTitle("Matrix54"));
    }

    @Test
    void validMovieTitleMultipleWordsTest() {
        assertTrue(Validators.validMovieTitle("The Matrix"));
        assertFalse(Validators.validMovieTitle("the Matrix"));
        assertFalse(Validators.validMovieTitle("The matrix"));
        assertTrue(Validators.validMovieTitle("Fast And Furious"));
        assertTrue(Validators.validMovieTitle("The Matrix2"));
        assertTrue(Validators.validMovieTitle("Fast And Furious7"));
        assertFalse(Validators.validMovieTitle("2001 A Space Odyssey"));
    }

    @Test
    void validMovieTitleWithSpecialCharactersTest() {
        assertFalse(Validators.validMovieTitle("The-Matrix"));
        assertFalse(Validators.validMovieTitle("The@Matrix"));
        assertFalse(Validators.validMovieTitle("The M@trix"));
        assertFalse(Validators.validMovieTitle("The_Matrix"));
    }

    // ========== validMovieId Tests ==========

    @Test
    void validMovieIdNullTest() {
        assertFalse(Validators.validMovieIdLetters(null, "The Matrix"));
        assertFalse(Validators.validMovieIdUniqueNumbers(null, "The Matrix"));
        assertFalse(Validators.validMovieId(null, "The Matrix", existingMovieIds));
    }

    @Test
    void validMovieIdEmptyTest() {
        assertFalse(Validators.validMovieIdLetters("", "The Matrix"));
        assertFalse(Validators.validMovieIdUniqueNumbers("", "The Matrix"));
        assertFalse(Validators.validMovieId("", "The Matrix", existingMovieIds));

        assertFalse(Validators.validMovieIdLetters("   ", "The Matrix"));
        assertFalse(Validators.validMovieIdUniqueNumbers("   ", "The Matrix"));
        assertFalse(Validators.validMovieId("   ", "The Matrix", existingMovieIds));
    }

    @Test
    void validMovieIdStartsWithSpaceTest() {
        assertFalse(Validators.validMovieIdLetters(" TM123", "The Matrix"));
        assertTrue(Validators.validMovieIdUniqueNumbers(" TM123", "The Matrix"));
        assertFalse(Validators.validMovieId(" TM123", "The Matrix", existingMovieIds));

        assertFalse(Validators.validMovieIdLetters("TM123 ", "The Matrix"));
        assertFalse(Validators.validMovieIdUniqueNumbers("TM123 ", "The Matrix"));
        assertFalse(Validators.validMovieId("TM123 ", "The Matrix", existingMovieIds));
    }

    @Test
    void validMovieIdWrongCapitalLettersTest() {
        assertFalse(Validators.validMovieIdLetters("ABC123", "The Matrix"));
        assertTrue(Validators.validMovieIdUniqueNumbers("ABC123", "The Matrix"));
        assertFalse(Validators.validMovieId("ABC123", "The Matrix", existingMovieIds));

        assertFalse(Validators.validMovieIdLetters("M123", "The Matrix"));
        assertTrue(Validators.validMovieIdUniqueNumbers("M123", "The Matrix"));
        assertFalse(Validators.validMovieId("M123", "The Matrix", existingMovieIds));

        assertFalse(Validators.validMovieIdLetters("tm123", "The Matrix"));
        assertTrue(Validators.validMovieIdUniqueNumbers("tm123", "The Matrix"));
        assertFalse(Validators.validMovieId("tm123", "The Matrix", existingMovieIds));
    }


    @Test
    void validMovieIdWrongDigitCountTest() {
        assertTrue(Validators.validMovieIdLetters("TM12", "The Matrix"));
        assertFalse(Validators.validMovieIdUniqueNumbers("TM12", "The Matrix"));
        assertFalse(Validators.validMovieId("TM12", "The Matrix", existingMovieIds));

        assertTrue(Validators.validMovieIdLetters("TM1234", "The Matrix"));
        assertFalse(Validators.validMovieIdUniqueNumbers("TM1234", "The Matrix"));
        assertFalse(Validators.validMovieId("TM1234", "The Matrix", existingMovieIds));
    }

    @Test
    void validMovieIdNonNumericDigitsTest() {
        assertTrue(Validators.validMovieIdLetters("TM12A", "The Matrix"));
        assertFalse(Validators.validMovieIdUniqueNumbers("TM12A", "The Matrix"));
        assertFalse(Validators.validMovieId("TM12A", "The Matrix", existingMovieIds));

        assertFalse(Validators.validMovieIdLetters("TM12#", "The Matrix"));
        assertFalse(Validators.validMovieIdUniqueNumbers("TM12#", "The Matrix"));
        assertFalse(Validators.validMovieId("TM12#", "The Matrix", existingMovieIds));

        assertFalse(Validators.validMovieIdLetters("TM123#", "The Matrix"));
        assertFalse(Validators.validMovieIdUniqueNumbers("TM123#", "The Matrix"));
        assertFalse(Validators.validMovieId("TM123#", "The Matrix", existingMovieIds));

        assertFalse(Validators.validMovieIdLetters("TMABC", "The Matrix"));
        assertFalse(Validators.validMovieIdUniqueNumbers("TMABC", "The Matrix"));
        assertFalse(Validators.validMovieId("TMABC", "The Matrix", existingMovieIds));

        assertFalse(Validators.validMovieIdLetters("TM#123", "The Matrix"));
        assertTrue(Validators.validMovieIdUniqueNumbers("TM#123", "The Matrix"));
        assertFalse(Validators.validMovieId("TM#123", "The Matrix", existingMovieIds));

        assertFalse(Validators.validMovieIdLetters("TMA123", "The Matrix"));
        assertTrue(Validators.validMovieIdUniqueNumbers("TMA123", "The Matrix"));
        assertFalse(Validators.validMovieId("TMA123", "The Matrix", existingMovieIds));
    }

    @Test
    void validMovieIdNonUniqueDigitsTest() {
        assertTrue(Validators.validMovieIdLetters("TM112", "The Matrix"));
        assertFalse(Validators.validMovieIdUniqueNumbers("TM112", "The Matrix"));
        assertFalse(Validators.validMovieId("TM112", "The Matrix", existingMovieIds));

        assertTrue(Validators.validMovieIdLetters("TM222", "The Matrix"));
        assertFalse(Validators.validMovieIdUniqueNumbers("TM222", "The Matrix"));
        assertFalse(Validators.validMovieId("TM222", "The Matrix", existingMovieIds)); 
    }

    @Test
    void validMovieIdCorrectFormatTest() {
        assertTrue(Validators.validMovieIdLetters("TM123", "The Matrix"));
        assertTrue(Validators.validMovieIdUniqueNumbers("TM123", "The Matrix"));
        assertTrue(Validators.validMovieId("TM123", "The Matrix", existingMovieIds));

        assertTrue(Validators.validMovieIdLetters("FAF456", "Fast And Furious"));
        assertTrue(Validators.validMovieIdUniqueNumbers("FAF456", "Fast And Furious"));
        assertTrue(Validators.validMovieId("FAF456", "Fast And Furious", existingMovieIds));

        assertTrue(Validators.validMovieIdLetters("M234", "Matrix2"));
        assertTrue(Validators.validMovieIdUniqueNumbers("M234", "Matrix2"));
        assertTrue(Validators.validMovieId("M234", "Matrix2", existingMovieIds));
    }

    @Test
    void validMovieIdDuplicateMovieIdTest() {
        assertTrue(Validators.validMovieIdLetters("EDR145", "Evil Dead Rise"));
        assertTrue(Validators.validMovieIdUniqueNumbers("EDR145", "Evil Dead Rise"));
        assertFalse(Validators.validMovieId("EDR145", "Evil Dead Rise", existingMovieIds));
    }
}

