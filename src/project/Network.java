package project;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

//Handles procedure of sending and preparing to send data
public class Network {

    //object to be sent between Alice and Bob
    public static class NetworkData implements Serializable {
        public byte[] data;
        public byte[] signature;
        public PublicKey public_key;

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

            //hash file contents
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            byte[] hashedBytes = digest.digest(fileContent);
            // Generate the NetworkData object
            CryptoUtil signer = new CryptoUtil();

            NetworkData toSend = new NetworkData(hashedBytes); //use data
            toSend.signature = signer.signData(toSend.data);
            toSend.public_key = signer.getPublicKey();
            return toSend;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
