import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Calendar;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class KeyValueDataStore implements Serializable {
    private static int counter = 0;
    private static Scanner scanner = new Scanner(System.in);
    private static final KeyValueDataStore INSTANCE = new KeyValueDataStore();
    private static final long ONE_SEC_IN_MILLIS = 1000; // 1 sec equals to 1000 millisecs
    private static Map<String, String> keyMap = new TreeMap<>();
    // Making the class Thread-safe with the use of map here
    private static Map<String, String> metadataMap = new TreeMap<>();
    // Also here we have used Treemap to keep the values in the datastore in sorted
    // order.
    static String DELIMITER = ":";

    // fille paths for data storgae .. can only be changed from here
    private static String dbFilePath = System.getProperty("KeyValueDataStore.dbfilepath", "./data/db.txt");
    private static String metaFilePath = System.getProperty("KeyValueDataStore.metafilepath", "./data/db-meta.txt");
    private static Integer defaultTTL = Integer.valueOf(System.getProperty("KeyValueDataStore.defaultTTL", "604800"));
    // 7 days equals to 604800 seconds

    private KeyValueDataStore() {
    }

    public static KeyValueDataStore getinstance() {
        return KeyValueDataStore.INSTANCE;
    }

    // @SuppressWarnings("unused")
    // private KeyValueDataStore readResolve() {
    // return KeyValueDataStore.INSTANCE;
    // }

    private String createKeyValue(String key, String value) {
        StringBuilder stringBuilder = new StringBuilder();
        // Encode data using Base64 Encoding Technique
        String base64EncodedValue = Base64.getEncoder().encodeToString(value.getBytes());
        stringBuilder.append(key).append(DELIMITER).append(base64EncodedValue);
        return stringBuilder.toString();
    }

    private String createKeyWithTTL(String key, Long tTLVal) {
        return key + DELIMITER + tTLVal.toString();
    }

    public String getDecodedValue(String keyValue) {
        // Decode data using Base64
        byte[] decodedBytes = Base64.getDecoder().decode(keyValue);
        String decodedStr = new String(decodedBytes, Charset.defaultCharset());
        return decodedStr;
    }

    // private void preProcess(String dbFilePath, String metaFilePath) {
    // this.createFile(dbFilePath);
    // this.createFile(metaFilePath);
    // this.keyMap = Functions.getAllLinesFromFile(dbFilePath);
    // this.metadataMap = Functions.getAllLinesFromFile(metaFilePath);
    // }

    private void DeleteKey(String key) {
        Functions.deleteLineInFile(dbFilePath, key);
        keyMap.remove(key);
        Functions.deleteLineInFile(metaFilePath, key);
        metadataMap.remove(key);
        System.err.println("FAILED: Key expired and it has been removed from DataStore");
    }

    public static void main(String[] args) {
        boolean terminate = false;
        while (!terminate) {
            try {
                if (counter > 0) {
                    System.out.println("!------------------------------------------------------------------------! \n"
                            + "Starting Application Again !! ... hold on ");
                    TimeUnit.SECONDS.sleep(2);
                }
                System.out.println(
                        "\n<------------------- Welcome to Priyansh's Key-Value DataStore -------------------->");
                System.out.println("Features in DataStore: \n" + "   Type 1 : (Create) To Create Key-Value pair. \n"
                        + "   Type 2 : (Read) To Read the Key-Value pair. \n"
                        + "   Type 3 : (Delete) To Delete the values by Key. \n"
                        + "   Type 4 : To exit the application.");
                System.out.print("Enter your choice here:");
                int input = scanner.nextInt();
                scanner.nextLine();
                // defining the inputs to be used for handling the key,value, TTL Value.
                String key = null, value = null;
                Long TTLVal = 0L;
                Calendar cal = Calendar.getInstance();
                switch (input) {
                    case 1:
                        System.out.println("Enter your Key:");
                        key = scanner.nextLine();
                        if (key.equals("")) {
                            System.err.println("--> FAILED! : Key cannot be an empty field.");
                            continue;
                        }
                        // checking for redundancy (duplicacy) of the input key
                        if (keyMap.containsKey(key)) {
                            System.err.println(
                                    "--> FAILED! : Key Already exists in the Data Store. Try again using other key.");
                            continue;
                        }
                        System.out.println("Enter the Value co-responding to the key: ");
                        value = scanner.nextLine();
                        if (value == "") {
                            System.err.println("--> FAILED! : Value failed can't be empty. Try agin !");
                            continue;
                        }
                        System.out.println(
                                "Enter the Time To Live duration (in seconds) of this key [type 0 for default that is 7 days]: ");
                        TTLVal = scanner.nextLong();

                        if (TTLVal <= 0) { // edge case 1 settingttl valto default 7 days
                            System.out.println("Setting TTL value to default i.e 7 days");
                            TTLVal = cal.getTimeInMillis() + (defaultTTL * ONE_SEC_IN_MILLIS);
                        } else {
                            TTLVal = cal.getTimeInMillis() + (TTLVal * ONE_SEC_IN_MILLIS);
                        }
                        // till now all the error handling is done now the file handling part starts
                        String key_value = KeyValueDataStore.INSTANCE.createKeyValue(key, value);
                        boolean result = Functions.writeToFile(dbFilePath, key_value, true);

                        if (result) {
                            keyMap.put(key, value);
                            result = Functions.writeToFile(metaFilePath,
                                    KeyValueDataStore.INSTANCE.createKeyWithTTL(key, TTLVal), true);
                            metadataMap.put(key, TTLVal.toString());
                            if (result)
                                System.out.println("--> SUCCESS: Key-Value pair has been added successfully");
                        } else {
                            System.err.println(
                                    "--> FAILED! : Reason can be File doesn't have write permission or File is too big to append data.\n");
                        }
                        break;

                    case 2:
                        System.out.println("Enter the key: ");
                        key = scanner.nextLine();
                        if (key == "") {
                            System.err.println("--> FAILED! : Key field can't be empty. Try again!");
                            continue;
                        }
                        if (!keyMap.containsKey(key)) {
                            System.err.println("--> FAILED RETRIEVAL! : key is not present in the Data Store.");
                            continue;
                        }
                        TTLVal = Long.valueOf(metadataMap.get(key));
                        if (cal.getTimeInMillis() > TTLVal) {
                            KeyValueDataStore.INSTANCE.DeleteKey(key);
                            continue;
                        }
                        // Now the retrieval process of the key begins
                        String str = Functions.getLineFromFile(dbFilePath, key);
                        int positionOfDelimeter = str.indexOf(DELIMITER);
                        String EncodedValue = str.substring(positionOfDelimeter + 1);
                        value = KeyValueDataStore.INSTANCE.getDecodedValue(EncodedValue);
                        String KeyValPair = String.format("SUCCESS \n Key : %s, Value : %s", key, value);
                        System.out.println(KeyValPair);
                        break;

                    case 3:
                        // TODO for delete functionality
                        // TODO : adding some error responses in read functionality
                        // update feature will also be added in future; to make it a fully functional
                        // crud operational data store
                        break;

                    case 4:
                        System.out.println("Exiting now ...");
                        terminate = true;
                        TimeUnit.SECONDS.sleep(2);
                        break;

                    default:
                        System.out.println("OOPS !! wrong choice. Try again :)");
                        break;
                }
                counter++;
            } catch (Exception e) {
                continue;
            }
        }
    }
}
