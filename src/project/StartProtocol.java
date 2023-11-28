package project;

public class StartProtocol {
    public static void main(String[] args) {
        if (args.length != 4) {
            // Opens a server on <port>, and sends the 5 messages from <messagesPath>
            // to and listens to <remoteHost> with port = <remotePort> for messages
            System.out.println("Usage: java Peer <port> <messagesPath> <remotePort> <remoteHost>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        String messagesPath = args[1];
        int remotePort = Integer.parseInt(args[2]);
        String remoteHost = args[3];

        Peer peer = new Peer(port, messagesPath, remotePort, remoteHost);
    }
}
