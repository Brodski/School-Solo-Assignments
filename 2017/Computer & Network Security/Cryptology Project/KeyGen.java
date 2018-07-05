

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;


public class KeyGen {

    public static void main(String[] args) throws Exception {

        SecureRandom random = new SecureRandom();
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
//        generator.initialize(1024, random);  //1024: key size in bits
        generator.initialize(1024);  //1024: key size in bits
        KeyPair pair = generator.generateKeyPair();
//        Key XPublic = pair.getPublic();
//        Key XPrivate = pair.getPrivate();
        PublicKey XPublic = pair.getPublic();
        PrivateKey XPrivate = pair.getPrivate();


        pair = generator.generateKeyPair();
//        Key YPublic = pair.getPublic();
//        Key YPrivate = pair.getPrivate();
        PublicKey YPublic = pair.getPublic();
        PrivateKey YPrivate = pair.getPrivate();

        KeyFactory factory = KeyFactory.getInstance("RSA");

        RSAPublicKeySpec XPubSpec = factory.getKeySpec(XPublic, RSAPublicKeySpec.class);
        RSAPrivateKeySpec XPrivSpec = factory.getKeySpec(XPrivate, RSAPrivateKeySpec.class);

        saveToFile("XPublic.key", XPubSpec.getModulus(), XPubSpec.getPublicExponent());
        saveToFile("XPrivate.key", XPrivSpec.getModulus(), XPrivSpec.getPrivateExponent());


        RSAPublicKeySpec YPubSpec = factory.getKeySpec(YPublic, RSAPublicKeySpec.class);
        RSAPrivateKeySpec YPrivSpec = factory.getKeySpec(YPrivate, RSAPrivateKeySpec.class);

        saveToFile("YPublic.key", YPubSpec.getModulus(), XPubSpec.getPublicExponent());
        saveToFile("YPrivate.key", YPrivSpec.getModulus(), XPrivSpec.getPrivateExponent());

        savePrivate("XPrivate.key", XPrivate);
        savePrivate("YPrivate.key", YPrivate);
        savePublic("XPublic.key",XPublic);
        savePublic("YPublic.key", YPublic);
        takeUserInputForKey();
        return;
    }


    //save the prameters of the public and private keys to file
    public static void saveToFile(String fileName, BigInteger mod, BigInteger exp) throws IOException {

        System.out.println("Write to " + fileName + ": modulus = " + mod.toString() + "\n exponent = " + exp.toString() + "\n");

        ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
 //       Path path = Paths.get(fileName);
//        System.out.println("Absolute path: " + path.toAbsolutePath());

        try {
            oout.writeObject(mod);
            oout.writeObject(exp);
        } catch (Exception e) {
            throw new IOException("Unexpected error", e);
        } finally {
            oout.close();
        }
    }

    public static void  takeUserInputForKey() throws Exception {
        String userInput = "";
        Scanner reader = new Scanner(System.in);
        while (userInput.length() != 16) {
            System.out.println("Enter a sixteen characters:");
            //userInput = reader.next();
            userInput = "1234567890ABCDEF";
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

    public static void SaveKeyPair(String path, PrivateKey privateKey, PublicKey publicKey) throws IOException{

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
                publicKey.getEncoded());
        FileOutputStream fos = new FileOutputStream("public2.key");
        fos.write(x509EncodedKeySpec.getEncoded());
        fos.close();

        // Store Private Key.
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                privateKey.getEncoded());
        fos = new FileOutputStream("private2.key");
        fos.write(pkcs8EncodedKeySpec.getEncoded());
        fos.close();
    }

}
