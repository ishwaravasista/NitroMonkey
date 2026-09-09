import java.nio.file.*;
import java.util.List;
import java.io.IOException;

/**
 * Utility class responsible for loading word lists from files on disk.
 * Each word list is expected to be in the "wordlists" directory.
 */
public class WordListLoader
{
    /**
     * Reads all lines from the specified word list file and returns them as a List of Strings.
     * 
     * @param fileName the name of the word list file located in the "wordlists" folder
     * @return a List of words read from the file, in their original order
     * @throws RuntimeException if the file cannot be read or an I/O error occurs
     */
    public static List<String> load(String fileName)
    {
        Path path = Paths.get("wordlists", fileName);
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load word list: " + fileName, e);
        
        }
    }

}