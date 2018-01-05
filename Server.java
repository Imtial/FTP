import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    Thread t;
    ServerSocket serverSocket;
    Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            System.out.println("Waiting on port " + serverSocket.getLocalPort());
            Socket socket = serverSocket.accept();
            System.out.println("Connected to " + socket.getRemoteSocketAddress());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("Thank you for connecting to " + serverSocket.getInetAddress());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            System.out.println(in.readUTF());
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
//        int port = Integer.parseInt(args[0]);
        int port = 9999;
        new Server(port);
    }
}
