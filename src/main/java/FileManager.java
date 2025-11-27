import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();

        try (FileReader fileReader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }

        } catch (IOException e) {
            System.out.println("Failed to read file " + filePath);
        }

        return lines;
    }

    public void writeFile(String filePath, List<String> content) {
        try (FileWriter fileWriter = new FileWriter(filePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            
            for (String line : content) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file " + filePath);
        }
    }
}
