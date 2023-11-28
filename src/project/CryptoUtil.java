package project;

import java.security.*;
import java.util.Base64;

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

    public String signData(byte[] data) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(keyPair.getPrivate());
            signature.update(data);

            byte[] signatureBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean verifySignature(byte[] data, String signature, PublicKey verifyingKey) {
        try {
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(verifyingKey);
            sig.update(data);

            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            return sig.verify(signatureBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String hashMessage(String message) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(message.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
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
//
//        // Alice signs a message
//        String aliceMessage = "HELLO";
//        String aliceSignature = aliceCrypto.signData(aliceMessage.getBytes());
//
//        // Bob signs his own message
//        String bobMessage = "HELLO123";
//        String bobSignature = bobCrypto.signData(bobMessage.getBytes());
//
//        // Alice and Bob exchange signatures
//
//        // Alice and Bob hash their messages
//        String aliceHash = aliceCrypto.hashMessage(aliceMessage);
//        String bobHash = bobCrypto.hashMessage(bobMessage);
//
//        // Alice and Bob exchange hashes
//
//        // Alice and Bob compare the hashes
//        boolean messagesMatch = aliceHash.equals(bobHash);
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

