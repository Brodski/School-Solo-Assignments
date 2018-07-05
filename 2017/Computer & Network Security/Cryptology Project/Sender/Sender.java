package Sender;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;
import java.util.Arrays;
import java.util.Scanner;


public class Sender {
    static private String encryptionKey = "0123456789abcdef";
    static private byte[] symData;
    static private byte[] bigByteFile;
    static private String symFileName;
    static private String recPubFileName; //Reciver's public key file name
    static private Key recPubKey;
    static String useThisFile;


    public static void main(String[] args) {
//        byte [] cipher = encrypt();
        symFileName = "symmetric.key";
        recPubFileName = "YPublic.key";
        //getSymKeyBytes();
        symData = convertFileToByte(symFileName);

 //       recPubKey = readRecieversPubKey();    // MOOOVED BELOW
        useThisFile = getUserInput();
            int sumSize = getFileSizes(useThisFile);
            System.out.println("File size of user input (again): " + sumSize);
        //bigByteFile = new byte[sumSize];
        bigByteFile = convertFileToByte(useThisFile);

        String anotherFile = "testfile.kmk" ;

        System.out.println("===========================================");
                byte [] wtfIsThisShit = convertFileToByte(useThisFile);
                byte[] wtfShit = Arrays.copyOfRange(wtfIsThisShit,wtfIsThisShit.length-49,wtfIsThisShit.length);
                printBytes(wtfShit);
                saveBytesToFile("bullshit.xd", wtfIsThisShit);
            System.out.println("SAVING symData:");
        saveBytesToFile(anotherFile, symData);
            System.out.println("PRINTING symData:");
            printBytes(symData);
        byte[] KMK = convertFileToByte(anotherFile);
            System.out.println("PRINTING KMK (1):");
            printBytes(KMK);
            System.out.println("bigByteFile.length: "+ bigByteFile.length);
        saveBytesToFile(anotherFile, KMK, bigByteFile);
            System.out.println("PRINTING KMK (2):");
            System.out.println("KMK.length: "+ KMK.length);
        KMK = convertFileToByte(anotherFile);
            System.out.println("------------XD-----------");
        byte[] subByte0 = Arrays.copyOfRange(KMK,KMK.length-49,KMK.length);
        byte[] subByteXD = Arrays.copyOfRange(bigByteFile,bigByteFile.length-49,bigByteFile.length);
            printBytes(subByte0);
            printBytes(subByteXD);
//            printBehindBytes(KMK, 30);
        saveBytesToFile(anotherFile, KMK, symData);

            KMK = convertFileToByte(anotherFile);
            System.out.println("KMK.length: " +KMK.length);
        System.out.println("-----------------------");
        byte[] subByte2 = Arrays.copyOfRange(KMK,KMK.length-49,KMK.length);
        printBytes(subByte2);

        //        writeKMK(useThisFile, sumSize);
        //readKMK(sumSize);
        byte[] hash;
        try {
            hash = hash(anotherFile);
            saveBytesToFile("message.khmac", hash);
            AESEncrption(); //saved at "message.aescipher"
            encryptKeyRSA();
//            decryptKey();

        }
        catch (Exception e){
            System.out.print("worthless try/catch");
        }



        System.out.println("ENDING");
    }

    public static int getFileSizes(String fileName) {
        File messageFile = new File(fileName);
        System.out.println("File size of user input: " + messageFile.length());

        File symFile = new File(symFileName);
        System.out.println("File size of symFile: " + symFile.length());
        return (int) messageFile.length() + (int) symFile.length() + (int) symFile.length(); // K|M|K
    }
/*
    public static void printBehindBytes(byte[] byteArr, int specLength) {
        System.out.println("Printing a byte array FROM BEHIND.");
        System.out.println("specLength: " + specLength);
        for (int i = 0, j = 0; i < specLength; i++, j++) {
            System.out.format("%2X ", new Byte(byteArr[ (byteArr.length-1) -i]));
            if (j >= 15) {
                System.out.println("");
                j = -1;
            }
        }
        System.out.println("");

    }
*/
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
/*
    public static void readKMK(int sumSize) {
        String saveFile = "message.kmk";

        try {
            BufferedInputStream file = new BufferedInputStream(new FileInputStream(saveFile));

            //byte[] cipherText2 = cipher.doFinal(input2);
            byte[] fileArry;// = new byte[sumSize];
            fileArry = convertFileToByte(saveFile);

            System.out.println("fileArry:");
            for (int i = 0, j = 0; i < fileArry.length; i++, j++) {
                System.out.format("%2X ", new Byte(fileArry[i]));
                if (j >= 15) {
                    System.out.println("");
                    j = -1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
*/

