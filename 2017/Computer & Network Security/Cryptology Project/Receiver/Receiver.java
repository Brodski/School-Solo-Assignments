package Receiver;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Scanner;

public class Receiver {

    public static void main(String[] arg){

        String keyFileName = "YPrivate.key";
        //Key recPrivateKey = readRecieversPrivateKey(keyFileName);
        String nameOfDecrpFile = getUserInput();
        try {
            byte[]bullshit = convertFileToByte("kxy.rsacipher");
            System.out.println("kxy.rsacipher");
            printBytes(bullshit);
            decryptKey();
            AESDecryption();

            makeIntoKMK("decipheredMsg.m","message2.kmk");
            String hashFileName = "newc.kmk";
            byte[] hashCalc = hash("newKMKcalc.kmk");
            byte[] hashRecieved = convertFileToByte("message.khmac");
//            byte[] secreteHash = hash("testfile.kmk");
            System.out.println("RECIEVED");
            printBytes(hashRecieved);
            System.out.println("CALCULATED");
            printBytes(hashCalc);
//            System.out.println("THIS MUST WOOOOOOOOOOOOOOOOOOOOOK");
//            printBytes(secreteHash);

        }
        catch (Exception e){
            System.out.print("worthless try/catch #2");
        }


    }
    public static String getUserInput() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Input the name of the message file:");
        /*
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String dirc = System.getProperty("user.dir");
        File folder = new File(dirc);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                //         System.out.println(file.getName());
            }
        }
        */
        //String userIn = reader.next();
        String userIn = "DecryptedFile";
        System.out.println("userIn: " + userIn);
        return userIn;
    }
    //read key parameters from a file and generate the private key
