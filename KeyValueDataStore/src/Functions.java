import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class Functions {

	public static boolean writeToFile(String filePath, Object value, boolean append) {
		File file = new File(filePath);
		if (!file.canWrite() || (file.length() + (16 * 1024)) > 1073741824L) // if File size + current data(16KB)
		// exceeds 1 GB or File is not writable error
		{
			return false;
		}
		try (PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(file, append)))) {
			pr.println(value);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static Map<String, String> getAllLinesFromFile(String dbFilePath) {
		return null;
	}

}
