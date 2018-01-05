import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server implements Runnable {
    private Thread t;
    private ServerSocket serverSocket;
    private int timeOut = (int) Math.pow(10,4);
    private String path = "/home/enan/Desktop/t.txt";
    private File file = new File(path);

    Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(timeOut);
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

            /*
            FileWriter fout = new FileWriter(file);
            fout.write("Hello World");
            fout.close();
            */

            byte [] buffer = new byte[1024];
            int noOfBytes;
            FileInputStream fi = new FileInputStream(file);
            while ((noOfBytes = fi.read(buffer)) != -1) {
                out.write(buffer, 0, noOfBytes);
            }

            in.close();
            out.close();
            fi.close();

            socket.close();
        } catch (SocketTimeoutException e) {
            System.out.println("Server timed out!");
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