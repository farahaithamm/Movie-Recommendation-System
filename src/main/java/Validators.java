import java.util.HashSet;
import java.util.Set;

public class Validators {

    // ========== User Validation ==========
    public static boolean validUserName(String userName) {
        if (userName == null || userName.trim().isEmpty()) return false;
        if (userName.charAt(0) == ' ') return false;
        return userName.matches("^[A-Za-z]+( [A-Za-z]+)*$");
    }
    
    public static boolean validUserIdFormat(String userId) {
        if (userId == null || userId.trim().isEmpty() || userId.length() != 9) return false;
        return userId.matches("^\\d{8}[A-Za-z]?$|^\\d{9}$");
    }

    public static boolean validUserId(String userId, Set<String> UsersIds) {
        boolean trueFormat = validUserIdFormat(userId);
        return trueFormat && (!UsersIds.contains(userId));
    }
    
    // ========== Movie Validation ==========
    public static boolean validMovieTitle(String movieTitle) {
        if (movieTitle == null || movieTitle.trim().isEmpty()) return false;
        if (movieTitle.charAt(0) == ' ' || movieTitle.charAt(movieTitle.length() - 1) == ' ') return false;
        return movieTitle.matches("^([A-Z][a-zA-Z0-9]*)( [A-Z][a-zA-Z0-9]*)*$");
    }

    public static boolean validMovieIdLetters(String movieId, String title){
        if (movieId == null || movieId.trim().isEmpty()) return false;
        
        for (int i = 0; i < movieId.length(); i++) {
            char c = movieId.charAt(i);
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }

        String expectedStart = title.replaceAll("[^A-Z]", "");

        int firstDigitIndex = -1;
        for (int i = 0; i < movieId.length(); i++) {
            if (Character.isDigit(movieId.charAt(i))) {
                firstDigitIndex = i;
                break;
            }
        }
        
        if (firstDigitIndex == -1) return false;

        String lettersPart = movieId.substring(0, firstDigitIndex);

        if (!lettersPart.equals(expectedStart)) return false;
        return true;
    }
    public static boolean validMovieIdUniqueNumbers(String movieId, String title){
        if (movieId == null || movieId.trim().isEmpty()) return false;

        int firstDigitIndex = -1;
        for (int i = 0; i < movieId.length(); i++) {
            if (Character.isDigit(movieId.charAt(i))) {
                firstDigitIndex = i;
                break;
            }
        }
        
        if (firstDigitIndex == -1) return false;
        
        String digits = movieId.substring(firstDigitIndex);
        if (!digits.matches("\\d{3}")) return false;
        
        return true;
    }

    public static boolean validMovieId(String movieId, String title, Set<String> MoviesIds) {
        boolean trueLetters = validMovieIdLetters(movieId, title);
        boolean trueNumbers = validMovieIdUniqueNumbers(movieId, title);

        if (!trueLetters || !trueNumbers) return false;
        if (MoviesIds.contains(movieId)) return false;

        String currentDigits = movieId.substring(movieId.length() - 3);

        for (String existingId : MoviesIds) {
            String existingDigits = existingId.substring(existingId.length() - 3);
            if (existingDigits.equals(currentDigits)) {
                return false;
            }
        }

        return true;
    }
}

