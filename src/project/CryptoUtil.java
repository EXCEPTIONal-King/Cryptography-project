package project;

import java.security.*;

public class CryptoUtil {

    private KeyPair keyPair;

    public CryptoUtil() {
        generateKeyPair();
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public byte[] signData(byte[] data) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(keyPair.getPrivate());
            signature.update(data);

            byte[] signatureBytes = signature.sign();
            return signatureBytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean verifySignature(byte[] data, byte[] signature, PublicKey verifyingKey) {
        try {
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(verifyingKey);
            sig.update(data);
            return sig.verify(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte[] hashMessage(String message) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(message.getBytes());
            return hashedBytes;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

//    public static void main(String[] args) {
//        // Example usage of CryptoUtil
//        CryptoUtil aliceCrypto = new CryptoUtil();
//        CryptoUtil bobCrypto = new CryptoUtil();
//        // Alice signs a message
//        String aliceMessage = "HELLO123";
//        String aliceSignature = aliceCrypto.signData(aliceMessage.getBytes());
//
//        // Bob signs his own message
//        String bobMessage = "HELLO123";
//
//        String bobSignature = bobCrypto.signData(bobMessage.getBytes());
//
//        // Alice and Bob exchange signatures and public keys
//
//        // Alice and Bob hash their messages
//        byte[] aliceHash = aliceCrypto.hashMessage(aliceMessage);
//        byte[] bobHash = bobCrypto.hashMessage(bobMessage);
//
//        // Alice and Bob exchange hashes
//
//        // Alice and Bob compare the hashes
//        boolean messagesMatch = Arrays.equals(aliceHash, bobHash);
//        boolean signaturesMatch = aliceCrypto.verifySignature(aliceMessage.getBytes(), bobSignature, bobCrypto.getPublicKey())
//                && bobCrypto.verifySignature(bobMessage.getBytes(), aliceSignature, aliceCrypto.getPublicKey());
//
//        System.out.println("Alice's Message: " + aliceMessage);
//        System.out.println("Bob's Message: " + bobMessage);
//        System.out.println("Alice's Hash: " + aliceHash);
//        System.out.println("Bob's Hash: " + bobHash);
//        if (messagesMatch && signaturesMatch) {
//            System.out.println("Messages match!");
//        } else {
//            System.out.println("Messages don't match!");
//        }
//    }
}

