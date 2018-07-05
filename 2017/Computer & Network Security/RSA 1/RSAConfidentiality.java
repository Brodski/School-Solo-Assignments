import java.io.*;

import java.security.Key;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;

import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.RSAPrivateKeySpec;

import java.math.BigInteger;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAConfidentiality {
    public static void main(String[] args) throws Exception {


                byte[] plainText = "my super awesome text message".getBytes();


                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
                keyGen.initialize(1024);
                KeyPair key = keyGen.generateKeyPair();

                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

                cipher.init(Cipher.ENCRYPT_MODE, key.getPublic());
                byte[] cipherText = cipher.doFinal(plainText);


                System.out.println("CIPHER");
                System.out.println( new String(cipherText, "UTF8") );
                cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
                System.out.println("DECRYPT!");
                byte[] newPlainText = cipher.doFinal(cipherText);

                System.out.println( new String(newPlainText, "UTF8") );


                /////////////// PUBLIC ///////////////
                byte[] pubByte = key.getPublic().getEncoded();
                FileOutputStream fos = new FileOutputStream("savedPub.key");
                fos.write(pubByte);
                fos.close();

                ////////////////////////////////////////

                /////////////// PRIVATE ///////////////
                byte[] privByte = key.getPrivate().getEncoded();
                FileOutputStream fos2 = new FileOutputStream("savedPriv.key");
                fos2.write(privByte);
                fos2.close();

                ///////////////////////////////////////////

                ////////////PUB FILE OPEN /////////////////////
                FileInputStream fis = new FileInputStream("savedPub.key");
                byte[] enc = new byte[fis.available()];
                fis.read(enc);
                fis.close();
                X509EncodedKeySpec pubKeySpc = new X509EncodedKeySpec(enc);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                PublicKey pubK = kf.generatePublic(pubKeySpc);
                ////////////////////////////////////////////////

                ////////////PRIVATE FILE OPEN   //////////////////

                FileInputStream fis2 = new FileInputStream("savedPriv.key");
                byte[] enc2 = new byte[fis2.available()];
                fis2.read(enc2);
                fis2.close();

                PKCS8EncodedKeySpec privKeySpc = new PKCS8EncodedKeySpec(enc2);
                KeyFactory kf2 = KeyFactory.getInstance("RSA");
                PrivateKey privK = kf2.generatePrivate(privKeySpc);
                //////////////////////////////////////////////////

                Cipher cipher2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");

                cipher2.init(Cipher.ENCRYPT_MODE, pubK);
                System.out.println("newPlainText is " + new String(newPlainText, "UTF8"));
                byte[] cipherText2 = cipher2.doFinal(newPlainText);

                System.out.println("CIPHER");
                System.out.println( new String(cipherText2, "UTF8") );

                System.out.println("DECRYPT WITH privK");
                cipher2.init(Cipher.DECRYPT_MODE, privK);
                byte[] newPlainText2 = cipher2.doFinal(cipherText2);
                System.out.println( new String(newPlainText2, "UTF8") );



            }
        }