import java.io.*;
import java.security.*;
import java.util.Scanner;


public class KeyGen {

    public static void main(String[] args) throws Exception {

        SecureRandom random = new SecureRandom();
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024, random);  //1024: key size in bits
        KeyPair pair;

        pair = generator.generateKeyPair();
        PublicKey XPublic = pair.getPublic();
        PrivateKey XPrivate = pair.getPrivate();

        pair = generator.generateKeyPair();
        PublicKey YPublic = pair.getPublic();
        PrivateKey YPrivate = pair.getPrivate();

        savePrivate("XPrivate.key", XPrivate);
        savePrivate("YPrivate.key", YPrivate);
        savePublic("XPublic.key",XPublic);
        savePublic("YPublic.key", YPublic);
        takeUserInputForKey();
        return;
    }


    public static void  takeUserInputForKey() throws Exception {
        String userInput = "";
        Scanner reader = new Scanner(System.in);
        while (userInput.length() != 16) {
            System.out.println("Enter a sixteen characters:");
            userInput = reader.next();
            //userInput = "1234567890ABCDEF";
        }

        System.out.println("You entered: " +userInput);
///        ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("symmetric.key")));

        FileOutputStream fos = new FileOutputStream("symmetric.key");
        try {
//            oout.writeObject(userInput);
            fos.write(userInput.getBytes());
        } catch (Exception e) {
            throw new IOException("Unexpected error", e);
        } finally {
            //oout.close();
            fos.close();
        }
        return;
    }

    //So, the saveToFile() method provided by the "RSAConfidentiality.java" file found on blackboard
    // does not work for what ever reason. I instead used the code provided by oracle to save and retrieve a Key.
    // A) https://docs.oracle.com/javase/tutorial/security/apisign/step4.html
    // B) https://docs.oracle.com/javase/tutorial/security/apisign/vstep2.html
    public static void savePublic(String file, PublicKey pubKey) throws Exception{
        byte[] pubByte = pubKey.getEncoded();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(pubByte);
        fos.close();
    }
    public static void savePrivate(String file, PrivateKey privKey) throws Exception {
        byte[] privByte = privKey.getEncoded();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(privByte);
        fos.close();
    }


}
