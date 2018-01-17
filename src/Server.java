import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Vector;

class Serve implements Runnable {
    private Socket socket;
    private InputStream is;
    private OutputStream os;

    Serve(Socket socket) throws IOException {
        this.socket = socket;
        is = socket.getInputStream();
        os = socket.getOutputStream();
        new Thread(this).start();
    }
    private void download(String path) {

        Vector<Byte> fileBytes = new Vector<>();
        byte[] bytes = new byte[1024];
        int noOfBytes;

        File file = new File(path);
        try (FileInputStream fin = new FileInputStream(file)) {
            while ((noOfBytes = fin.read(bytes)) != -1) {
                Byte[] wrapBytes = new Byte[noOfBytes];
                for (int i = 0; i < noOfBytes; ++i)
                    wrapBytes[i] = bytes[i];
                Collections.addAll(fileBytes, wrapBytes);
            }
        } catch (IOException e) {
            System.out.println("In ObjectOutputStream objout");
        }

        long len = fileBytes.size();
        System.out.println(len);

        FileByte fb = new FileByte(fileBytes, file.getName());
        try {
            ObjectOutputStream objout = new ObjectOutputStream(os);
            objout.writeObject(fb);
        } catch (IOException e) {
            System.out.println("In ObjectOutputStream objout");
            e.printStackTrace();
        }
    }


    private void upload(String path) throws IOException {
        path = "/home/enan/Desktop/contest";
        ObjectInputStream objin = new ObjectInputStream(is);
        Object object = null;
        try {
            object = objin.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (object != null && object instanceof FileByte) {
            System.out.println("Yeah");
            FileByte fileByte = (FileByte) object;
            FileOutputStream fout = new FileOutputStream(path + "/" + fileByte.getName());
            Byte[] tBytes = fileByte.getFileBytes().toArray(new Byte[fileByte.getFileBytes().size()]);
            byte[] t_bytes = new byte[tBytes.length];
            for (int i = 0; i < t_bytes.length; ++i)
                t_bytes[i] = tBytes[i];
            fout.write(t_bytes);
        }
    }
    /*
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
    */

    @Override
    public void run() {
        try {
            System.out.println("Connected to " + socket.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(is);
            DataOutputStream out = new DataOutputStream(os);

            out.writeUTF("Thankyou for connecting to " + socket.getLocalPort());
            System.out.println("Client : " + in.readUTF());


            boolean breakWhile = false;
            while (!breakWhile) {
                String clientMessage = in.readUTF();
                if(clientMessage.equals("exit")) breakWhile=true;
                String [] client = clientMessage.split(",");
                String cmd = client[0];
                String path = client[1];
                switch (cmd) {
                    case "download":
                        download(path);
                        break;
                    case "upload":
                        upload(path);
                        break;
                    case "ls":
//                        ls();
                        break;
                    case "exit":
                        breakWhile = true;
                        break;
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

        int port;
        if(args.length == 0) port = 9999;
        else port = Integer.parseInt(args[0]);

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
