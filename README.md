# Movie Recommendation System

A simple Java-based movie recommendation engine built for software testing practice and file-based data handling without a database. It reads plain-text files that describe movies and users, validates them, and produces genre-based recommendations for each user.

## Contents

- [Requirements](#requirements)
- [Project Structure](#project-structure)
- [Processing Flow](#processing-flow)
- [Data Formats](#data-formats)
- [Running the App](#running-the-app)
- [Running Tests](#running-tests)
- [Troubleshooting](#troubleshooting)

## Requirements

- JDK 8 or later
- Maven 3.8+
- Any shell (PowerShell, CMD, Bash)

## Project Structure

```
src/
  main/
    java/
      MovieRecommendationSystem.java   # entry point & workflow
      Movie.java                      # movie entity
      User.java                       # user entity
      FileManager.java                # file I/O helpers
      Validators.java                 # input validation helpers
    files/
      movies.txt                      # movie dataset
      users.txt                       # user dataset
      recommendations.txt             # output file
  test/
    java/
      *Test.java                      # JUnit tests (FileManager, Validators, System)
    files/
      test*.txt                       # test fixtures
```

## Processing Flow

1. `loadData`: reads the movie and user files, mapping every two lines to a `Movie` or `User`.
2. `validateData`: ensures names, IDs, and uniqueness rules are valid via `Validators`.
3. `createRecommendedMovies`: derives favorite genres per user and suggests unseen movies sharing those genres.
4. `writeRecommendedMovies`: writes either each user’s recommendations or the first detected error to `recommendations.txt`.
5. `printMovies/printUsers`: logs data to the console for manual inspection.

## Data Formats

- `movies.txt`
  ```
  MovieTitle, MOVID123
  Genre1, Genre2, Genre3
  ```

- `users.txt`
  ```
  User Name, 123456789
  MOVID123, MOVID456
  ```

- `recommendations.txt` (output)
  ```
  User Name, 123456789
  Recommended Movie 1, Recommended Movie 2
  ```

## Running the App

1. Update `src/main/files/` with your own movie and user data.
2. Run the application:
   ```
   mvn compile exec:java -Dexec.mainClass=MovieRecommendationSystem
   ```
   (or run `MovieRecommendationSystem.main` from your IDE).
3. Check the console output and the updated `src/main/files/recommendations.txt`.

## Running Tests

- Execute all JUnit tests:
  ```
  mvn test
  ```
- Test reports (text + XML) live in `target/surefire-reports/`.

## Troubleshooting

- **Missing files**: `main` aborts if `movies.txt` or `users.txt` can’t be found at the expected paths.
- **Malformed input**: the first validation error is added to `errors` and written to `recommendations.txt`.
- **Unknown movie IDs**: if a user likes an ID absent from `movies.txt`, the system logs exactly which IDs are invalid.
- **Cleaning up**: clear `recommendations.txt` or rerun the pipeline after fixing input issues.