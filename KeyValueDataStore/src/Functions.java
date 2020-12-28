import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

	public static void deleteLineInFile(String filePath, String key) throws IOException {
		final ReentrantLock lock = new ReentrantLock();
		try {
			lock.lock();
			File inputFile = new File(filePath);
			File tempFile = new File("tempfile.txt");

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String lineToRemove = key;
			String currentLine;

			while ((currentLine = reader.readLine()) != null) {
				// trim newline when comparing with lineToRemove
				if (currentLine.startsWith(lineToRemove))
					continue;
				writer.write(currentLine + System.getProperty("line.separator"));
			}
			writer.close();
			reader.close();
			tempFile.renameTo(inputFile);
		} finally {
			lock.unlock();
		}
	}

	// there are 2 ways of locking a file while using thread safety functionality
	// 1. using reentrantLock and locking it and unlockign it after a while
	// 2. using object lock
	public static String getLineFromFile(String filePath, String key) {
		final Object lock = new Object();
		try {
			synchronized (lock) {
				File readFile = new File(filePath);
				BufferedReader reader = new BufferedReader(new FileReader(readFile));

				String linetofind = key;
				String currline;

				while ((currline = reader.readLine()) != null) {
					if (currline.startsWith(linetofind)) {
						return currline;
					}
				}

				reader.close();
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		return "";
	}

}