    public static String getUserInput() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Which file? \n01_Intro or HW1_CS3750.pdf");
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String dirc = System.getProperty("user.dir");
        File folder = new File(dirc);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                //         System.out.println(file.getName());
            }
        }
        //String userIn = reader.next();
        String userIn = "01_Intro.pdf";
        System.out.println("userIn: " + userIn);
        return userIn;
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
    /*
      public static void getSymKeyBytes() {

          //ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(nPath)));
          //byte[] data = Files.readAllBytes(new File("\\src\\Sender\\symmetric.key").toPath());
          try {
              File symFile = new File(symFileName);
              System.out.println("File size: " + symFile.length());
              BufferedInputStream file = new BufferedInputStream(new FileInputStream(symFileName));
              System.out.println("symFileName: " + symFileName);
              Path symPath = Paths.get(symFileName);
              symData = Files.readAllBytes(symPath);
              //Path path2 = Paths.get(file);
              //System.out.println("Absolute path: " + path2.toAbsolutePath());
          } catch (IOException e) {
              e.printStackTrace();
          }
          return;
      }
  */
    public static PublicKey readRecieversPubKey() {

        //InputStream in = RSAConfidentiality.class.getResourceAsStream(dirc);

        try {
            InputStream in = new FileInputStream(recPubFileName);

            ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));

            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();

            //System.out.println("Read from " + keyFileName + ": modulus = " + m.toString() + ", exponent = " + e.toString() + "\n");

            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey key = factory.generatePublic(keySpec);

            in.close();
            oin.close();

            return key;
        } catch (Exception e) {
            //         oin.close();
            throw new RuntimeException("Spurious serialisation error", e);
        } finally {
//            oin.close();
        }
    }

   //save the prameters of the public and private keys to file
    public static byte[] saveBytesToFile(String fileName, byte[] fileInBytes) {
        byte[] emptyArr = null;
        return saveBytesToFile(fileName, fileInBytes, emptyArr );

    }
    public static byte[] saveBytesToFile(String fileName, byte[] file1, byte[] file2){

        String saveFile = fileName;
//        FileInputStream in = null;
        ByteArrayOutputStream outputStream;  // = null;

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
            System.out.println("xxxxxxxxxxxxxxxxxxHASH PRINT:");
            printBytes(hash);
            System.out.println("hash.length: " + hash.length);

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

    public static void AESEncrption() throws Exception{
        //static String plaintext = "test text 123\0\0\0"; /*Note null padding*/
        //static String plaintext2 = "2nd piece 789\0\0\0"; /*Note null padding*/
        String thisKey = "symmetric.key";
        String IV = "AAAAAAAAAAAAAAAA";
        byte[] keyBytes = convertFileToByte(thisKey);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec IVready = new IvParameterSpec(IV.getBytes("UTF-8"));

        int fileLength = convertFileToByte(useThisFile).length;
        FileInputStream fis = new FileInputStream(useThisFile);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(fileLength);
        byte[] messageBytes = new byte[fileLength];
        byte[] THEFile = convertFileToByte(useThisFile);

        for (int i = 0; i < fileLength;  i=i+16 ) {
            int dummy = fileLength - i;
            if (i+16 >= fileLength ) {

                byte[] subMessage = Arrays.copyOfRange(THEFile,i, THEFile.length);
//                printBytes(subByteXD);

 //               fis.read(messageBytes,i , (fileLength-i)-1);  //Okay, listen, this fileInputStream is garbage. This line will get a diffrent result that the Arrays.copyOfRange(...) above. I spent like 3 hours trying trying to figure out what is wrong! ><
//                byte[] subMessage = Arrays.copyOfRange(messageBytes,i, fileLength);
                byte[] pads = padCiphMessage((fileLength - i) );
                System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

                printBytes(pads);
                printBytes(subMessage);

                ByteArrayOutputStream byOut = new ByteArrayOutputStream();
                byOut.write(subMessage);
                byOut.write(pads);
                byte[] msgAndPads = byOut.toByteArray();
                byte[] last16 = Arrays.copyOfRange(msgAndPads, 0, msgAndPads.length);
                printBytes(last16);

                byteArrayOutputStream.write( encrypt(msgAndPads, key, IVready)) ;
                System.out.println("HEY! i > fileLength!");
                break;
            }
            fis.read(messageBytes,i , 16);
            byte[] subMessage = Arrays.copyOfRange(messageBytes,i, i+16);
            byteArrayOutputStream.write( encrypt(subMessage, key, IVready)) ;
        }
        byte[] cipheredBytes = byteArrayOutputStream.toByteArray();
        saveBytesToFile("message.aescipher", cipheredBytes );
        fis.close();
        byteArrayOutputStream.close();

    }

    public static byte[] padCiphMessage(int diffInSize){
        String nullArr ="";
        byte[] nullAr ;
        if (16-diffInSize == 0){
            nullAr = new byte[16];
        }
        else {
            nullAr = new byte[ (16-diffInSize) ];
        }
        nullAr[0] = 1;
//        int roundTo16 =  16 - diffInSize;
        for (int i = 0; i < (nullAr.length - 1); i++) {
            //nullArr = nullArr + "\0";
            //String poopy = nullArr;
            nullAr[i+1] = 0;
        }
        //byte[] pads = nullArr.getBytes();

        return nullAr;

    }

    public static byte[] encrypt(byte[] bytesToEnc, SecretKeySpec key, IvParameterSpec IVready) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        cipher.init(Cipher.ENCRYPT_MODE, key, IVready );
        return cipher.doFinal(bytesToEnc);

    }

    public static void encryptKeyRSA() throws Exception{

        // recieers pub key = recPubKey;
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        //Generate a pair of keys
        SecureRandom random = new SecureRandom();
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
//        generator.initialize(1024, random);  //1024: key size in bits
        generator.initialize(1024);  //1024: key size in bits

//        KeyPair kp = loadKeyPair("XDD");
//        Key recPubKey = kp.getPublic();
        //Key recPubKey = readRecieversPubKey();
        PublicKey recPubK = loadPublicKey("YPublic.key");
//        cipher.init(Cipher.ENCRYPT_MODE, recPubKey, random);
        cipher.init(Cipher.ENCRYPT_MODE, recPubK);
 //       System.out.println("symData");
//        printBytes(symData);
        byte[] cipherKey = cipher.doFinal(symData);
        saveBytesToFile("kxy.rsacipher",cipherKey);
        System.out.println("cipherKey");
        printBytes(cipherKey);

    }

  public static PublicKey loadPublicKey(String filename) throws Exception {
        FileInputStream fis = new FileInputStream(filename);
        byte[] enc = new byte[fis.available()];
        fis.read(enc);
        fis.close();

        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(enc);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pubK = kf.generatePublic(pubKeySpec);
        return pubK;
  }
  public static PrivateKey loadPrivateKey(String filename) throws  Exception {
        FileInputStream fis = new FileInputStream(filename);
        byte[] enc = new byte[fis.available()];
        fis.read(enc);
        fis.close();

        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(enc);
        KeyFactory kf2 = KeyFactory.getInstance("RSA");
        PrivateKey privK = kf2.generatePrivate(privKeySpec);
        return privK;
  }

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

    public static void decryptKey() throws Exception {

        PrivateKey recPrivateKey = readRecieversPrivateKey("YPrivate.key");
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        byte[] cipherKey = convertFileToByte("kxy.rsacipher");

        cipher.init(Cipher.DECRYPT_MODE, recPrivateKey);
        byte[] decipheredKey = cipher.doFinal(cipherKey);
        saveBytesToFile("message2.kmk", decipheredKey);
        printBytes(decipheredKey);
    }
*/

}







    //public static byte[] encrypt() throws Exception {

//        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
 //       SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
//        cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
 //       return cipher.doFinal(plaintext.getBytes("UTF-8"));

    //}



/*
        Path path = Paths.get("/src/Sender");
        String nPath = path.toString();
        nPath = nPath + "/" + fileName;
        path = Paths.get(nPath);
        System.out.println("nPath: " + nPath);
        File myFile = new File(nPath);
        System.out.println("myFile.getParent(): " + myFile.getParent());
*/








/*        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream(saveFile);
            out = new FileOutputStream("outagain.txt");
            int W;

            while ((W = in.read()) != -1) {
                out.write(W);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    } */