/*
    public static PrivateKey readRecieversPrivateKey(String keyFileName) throws IOException {

        InputStream in = new FileInputStream(keyFileName);
        ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));

        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            //System.out.println("Read from " + keyFileName + ": modulus = " + m.toString() + ", exponent = " + e.toString() + "\n");
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PrivateKey key = factory.generatePrivate(keySpec);

            in.close();
            oin.close();
            return key;

        } catch (Exception e) {
            throw new RuntimeException("Spurious serialisation error", e);
        } finally {
            oin.close();
        }
    }
*/

    public static byte[] hash(String fileName) throws IOException, NoSuchAlgorithmException {
        //       long LUL0 = fileSize/1024;
//        double LUL = Math.ceil(fileSize/1024);
//        int BUFFER_SIZE = (int) (Math.ceil(fileSize/1024)+1)*1024 ;
        int BUFFER_SIZE = 32 * 1024;
        try {
            BufferedInputStream file = new BufferedInputStream(new FileInputStream(fileName));
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            DigestInputStream in = new DigestInputStream(file, md);
            int i;
            int count = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            do {
                count = count +1;
                i = in.read(buffer, 0, BUFFER_SIZE);
            } while (i == BUFFER_SIZE);
            md = in.getMessageDigest();
            in.close();

            byte[] hash = md.digest();

            file.close();
            in.close();
            return hash;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //outputStream.close();
        }
        byte[] fileBtyes = new byte[0];
        System.out.println("RETURNING EMPTY ARRAY :(");
        return fileBtyes;
    }

    public static PrivateKey readPrivKeyFromFile(String keyFileName) throws IOException {

        InputStream in = Receiver.class.getResourceAsStream(keyFileName);
        ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));

        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();

            System.out.println("Read from " + keyFileName + ": modulus = " + m.toString() + "\n exponent = " + e.toString() + "\n");

            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PrivateKey key = factory.generatePrivate(keySpec);

            return key;
        } catch (Exception e) {
            throw new RuntimeException("Spurious serialisation error", e);
        } finally {
            oin.close();
        }
    }
    public static byte[] saveBytesToFile(String fileName, byte[] fileInBytes) {
        byte[] emptyArr = null;
        return saveBytesToFile(fileName, fileInBytes, emptyArr );

    }
    public static byte[] saveBytesToFile(String fileName, byte[] file1, byte[] file2){

        String saveFile = fileName;
//        FileInputStream in = null;
        ByteArrayOutputStream outputStream = null;

        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            outputStream = new ByteArrayOutputStream();
            //outputStream.write(file1);
            fos.write(file1);
            //printBytes(file1);
            if (file2 != null){
                //System.out.println("FILE IS NOT NULL");
                //outputStream.write(file2);
                fos.write(file2);
                //  printBytes(file2);
            }
            byte c[] = outputStream.toByteArray();
            //printBytes(c);
            //String saveFile = "message.kmk";
            //        ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(saveFile)));
            //       oout.writeObject(c);
            outputStream.close();
            //      oout.close();
            fos.close();
            return c;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //outputStream.close();
        }
        byte[] fileBtyes = new byte[0];
        System.out.println("RETURNING EMPTY ARRAY saveBytesToFile:(");
        return fileBtyes;

    }

    public static byte[] convertFileToByte(String myFile) {
        try {
            File file = new File(myFile);
            System.out.println("File size of " +myFile + ": " + file.length());
            Path myPath = Paths.get(myFile);
            byte[] fileBtyes = Files.readAllBytes(myPath);
            return fileBtyes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] fileBtyes = new byte[0];
        System.out.println("RETURNING EMPTY ARRAY :(");
        return fileBtyes;
    }

    public static void printBytes(byte[] byteArr, int specLength) {
        System.out.println("Printing a byte array.");
        System.out.println("specLength: " + specLength);
        for (int i = 0, j = 0; i < specLength; i++, j++) {
            System.out.format("%2X ", new Byte(byteArr[i]));
            if (j >= 15) {
                System.out.println("");
                j = -1;
            }
        }
        System.out.println("");

    }
    public static void printBytes(byte[] byteArr) {
        printBytes(byteArr, byteArr.length);
    }

    public static void AESDecryption()throws Exception {
        String fileNameKey ="message2.kmk" ;
        String fileNameCiphered = "message.aescipher";
        String IV = "AAAAAAAAAAAAAAAA";
        byte[] keyBytes = convertFileToByte(fileNameKey);
        SecretKeySpec key = new SecretKeySpec(keyBytes,"AES");
        IvParameterSpec IVready = new IvParameterSpec(IV.getBytes());

        int fileLength = convertFileToByte(fileNameCiphered).length;
        String useThisFile = "message.aescipher";
        FileInputStream fis = new FileInputStream(fileNameCiphered);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(fileLength);
        byte[] messageBytes = new byte[fileLength];

        for (int i = 0; i < fileLength; i=i+16){
            if (i+16>fileLength){
                System.out.println("HEY! i > fileLength! WTF THIS SHOULDN'T BE");
                //DO stuff
            }
            fis.read(messageBytes,i,16);
            byte[] subMessage = Arrays.copyOfRange(messageBytes,i,i+16);
            byteArrayOutputStream.write( decrypt(subMessage, key, IVready));
            if (i >= fileLength- 50) {
//                System.out.println("AT THE END");
            }
        }

        byte[] decipheredBytes = byteArrayOutputStream.toByteArray();
        byte[] decipheredDepadded = removePad(decipheredBytes);
   //     printBytes(decipheredDepadded);
        System.out.println("decipheredDdepadded.length: " +decipheredDepadded.length);
//        saveBytesToFile("decipheredMsg.m", decipheredBytes );
//        printBytes(decipheredBytes);

        byte[] subByte2 = Arrays.copyOfRange(decipheredDepadded,decipheredDepadded.length-49,decipheredDepadded.length);
        printBytes(subByte2);
        saveBytesToFile("decipheredMsg.m", decipheredDepadded );
        fis.close();
        byte[] wtflip = convertFileToByte("decipheredMsg.m");
        byte[] subByte22 = Arrays.copyOfRange(wtflip,wtflip.length-49,wtflip.length);
        printBytes(subByte22);
        byteArrayOutputStream.close();
    }

    public static byte[] decrypt(byte[] cipherText, SecretKeySpec key, IvParameterSpec IVready) throws Exception{
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        cipher.init(Cipher.DECRYPT_MODE, key, IVready);
        return cipher.doFinal(cipherText);
    }

    public static byte[] removePad(byte[] decipheredMsg ) throws Exception{

        int length = decipheredMsg.length;
        byte[] last16bytes = Arrays.copyOfRange(decipheredMsg,length-16, length);
        printBytes(last16bytes);
        //int i = last16bytes.length;
        int i = length-1;
        int dum = 15;
        while ( decipheredMsg[i] != 1 ){
            i = i - 1;
            dum =dum-1;
        }
        byte[] poo = Arrays.copyOfRange(last16bytes, 0, dum);
        return Arrays.copyOfRange(decipheredMsg, 0, i);
    }

    public static void decryptKey() throws Exception {

        //Key recPrivateKey = readRecieversPrivateKey("YPrivate.key");
        //KeyPair kp = loadKeyPair("empty");
        //PrivateKey recPrivateKey = kp.getPrivate();

        PrivateKey recPrivK = loadPrivateKey("YPrivate.key");
//        Key recPrivateKey = readPrivKeyFromFile("YPrivate.key");

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        byte[] cipherKey = convertFileToByte("kxy.rsacipher");

        cipher.init(Cipher.DECRYPT_MODE, recPrivK);
        byte[] decipheredKey = cipher.doFinal(cipherKey);
        saveBytesToFile("message2.kmk", decipheredKey);
        printBytes(decipheredKey);
    }

    public static void makeIntoKMK(String msgFileName, String keyFileName) throws  Exception{
        byte[] keyBytes = convertFileToByte(keyFileName);
        byte[] msgBytes = convertFileToByte(msgFileName);
//        byte[] subByte = Arrays.copyOfRange(msgBytes,0,48);
        byte[] subByte2 = Arrays.copyOfRange(msgBytes,msgBytes.length-49,msgBytes.length);
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//        printBytes(subByte);
        printBytes(subByte2);
//        printBytes(keyBytes);
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        String kmkName = "newKMKcalc.kmk";
        saveBytesToFile(kmkName, keyBytes, msgBytes);
        byte[] kmkBytes = convertFileToByte(kmkName);
        saveBytesToFile(kmkName,  kmkBytes, keyBytes);
    }

    public static PrivateKey loadPrivateKey(String filename) throws  Exception {
        FileInputStream fis = new FileInputStream(filename);
        byte[] enc = new byte[fis.available()];
        fis.read(enc);
        fis.close();

        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(enc);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privK = kf.generatePrivate(privKeySpec);
        return privK;
    }

/*
    public static KeyPair loadKeyPair(String file) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        File filePublicKey = new File("YPublic2.key");
        FileInputStream fis = new FileInputStream("YPrivate.key");
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();

        // Read Private Key.
        File filePrivateKey = new File("YPrivate2.key");
        fis = new FileInputStream("YPrivate2.key");
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();

        // Generate KeyPair.
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        return new KeyPair(publicKey, privateKey);


    }
*/
}
