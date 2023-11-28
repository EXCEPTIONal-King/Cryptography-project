package project;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

//Handles procedure of sending and preparing to send data
public class Network {

    //object to be sent between Alice and Bob
    public static class NetworkData implements Serializable {
        /*String
        char[]
        byte[]

                //collision-resistant one-way function
                //hash a files contents
        int Signature*/



        // This is temp, but I'm not sure what is being sent after encryption so currently I'm just putting a
        // byte array here
        byte[] data;

        public NetworkData(byte[] data) {
            this.data = data;
        }

    }

    public static NetworkData generateNetworkDataFromFile(String filepath) {
        try (FileInputStream inputStream = new FileInputStream(filepath)) {
            // Determine the size of the file (near 500MB)
            long fileSize = inputStream.available();

            // Create a byte array to hold the file content
            byte[] fileContent = new byte[(int) fileSize];

            // Read the file content into the byte array
            inputStream.read(fileContent);

            // Generate the NetworkData object
            return new NetworkData(fileContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
