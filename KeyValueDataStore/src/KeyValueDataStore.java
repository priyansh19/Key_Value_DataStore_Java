import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class KeyValueDataStore implements Serializable{
    private static int counter = 0;
    private static Scanner scanner = new Scanner(System.in);
    private static final KeyValueDataStore INSTANCE = new KeyValueDataStore();
    private static final long ONE_SEC_IN_MILLIS = 1000; // 1 sec equals to 1000 millisecs
    private static Map<String, String> keyValueMap = new TreeMap<>(); // Making the class Thread-safe with the use of map here.
    private static Map<String, String> metadataMap = new TreeMap<>(); // Also here we have used Treemap to keep the values in the datastore in sorted order.
    static String DELIMITER = ":";
    
    private KeyValueDataStore(){ 
    }

    public static KeyValueDataStore getinstance(){
        return KeyValueDataStore.INSTANCE;
    }

    @SuppressWarnings("unused")
    private KeyValueDataStore readResolve() {
        return KeyValueDataStore.INSTANCE;
    }
    public static void main(String[] args){
        while(true){
            try {
                if (counter > 0) {
                    System.out.println("Starting Application Again !! ... hold on ..");
                    TimeUnit.SECONDS.sleep(2);
                }
                System.out.println("\n<------------------- Welcome to Priyansh's Key-Value DataStore -------------------->");
                System.out.println("Features in DataStore: \n" +
                                    "   Type 1 : (Create) To Create Key-Value pair. \n" +
                                    "   Type 2 : (Read) To Read the Key-Value pair. \n" +
                                    "   Type 3 : (Delete) To Delete the values by Key.");
                System.out.print("Enter your choice here:");
                int input = scanner.nextInt();
                System.out.println("Entered choice:" + input);
    
                // defining the inputs to be used for handling the key,value, TTL Value.
                String key = null, value = null;
                Long TTLVal = 0L;
                Calendar cal = Calendar.getInstance(); 
                switch (input) {
                    case 1:
                        System.out.println("Enter your Key:");
                        key = scanner.nextLine();
                        if(key.equals("")){
                            System.err.println("FAILED ! : Key cannot be an empty field.");
                            continue;
                        }
                        break;
                
                    case 2:
                        break;
    
                    case 3:
                        break;
    
                    default:
                        System.out.println("OOPS !! wrong choice. Try again :)");
                        break;
                }
                counter ++;
            } catch (Exception e) {
                continue;
            }
        }
    }
}
