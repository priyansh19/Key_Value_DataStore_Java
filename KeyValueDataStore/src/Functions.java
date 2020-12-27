import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.TimeUnit;

// This file contains all the file handling functions along with processing on it 
public class Functions {

	public static boolean writeToFile(String filePath, Object value, boolean append)
			throws InterruptedException, IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("File not exist !! .. Hold On a bit .. We are creating file at:" + filePath);
			TimeUnit.SECONDS.sleep(2);
			File folder = new File("data");
			File f1 = new File(filePath);
			folder.mkdir();
			f1.createNewFile();
		}
		if (!file.canWrite() || (file.length() + (16 * 1024)) > 1073741824L)
		// if File size + current data(16KB)
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

	public static void deleteLineInFile(String filePath, String key) {

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
				PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(new File(filePath))))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith(key)) {
					continue;
				}
				pr.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, String> getAllLinesFromFile(String dbFilePath) {
		return null;
	}

	public static String getLineFromFile(String filePath, String key) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith(key)) {
					return line;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}
