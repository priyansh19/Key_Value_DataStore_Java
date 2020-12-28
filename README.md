# Key_Value_DataStore in Java

This is a simple key_value datastore to implement few of the basic functionalities. There are some Functional as well as Non-Functional features are added while building this datastore. It implements CRD (Create, Read, Delete functionalities) as of now with a most demanded feature which is TTL ( Time-To-Live ) for a specific key.

## :memo: Few points to remember before compiling the aplication:

- It is built using Java 11
- Java being a **Platform Independent** language, this application can run on all type of Operating Systems.
- User can edit the path in **KeyValueDataStore** file
- In this application will set path from the class using JVM Flags defined at the starting of main class.
- We have assumed that the input provided will be in the range limits of the **String** charachter
- Appropriate error responses must always be returned to a User if he uses the data store in unexpected ways or breaches any limits.

## :rocket: Functional Requirements

1. Path of the storage can be changed in **KeyValueDataStore.java** file. By default it is set to create files in the folder in which the execution takes place.
2. A new key-value pair can be added to the data store using the Create operation. The key is always a string - capped at 32chars. The value is always a JSON object - capped at 16KB. (This is done using Base 64 encoding).
3. If **Create** is invoked for an existing key, an appropriate error will be returned.
4. Key field can't be left blank for any key value entry.
5. While using the **Read** and **Delete** functions the keys firstly they will undergo checking for their TTL value if the key have crossed the TTL time limits then it will be removed from the data store.
6. if key is still in TTL limit it will be displayed in READ operation.
7. Read operation will give the output as **JSON Object**.

   #### TTL (Time To Live):

   - Every key supports setting a **Time-To-Live** property when it is created. This property is optional. If provided, it will be evaluated as an integer defining the number of seconds the key must be retained in the data store. Once the Time-To-Live for a key has expired, the key will no longer be available for **Read** and **Delete** operations.
   - By default in this application it is set to 7 days (can be changed)

## :rainbow: Non-functional Requirements

1. The size of the file storing data must never exceed 1GB.
2. More than one client process is allowed to use the same file as a data store at any given time. **Thread safety** is included in the program so more then one clients can access the data store at the same time.
3. **Proper synchronized shared file locks** are there so that _n_ number of process can use the shared data at the same time keeping the integrity of data consistent
4. A client process is allowed to access the data store using multiple threads, if it desires to. The data store is therefore made **thread-safe**.
5. The client will bear as **little memory**costs as possible to use this data store, while
    deriving maximum performance with respect to response times for accessing the data
    store.

#### Important Note:
  
 - **Security** : Value has been encoded as Base64 string instead of storing it as Plaintext. Also it is useful to store complex JSOn with many newlines as single line. So it can be efficiently retrieved from file
 - **Memory** : Not loading entire file into memory since it can be huge(upto 1GB) and will take up lots of memory. So loading one line into memory at a time and processing it.
 - **Performance** : Using BufferedReader and BufferedWriter so that it will use buffer(8 KB) and read/write in batch instead of reading/writing single character.
 - **Performance** : For faster retrieval, whenever our application starts, we will be reading from file and populate the ConcurrentHashMap. 2 Maps hold KeyValuePairs, KeyAndTTL info.
 - **Adaptability** : Provided a way to change default values through JVM flags

# :Rocket: How to use the application:

1) The Applications is based on REPL(Read, Eval, Print, Loop), an interactive commandline interface.
2) It is a file-based key-value data store that supports the basic CRD (create, read, and delete)
operations. This data store is meant to be used as a local storage for one single process on one
laptop.
3) The key-value will be stored in (./data/db.txt) file along with metadata file which contains in a separate (./data/db-meta.txt) file.
4) If you want to modify default values, add the jvm flags while starting the application.
    
   -  To set DB file to some other location: -KeyValueDataStore.dbfilepath=<YOUR_DB_FILEPATH>

   - To set DB metadata file to some other location: -KeyValueDataStore.dbfilepath=<YOUR_DB_META_FILEPATH>

   - To set default TTl time : <Time_in_millisecs> //By default it is 604800(i.e. 7 day)

5) If you use VScode then just import the project and click on run button showing just above the main function ( Ensure that the java language pakage is instaled from marketplace ).

6) For users without vscode follow these steps:
    ```bash
    $> cd {project folder loacation}
    $> cd KeyValueDataStore/src
    $> javac KeyValueDataStore.java ## this will generate the byte code for jvm 
    $> java KeyValueDataStore

    ## you are good to go now ... program will run :)
```
