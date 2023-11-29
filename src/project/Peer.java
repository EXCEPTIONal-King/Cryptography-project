package project;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Peer {

    private final int port;
    private final String messagesPath;
    private final int remotePort;
    private final String remoteHost;
    private final Network.NetworkData[] remoteData;
    private boolean verification;
    private Network.NetworkData[] localData;

    public Peer(int port, String messagesPath, int remotePort, String remoteHost) {
        this.port = port;
        this.messagesPath = messagesPath;
        this.remotePort = remotePort;
        this.remoteHost = remoteHost;
        this.remoteData = new Network.NetworkData[5];
        this.localData = new Network.NetworkData[5];
        this.verification = true;

        Thread listener = new Thread(this::startListener);
        listener.start();

        // Let both listeners start before attempting to send
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Thread sender = new Thread(this::startSender);
        sender.start();

        try {
            listener.join();
            sender.join();
        } catch (Exception e) {

        }
        if (!verification) {
            System.out.println("Signature Verification failed!");
            System.out.println("Aborting Comparisons");
        } else {
            for(int i = 0; i < 5; i++) {
                int returned_index = compareData(remoteData[i].data);
                if(returned_index != -1) {System.out.println("Remote file " + (i+1) + "  matches local file:  " + (returned_index+1)); }
                else System.out.println("Remote file " + (i+1) + " has no local matches");

            }
        }


    }

    private void startListener() {
        try (ServerSocket listenerSocket = new ServerSocket(this.port)) {
            Socket remoteSocket = listenerSocket.accept(); // Blocks until connection
            System.out.printf("Listener accepted remote port (%d) connection.\n", remoteSocket.getPort());
            ObjectInputStream remoteInput = new ObjectInputStream(remoteSocket.getInputStream());

            for (int i = 0; i < 5; i++) {
                Network.NetworkData remoteData = (Network.NetworkData) remoteInput.readObject(); // Blocks until read
                this.remoteData[i] = remoteData;
                CryptoUtil verifier = new CryptoUtil();
                if(!verifier.verifySignature(remoteData.data, remoteData.signature, remoteData.public_key)){//remoteData verification (public key, remote data, and signature)
                    this.verification = false;
                }
                //check if we have hash locally
                System.out.printf("Received object %d from remoteSocket: %s\n", i, remoteData.toString());
            }
            remoteSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void startSender() {
        try (Socket senderSocket = new Socket(this.remoteHost, this.remotePort)) {
            ObjectOutputStream remoteOutput = new ObjectOutputStream(senderSocket.getOutputStream());

            for (int i = 0; i < 5; i++) {

                Network.NetworkData dataToSend = Network.generateNetworkDataFromFile(this.messagesPath +
                        "/codeSegment" + (i+1) + ".bin"); //modify to give hashed data
                localData[i] = dataToSend;

                remoteOutput.writeObject(dataToSend); // Blocks until sending
                remoteOutput.flush();
                System.out.printf("Sent object %d to remoteSocket: %s\n", i, dataToSend);


            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //checks remote hash against local hashes
    //index of match if any
    public int compareData(byte[] remoteData) {

        //checks remote data against all local for identical hashes
        for (int i = 0; i < 5; i++) {
            if (Arrays.equals(remoteData, localData[i].data)) return i;
        }
        return -1;


    }

}
