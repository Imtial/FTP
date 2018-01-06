import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    static private String path = "/home/enan/Desktop/t1.txt";
    static private File file = new File(path);

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
//        Socket clientSocket = new Socket(args[0], args[1]);
        Socket clientSocket = new Socket("localhost", 9999);
        System.out.println("Connected to " + clientSocket.getRemoteSocketAddress());

        InputStream is = clientSocket.getInputStream();
        OutputStream os = clientSocket.getOutputStream();
        DataInputStream in = new DataInputStream(is);
        DataOutputStream out = new DataOutputStream(os);

        System.out.println(clientSocket.getRemoteSocketAddress() + " : " + in.readUTF());
        out.writeUTF("Thankyou");
        System.out.println(in.readUTF());
        String t_string = scan.nextLine();
        int choice = Integer.parseInt(t_string);
        out.writeUTF(t_string);

        if (choice == 1) {
            System.out.println(in.readUTF());
            t_string = scan.nextLine();
            out.writeUTF(t_string);
            FileOutputStream fout = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int noOfBytes;
            while ((noOfBytes = in.read(bytes)) != -1) {
                fout.write(bytes, 0, noOfBytes);
            }
            fout.close();
        }
        else if(choice == 2) {
            System.out.println(in.readUTF());
            t_string = scan.nextLine();
            out.writeUTF(t_string);
            FileInputStream fin = new FileInputStream(file);
            byte [] bytes = new byte[1024];
            int noOfBytes;
            while((noOfBytes = fin.read(bytes)) != -1) {
                out.write(bytes, 0, noOfBytes);
            }
            fin.close();
        }
        else if (choice == 3) {
            ObjectInputStream obis = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println(in.readUTF());
            t_string = scan.nextLine();
            out.writeUTF(t_string);
            String [] lists = (String []) obis.readObject();
            for (String list : lists) {
                System.out.println(list);
            }
            obis.close();
        }

        in.close();
        out.close();
        is.close();
        os.close();
        clientSocket.close();
    }
}