package project;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Peer {

    private final int port;
    private final String messagesPath;
    private final int remotePort;
    private final String remoteHost;
    private final Network.NetworkData[] remoteData;

    public Peer(int port, String messagesPath, int remotePort, String remoteHost) {
        this.port = port;
        this.messagesPath = messagesPath;
        this.remotePort = remotePort;
        this.remoteHost = remoteHost;
        this.remoteData = new Network.NetworkData[5];

        new Thread(this::startListener).start();

        // Let both listeners start before attempting to send
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(this::startSender).start();
    }

    private void startListener() {
        try (ServerSocket listenerSocket = new ServerSocket(this.port)) {
            Socket remoteSocket = listenerSocket.accept(); // Blocks until connection
            System.out.printf("Listener accepted remote port (%d) connection.\n", remoteSocket.getPort());
            ObjectInputStream remoteInput = new ObjectInputStream(remoteSocket.getInputStream());

            for (int i = 1; i <= 5; i++) {
                Network.NetworkData remoteData = (Network.NetworkData) remoteInput.readObject(); // Blocks until read
                this.remoteData[i - 1] = remoteData;
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

            for (int i = 1; i <= 5; i++) {
                Network.NetworkData dataToSend = Network.generateNetworkDataFromFile(this.messagesPath +
                        "/codeSegment" + i + ".bin");
                remoteOutput.writeObject(dataToSend); // Blocks until sending
                remoteOutput.flush();
                System.out.printf("Sent object %d to remoteSocket: %s\n", i, dataToSend);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
