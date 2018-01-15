import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


class Serve implements Runnable {
    private String path = "/home/enan/Desktop/t.txt";
    private File file = new File(path);
    private Thread t;
    protected Socket socket = null;
    private InputStream is = null;
    private OutputStream os = null;

    Serve(Socket socket) throws IOException {
        this.socket = socket;
        is = socket.getInputStream();
        os = socket.getOutputStream();
        t = new Thread(this);
        t.start();
    }

    private void download() throws IOException {
        DataInputStream in = new DataInputStream(is);
        DataOutputStream out = new DataOutputStream(os);

        byte [] bytes = new byte[1024];
        int noOfBytes;

        out.writeUTF("Enter the file path : ");
        path = in.readUTF();
        while (!(new File(path)).exists()){
            out.writeBoolean(false);
            path = in.readUTF();
        }
        out.writeBoolean(true);

        file = new File(path);

        OutputStream os = socket.getOutputStream();
        FileInputStream fin = new FileInputStream(file);
        long len = file.length();
        out.writeLong(len);
        while ((noOfBytes = fin.read(bytes)) != -1) {
            out.write(bytes, 0, noOfBytes);
        }
//        out.writeBoolean(true);
        while (!in.readBoolean());

        fin.close();
    }
    private void upload() throws IOException {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        byte [] bytes = new byte[1024];
        int noOfBytes;

        out.writeUTF("Enter the file path : ");
        path = in.readUTF();
        while (!(new File(path)).exists()) {
            out.writeBoolean(false);
            path = in.readUTF();
        }
        out.writeBoolean(true);

        file = new File(path);

        long len = in.readLong();
        InputStream is = socket.getInputStream();
        FileOutputStream fout = new FileOutputStream(file);
        long tempLen = 0;
//        while (!in.readBoolean());
        while ((noOfBytes = is.read(bytes)) != -1) {
            fout.write(bytes,0, noOfBytes);
            tempLen += noOfBytes;
            if (tempLen>=len) break;
        }
//        while (!in.readBoolean());
        fout.close();
    }
    private void ls() throws IOException {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        out.writeUTF("Enter the path of the directory : ");
        path = in.readUTF();
        while (!(new File(path)).exists() || !(new File(path).isDirectory())) {
            out.writeBoolean(false);
            if(!(new File(path)).exists()) {
                out.writeUTF("Doesn't exist");
                path = in.readUTF();
            }
            else if (!(new File(path)).isDirectory()) {
                out.writeUTF("Not a directory");
                path = in.readUTF();
            }
        }

        out.writeBoolean(true);

        String[] lists = (new File(path)).list();
        ObjectOutputStream objs = new ObjectOutputStream(socket.getOutputStream());
        objs.writeObject(lists);
    }

    @Override
    public void run() {
        try {
            System.out.println("Connected to " + socket.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(is);
            DataOutputStream out = new DataOutputStream(os);

            out.writeUTF("Thankyou for connecting to " + socket.getLocalPort());
            System.out.println(in.readUTF());

            String menu = "1. Download" + "\n" +
                        "2. Upload" + "\n" +
                        "3. List" + "\n" + 
                        "4. Exit" +"\n";

            boolean breakWhile = false;
            while (!breakWhile) {
                out.writeUTF(menu);
                int choice = Integer.parseInt(in.readUTF());
                switch (choice) {
                    case 1:
                        download();
                        break;
                    case 2:
                        upload();
                        break;
                    case 3:
                        ls();
                        break;
                    case 4:
                        breakWhile = true;
                    default:

                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


public class Server {
    public static void main(String[] args) throws IOException {
//        int port = Integer.parseInt(args[0]);
        int port = 9999;

        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Waiting on port " + serverSocket.getLocalPort());
        } catch(IOException e){
            e.printStackTrace();
        }
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch(IOException e){
                e.printStackTrace();
            }
            new Serve(socket);
        }
    }
}
