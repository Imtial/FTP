import java.io.*;
import java.net.Socket;

public class Client {
    static private String path = "/home/enan/Desktop/t1.txt";
    static private File file = new File(path);
    public static void main(String[] args) throws IOException {
//        String serverAddress = args[0];
        String serverAddress = "localhost";
//        int serverPort = Integer.parseInt(args[1]);
        int serverPort = 9999;
        Socket clientSocket = new Socket(serverAddress, serverPort);
        System.out.println("Connected to " + clientSocket.getRemoteSocketAddress());
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        System.out.println(in.readUTF());
        out.writeUTF("Thankyou");

        FileOutputStream fo = new FileOutputStream(file);
        byte [] bytes = new byte[1024];
        int noOfBytes;
        while ((noOfBytes = in.read(bytes)) != -1) {
            fo.write(bytes, 0, noOfBytes);
        }

        /*
        FileWriter fw = new FileWriter(file);
        fw.write(in.read());
        */

        in.close();
        out.close();
        clientSocket.close();
    }
}