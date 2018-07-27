import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;
import java.util.Arrays;
import java.util.Scanner;


public class Sender {

    public static void main(String[] args) {

        String kmkSaveAsName = "message.kmk";
        String hashSaveAsName = "message.khmac";
        String aesSaveAsName = "message.aescipher";
        String keyEncSaveAsName = "kxy.rsacipher";

        String yPubKeyFileName = "YPublic.key";
        String symFileName = "symmetric.key";
        String userChoiceToEnc = getUserInput();

        byte[] bigByteFile = convertFileToByte(userChoiceToEnc);
        byte[] symData = convertFileToByte(symFileName);
        makeKMKfile(kmkSaveAsName, symData, bigByteFile);

        try {
            byte[] hash = hash(kmkSaveAsName);
                System.out.println("Hash:");
                printBytes(hash);
                saveBytesToFile(hashSaveAsName, hash);
            AESEncrption(aesSaveAsName, userChoiceToEnc);
            encryptKeyRSA(keyEncSaveAsName, symData, yPubKeyFileName);
        }
        catch (Exception e){
            System.out.print("worthless try/catch");
        }
        return;
    }

    public static void printBytes(byte[] byteArr, int specLength) {
        System.out.println("Array Length: " + specLength);
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

    public static String getUserInput() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Input the name of the message file: 01_Intro.dpf or HW1_CS3750.pdf"); //A little extra I added so I could run this program easier

        String userIn = reader.next();
//        String userIn = "01_Intro.pdf";
        System.out.println("User entered: " + userIn);
        return userIn;
    }

    public static byte[] convertFileToByte(String myFile) {
        try {
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

    public static byte[] saveBytesToFile(String fileName, byte[] fileInBytes) {
        byte[] emptyArr = null;
        return saveBytesToFile(fileName, fileInBytes, emptyArr );

    }
    public static byte[] saveBytesToFile(String fileName, byte[] file1, byte[] file2){

        String saveFile = fileName;
        ByteArrayOutputStream outputStream;  // = null;

        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            outputStream = new ByteArrayOutputStream();
            fos.write(file1);
            //printBytes(file1);
            if (file2 != null){
                //System.out.println("FILE IS NOT NULL");
                fos.write(file2);
            }
            byte c[] = outputStream.toByteArray();
            //printBytes(c);
            //String saveFile = "message.kmk";
            outputStream.close();
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

    public static void makeKMKfile(String saveAsFileName, byte[] keyBytes, byte[] messageBytes){


        saveBytesToFile(saveAsFileName, keyBytes);

        byte[] KMK = convertFileToByte(saveAsFileName);

        saveBytesToFile(saveAsFileName, KMK, messageBytes);

        KMK = convertFileToByte(saveAsFileName);

        saveBytesToFile(saveAsFileName, KMK, keyBytes);

    }

    public static byte[] hash(String fileName) throws IOException, NoSuchAlgorithmException {
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

    public static void AESEncrption(String saveAsFileName, String userChoiceToEnc) throws Exception{
        String thisKey = "symmetric.key";
        String IV = "AAAAAAAAAAAAAAAA";
        byte[] keyBytes = convertFileToByte(thisKey);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec IVready = new IvParameterSpec(IV.getBytes("UTF-8"));

        int fileLength = convertFileToByte(userChoiceToEnc).length;
        FileInputStream fis = new FileInputStream(userChoiceToEnc);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(fileLength);
        byte[] messageBytes = new byte[fileLength];
        byte[] THEFile = convertFileToByte(userChoiceToEnc);

        for (int i = 0; i < fileLength;  i=i+16 ) {
            if (i+16 >= fileLength ) {

                byte[] subMessage = Arrays.copyOfRange(THEFile,i, THEFile.length);
 //              fis.read(messageBytes,i , (fileLength-i)-1);  //Okay, listen, this fileInputStream is garbage. This line will get a result diffrent from Arrays.copyOfRange(...) above. I spent like 3 hours trying trying to figure out what is wrong!
                byte[] pads = padCiphMessage((fileLength - i) );

                ByteArrayOutputStream byOut = new ByteArrayOutputStream();
                byOut.write(subMessage);
                byOut.write(pads);
                byte[] msgAndPads = byOut.toByteArray();
//                byte[] last16 = Arrays.copyOfRange(msgAndPads, 0, msgAndPads.length);
                byteArrayOutputStream.write( encrypt(msgAndPads, key, IVready)) ;
                break;
            }
            fis.read(messageBytes,i , 16);
            byte[] subMessage = Arrays.copyOfRange(messageBytes,i, i+16);
            byteArrayOutputStream.write( encrypt(subMessage, key, IVready)) ;
        }
        byte[] cipheredBytes = byteArrayOutputStream.toByteArray();
        saveBytesToFile(saveAsFileName, cipheredBytes );
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
        for (int i = 0; i < (nullAr.length - 1); i++) {
            nullAr[i+1] = 0;
        }
        return nullAr;

    }

    public static byte[] encrypt(byte[] bytesToEnc, SecretKeySpec key, IvParameterSpec IVready) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        cipher.init(Cipher.ENCRYPT_MODE, key, IVready );
        return cipher.doFinal(bytesToEnc);

    }

    public static void encryptKeyRSA(String saveAsFileName, byte[] keyBtyes, String yPubKeyFileName) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = new SecureRandom();
        generator.initialize(1024, random);

        PublicKey recPubK = loadPublicKey(yPubKeyFileName);
        cipher.init(Cipher.ENCRYPT_MODE, recPubK);
        byte[] cipherKey = cipher.doFinal(keyBtyes);
        saveBytesToFile(saveAsFileName, cipherKey);
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

}
