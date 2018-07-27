import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Scanner;

public class Receiver {

    public static void main(String[] arg){

        String nameForFinalDecrpFile = getUserInput();

        String privKeyFileName = "YPrivate.key";
        String kxyEncFileName = "kxy.rsacipher";
        String hashFileName ="message.khmac";
        String aesEncMsgFileName = "message.aescipher";

        String kxyDecSaveAsName= "symDecKey.key";
        String kmkMessageSaveAsName = "message.kmk";

        try {
            decryptKey(kxyDecSaveAsName,kxyEncFileName, privKeyFileName);//Okay, I know the lab said 'name the decrypted key as message.kmk, but my found it much clearer & easier to do it this way.
            AESDecryption(nameForFinalDecrpFile,aesEncMsgFileName, kxyDecSaveAsName);
            makeIntoKMK(kmkMessageSaveAsName, nameForFinalDecrpFile, kxyDecSaveAsName);

            byte[] hashCalc = hash(kmkMessageSaveAsName);
            byte[] hashReceived = convertFileToByte(hashFileName);
            System.out.println("RECIEVED");
            printBytes(hashReceived);
            System.out.println("CALCULATED");
            printBytes(hashCalc);

        }
        catch (Exception e){
            System.out.print("worthless try/catch #2");
        }


    }
    public static String getUserInput() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Input the name of the message file:");
        String userIn = reader.next();
//        String userIn = "DecryptedFile";
        System.out.println("You entered: " + userIn);
        return userIn;
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

    public static byte[] saveBytesToFile(String fileName, byte[] fileInBytes) {
        byte[] emptyArr = null;
        return saveBytesToFile(fileName, fileInBytes, emptyArr );

    }
    public static byte[] saveBytesToFile(String fileName, byte[] file1, byte[] file2){

        ByteArrayOutputStream outputStream = null;
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            outputStream = new ByteArrayOutputStream();
            fos.write(file1);
            if (file2 != null){
                fos.write(file2);
            }
            byte c[] = outputStream.toByteArray();
            outputStream.close();
            fos.close();
            return c;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        byte[] fileBtyes = new byte[0];
        System.out.println("RETURNING EMPTY ARRAY saveBytesToFile:(");
        return fileBtyes;

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

    public static void printBytes(byte[] byteArr, int specLength) {
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

    public static void AESDecryption(String saveAsFile, String nameCipheredFile, String keyFile)throws Exception {
        String IV = "AAAAAAAAAAAAAAAA";
        IvParameterSpec IVready = new IvParameterSpec(IV.getBytes());

        byte[] keyBytes = convertFileToByte(keyFile);
        SecretKeySpec key = new SecretKeySpec(keyBytes,"AES");
        int fileLength = convertFileToByte(nameCipheredFile).length;

        byte[] messageBytes = new byte[fileLength];
        FileInputStream fis = new FileInputStream(nameCipheredFile);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(fileLength);

        for (int i = 0; i < fileLength; i=i+16){  //We assume (fileLength % 16 = 0). No error checking
            fis.read(messageBytes,i,16);
            byte[] subMessage = Arrays.copyOfRange(messageBytes,i,i+16);
            byteArrayOutputStream.write( decrypt(subMessage, key, IVready));
        }

        byte[] decipheredBytes = byteArrayOutputStream.toByteArray();
        byte[] decipheredDepadded = removePad(decipheredBytes);

        fis.close();
        byteArrayOutputStream.close();
        saveBytesToFile(saveAsFile, decipheredDepadded );
        return;
    }

    public static byte[] decrypt(byte[] cipherText, SecretKeySpec key, IvParameterSpec IVready) throws Exception{
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        cipher.init(Cipher.DECRYPT_MODE, key, IVready);
        return cipher.doFinal(cipherText);
    }

    public static byte[] removePad(byte[] decipheredMsg ) throws Exception{

        int length = decipheredMsg.length;
        byte[] last16bytes = Arrays.copyOfRange(decipheredMsg,length-16, length);
//        printBytes(last16bytes);
        int i = length-1;
        int dum = 15;
        while ( decipheredMsg[i] != 1 ){
            i = i - 1;
            dum =dum-1;
        }
        byte[] poo = Arrays.copyOfRange(last16bytes, 0, dum);
        return Arrays.copyOfRange(decipheredMsg, 0, i);
    }

    public static void decryptKey(String decSaveAsName, String encKeyFileName, String privKeyFileName) throws Exception {
        PrivateKey recPrivK = loadPrivateKey(privKeyFileName);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        byte[] cipherKey = convertFileToByte(encKeyFileName);

        cipher.init(Cipher.DECRYPT_MODE, recPrivK);
        byte[] decipheredKey = cipher.doFinal(cipherKey);
        saveBytesToFile(decSaveAsName, decipheredKey);
//        printBytes(decipheredKey);
    }

    public static void makeIntoKMK(String saveAs, String msgFileName, String keyFileName) throws  Exception{
        byte[] keyBytes = convertFileToByte(keyFileName);
        byte[] msgBytes = convertFileToByte(msgFileName);

        saveBytesToFile(saveAs, keyBytes, msgBytes);
        byte[] kmkBytes = convertFileToByte(saveAs);
        saveBytesToFile(saveAs,  kmkBytes, keyBytes);
        return;
    }

    //Taken from Oracle, (see KenGen.java for comment)
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

}
