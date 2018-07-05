//Chris Brodski
// With help from https://youtu.be/VSi3KFlVAbE + the resources in the homework

import java.io.*;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.math.BigInteger;
import java.security.Security;
import com.sun.net.ssl.internal.ssl.Provider;

public class Client
{
    public static void main(String args[])
    {
        String serverName = ""; //To make the compiler happy
        int serverPort = -1;    //To make the compiler happy
        if (args.length > 1){

            serverName = args[0];
            serverPort = Integer.parseInt(args[1]);
        }
        else if (args.length == 1){

            System.exit(-1);
        }
        else {
            System.err.println("Not enough args. Using port '35786' and 'localhost' as remote address");
            serverPort = 35786;
            serverName = "localhost";
        }
//        Security.addProvider(new Provider());
  //      System.setProperty("javax.net.ssl.trustStore","myTrustStore.jts");
    //    System.setProperty("javax.net.ssl.trustStorePassword","123456");
//        System.setProperty("javax.net.debug","all");

        try
        {
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket)sslsocketfactory.createSocket(serverName,serverPort);
            DataOutputStream outputStream = new DataOutputStream(sslSocket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(sslSocket.getInputStream());

            //Not the best solution
            BufferedWriter bufferedWriter = new BufferedWriter( new FileWriter("clientOutputs.txt"));
            bufferedWriter.close();
            bufferedWriter = new BufferedWriter( new FileWriter("clientOutputs.txt",true));

            SSLSession session = sslSocket.getSession();
            String messageToSend;
            String recievedMsg;

            if (session.isValid()) {
                System.out.println("Peer host is " + session.getPeerHost());
                System.out.println("Cipher is " + session.getCipherSuite());
                System.out.println("Protocol is " + session.getProtocol());
                System.out.println("ID is " + new BigInteger(session.getId()));
                System.out.println("The creation time of this session is " + session.getCreationTime());
                System.out.println("The last acessed time of this session is " + session.getLastAccessedTime());
            }
            System.out.println(inputStream.readUTF());

            while (true)
            {
                messageToSend = System.console().readLine();
                bufferedWriter.write(messageToSend);
                bufferedWriter.newLine();
                outputStream.writeUTF(messageToSend);
                recievedMsg = inputStream.readUTF();
                System.err.println(recievedMsg);
                if(recievedMsg.equals("Bye"))
                {
                    break;
                }
            }
            System.err.println("Ending this client");
            bufferedWriter.close();
            sslSocket.close();
            outputStream.close();
            inputStream.close();
            return;
        }
        catch(Exception ex)
        {
            System.err.println("Error Happened : "+ ex.toString());
        }

    }
}