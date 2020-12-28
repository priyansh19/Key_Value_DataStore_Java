# Key_Value_DataStore in Java

This is a simple key_value datastore to implement few of the basic functionalities. There are some Functional as well as Non-Functional features are added while building this datastore. It implements CRD (Create, Read, Delete functionalities) as of now with a most demanded feature which is TTL ( Time-To-Live ) for a specific key. 

## Few points to remember before compiling the aplication:

- It is built using Java 11
- Java being a **Platform Independent** language, this application can run on all type of Operating Systems.
- User can edit the path in **KeyValueDataStore** file
- In this application will set path from the class using JVM Flags defined at the starting of main class.
- We have assumed that the input provided will be in the range limits of the **String** charachter
- Appropriate error responses must always be returned to a User if he uses the data store in unexpected ways or breaches any limits.

# Functional Requirements

1. Path of the storage can be changed in **KeyValueDataStore.java** file. By default it is set to create files in the folder in which the execution takes place.
2. A new key-value pair can be added to the data store using the Create operation. The key is always a string - capped at 32chars. The value is always a JSON object - capped at 16KB. (This is done using Base 64 encoding).
3. If **Create** is invoked for an existing key, an appropriate error will be returned.
4. Key field can't be left blank for any key value entry.
5. While using the **Read** and **Delete** functions the keys firstly they will undergo checking for their TTL value if the key have crossed the TTL time limits then it will be removed from the data store.
6. if key is still in TTL limit it will be displayed in READ operation.
7. Read operation will give the output as **JSON Object**.
   
### Note

- Every key supports setting a **Time-To-Live** property when it is created. This property is optional. If provided, it will be evaluated as an integer defining the number of seconds the key must be retained in the data store. Once the Time-To-Live for a key has expired, the key will no longer be available for **Read** and **Delete** operations.
- By default in this application it is set to 7 days (can be changed).