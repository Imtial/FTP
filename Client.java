import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    static private String path = "/home/enan/Desktop/t1.txt";
    static private File file = new File(path);

    public static void main(String[] args) throws IOException {
//        String serverAddress = args[0];
        String serverAddress = "localhost";
//        int serverPort = Integer.parseInt(args[1]);
        int serverPort = 9999;

        Scanner scan = new Scanner(System.in);

        Socket clientSocket = new Socket(serverAddress, serverPort);
        System.out.println("Connected to " + clientSocket.getRemoteSocketAddress());

        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

        System.out.println(in.readUTF());
        out.writeUTF("Thankyou");
        System.out.println(in.readUTF());
        String t_string = scan.nextLine();
        int choice = Integer.parseInt(t_string);
        out.writeUTF(t_string);
        System.out.println(in.readUTF());
        t_string = scan.nextLine();
        out.writeUTF(t_string);

        if (choice == 1) {
            FileOutputStream fo = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int noOfBytes;
            while ((noOfBytes = in.read(bytes)) != -1) {
                fo.write(bytes, 0, noOfBytes);
            }
        }
        else if(choice == 2) {
            FileInputStream fin = new FileInputStream(file);
            byte [] bytes = new byte[1024];
            int noOfBytes;
            while((noOfBytes = fin.read(bytes)) != -1) {
                out.write(bytes, 0, noOfBytes);
            }
        }


        in.close();
        out.close();
        clientSocket.close();
    }
}