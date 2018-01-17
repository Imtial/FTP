import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;

class GetServed {
    static private int downloadRequest;
    static private int uploadRequest;
    static {
        downloadRequest = uploadRequest = 0;
    }

    private Socket clientSocket;

    GetServed(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void download() {
        new Thread() {
            public void run() {
                try {
                    ObjectInputStream objin = new ObjectInputStream(clientSocket.getInputStream());
                    Object object = null;
                    try {
                        object = objin.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (object != null && object instanceof FileByte) {
                        System.out.println("Yeah");
                        FileByte fileByte = (FileByte) object;
                        FileOutputStream fout = new FileOutputStream("/home/enan/Desktop/contest" + "/" + fileByte.getName());
                        Byte[] tBytes = fileByte.getFileBytes().toArray(new Byte[fileByte.getFileBytes().size()]);
                        byte[] t_bytes = new byte[tBytes.length];
                        for (int i = 0; i < t_bytes.length; ++i)
                            t_bytes[i] = tBytes[i];
                        fout.write(t_bytes);
                    }
                } catch(IOException e) {
                    System.out.println("In client download method");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void upload(String path) {
        new Thread() {
            public void run() {
                Vector<Byte> fileBytes = new Vector<>();
                byte [] bytes = new byte[1024];
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
                    ObjectOutputStream objout = new ObjectOutputStream(clientSocket.getOutputStream());
                    objout.writeObject(fb);
                } catch (IOException e) {
                    System.out.println("In ObjectOutputStream objout");
                    e.printStackTrace();
                }
            }
        }.start();

    }
//    */
}

public class Client {

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);

        Socket clientSocket;
        if(args.length == 0)
            clientSocket = new Socket("localhost", 9999);
        else
            clientSocket = new Socket(args[0], Integer.parseInt(args[1]));

        System.out.println("Connected to " + clientSocket.getRemoteSocketAddress());

        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

        System.out.println(clientSocket.getRemoteSocketAddress() + " : " + in.readUTF());
        out.writeUTF("Thankyou");

        while(true) {
            String t_string = scan.nextLine();
            out.writeUTF(t_string);
            String [] strings = t_string.split(",");
            String cmd = strings[0];
            String path = strings[1];

            switch (cmd) {
                case "download":
                    new GetServed(clientSocket).download();
                    break;
                case "upload":
                    new GetServed(clientSocket).upload(path);
                    break;
                case "ls":
                    break;
                case "exit":
                    break;
            }

            /*
             * implement ls function
             */

            /*
            if (cmd.equals("ls")) {
                System.out.println(in.readUTF());
                t_string = scan.nextLine();
                out.writeUTF(t_string);
                while (!(in.readBoolean())) {
                    System.out.println(in.readUTF());
                    t_string = scan.nextLine();
                    out.writeUTF(t_string);
                }
                ObjectInputStream obis = new ObjectInputStream(clientSocket.getInputStream());
                String [] lists = (String[]) obis.readObject();
                for (String list : lists) {
                    System.out.println(list);
                }
            }
            */
        }
    }
}
