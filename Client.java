import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
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
        in.close();
        out.close();
        clientSocket.close();
    }
}
