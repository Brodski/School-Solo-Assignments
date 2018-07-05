import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.*;
import java.math.BigInteger;

public class Multi extends Thread {
//    private SSLSocket sslsocket = null;
    private SSLSocket sslSocket = null;

    public Multi(SSLSocket socket) {
        super("Multi");
        sslSocket = socket;
    }

    public void run() {
        {
            String fileStr;
            String receivedMsg;
            SSLSession session = sslSocket.getSession();

            try {
                System.out.println("\nServer: Echo Server Started & Ready to accept Client Connection");

                DataInputStream inputStream = new DataInputStream(sslSocket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(sslSocket.getOutputStream());

                if (session.isValid()) {
                    System.out.println("Peer host is " + session.getPeerHost());
                    System.out.println("Cipher is " + session.getCipherSuite());
                    System.out.println("Protocol is " + session.getProtocol());
                    System.out.println("ID is " + new BigInteger(session.getId()));
                    System.out.println("The creation time of this session is " + session.getCreationTime());
                    System.out.println("The last acessed time of this session is " + session.getLastAccessedTime());
                }

                while (true) {

                    outputStream.writeUTF("Server needs your info, please answer:\nUser Name: ");
                    receivedMsg = inputStream.readUTF();
                    BufferedWriter bufferedWriter = new BufferedWriter( new FileWriter(receivedMsg + ".txt"));
                    fileStr = "User Name: " + receivedMsg;
                    bufferedWriter.write(fileStr);
                    bufferedWriter.newLine();

                    //Less than optimal code below.
                    outputStream.writeUTF("Full Name:");
                    receivedMsg = inputStream.readUTF();
                    fileStr = fileStr + "\nFull Name: " + receivedMsg;
                    bufferedWriter.write("Full Name: " + receivedMsg);
                    bufferedWriter.newLine();

                    outputStream.writeUTF("Address:");
                    receivedMsg = inputStream.readUTF();
                    fileStr = fileStr + "\nAddress: " + receivedMsg;
                    bufferedWriter.write("Address: " + receivedMsg);
                    bufferedWriter.newLine();

                    outputStream.writeUTF("Phone number:");
                    receivedMsg = inputStream.readUTF();
                    fileStr = fileStr + "\nPhone number: " + receivedMsg;
                    bufferedWriter.write("Phone number: " + receivedMsg);
                    bufferedWriter.newLine();

                    outputStream.writeUTF("Email address:");
                    receivedMsg = inputStream.readUTF();
                    fileStr = fileStr + "\nEmail address: " + receivedMsg;
                    bufferedWriter.write("Email address: " + receivedMsg);
                    bufferedWriter.close();

                    System.out.println(fileStr);
                    outputStream.writeUTF(fileStr + "\nAdd more users? (yes for 'yes' or anything for 'no')");
                    receivedMsg = inputStream.readUTF();

                    if (!receivedMsg.equals("yes")) {
                        outputStream.writeUTF("Bye");
                        outputStream.close();
                        inputStream.close();
                        sslSocket.close();
                        break;
                    }
                }
            } catch (Exception ex) {
                System.err.println("Error Happened : " + ex.toString());
                System.err.println("Exiting");
                System.exit(1);
            }
        }
    }
}
