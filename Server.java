import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server implements Runnable {
    private Thread t;
    private ServerSocket serverSocket;
    private Socket socket;
    private int timeOut = (int) Math.pow(10,5);
    private String path = "/home/enan/Desktop/t.txt";
    private File file = new File(path);

    Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(timeOut);
        t = new Thread(this);
        t.start();
    }

    private void download(DataOutputStream out, DataInputStream in) throws IOException {
        FileInputStream fin = new FileInputStream(file);
        OutputStream os = socket.getOutputStream();
        byte [] bytes = new byte[1024];
        int noOfBytes;

        out.writeUTF("Enter the file path : ");
        path = in.readUTF();
        file = new File(path);
        while ((noOfBytes = fin.read(bytes)) != -1) {
            os.write(bytes, 0, noOfBytes);
        }
    }
    private void upload(DataOutputStream out, DataInputStream in) throws IOException {
        FileOutputStream fout = new FileOutputStream(file);
        InputStream is = socket.getInputStream();
        byte [] bytes = new byte[1024];
        int noOfBytes;

        out.writeUTF("Enter the file path : ");
        path = in.readUTF();
        file = new File(path);
        while ((noOfBytes = is.read(bytes)) != -1) {
            fout.write(bytes,0, noOfBytes);
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Waiting on port " + serverSocket.getLocalPort());
            socket = serverSocket.accept();
            System.out.println("Connected to " + socket.getRemoteSocketAddress());

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("Thank you for connecting to " + serverSocket.getInetAddress());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            System.out.println(socket.getRemoteSocketAddress() + in.readUTF());

            String menu = "1. Download" + "\n" +
                          "2. Upload" + "\n";

            out.writeUTF(menu);
            int choice = Integer.parseInt(in.readUTF());
            switch (choice) {
                case 1:
                    download(out, in);
                    break;
                case 2:
                    upload(out, in);
                    break;
                default:
                    break;
            }

            in.close();
            out.close();

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