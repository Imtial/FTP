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

        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

        System.out.println(clientSocket.getRemoteSocketAddress() + " : " + in.readUTF());
        out.writeUTF("Thankyou");
        while(true) {
            System.out.println(in.readUTF());
            String t_string = scan.nextLine();
            int choice = Integer.parseInt(t_string);
            out.writeUTF(t_string);

            if (choice == 1) {
                System.out.println(in.readUTF());
                t_string = scan.nextLine();
                out.writeUTF(t_string);
                while (!(in.readBoolean())) {
                    System.out.println("Path doesn't exist");
                    t_string = scan.nextLine();
                    out.writeUTF(t_string);
                }
                FileOutputStream fout = new FileOutputStream(file);
                long len = in.readLong();
                byte[] bytes = new byte[1024];
                int noOfBytes;
                long tempLen = 0;
//                while (!in.readBoolean());
                while ((noOfBytes = in.read(bytes)) != -1) {
                    fout.write(bytes, 0, noOfBytes);
                    tempLen += noOfBytes;
                    if (tempLen>=len) break;
                }
                out.writeBoolean(true);
                fout.close();
            }
            else if(choice == 2) {
                System.out.println(in.readUTF());
                t_string = scan.nextLine();
                out.writeUTF(t_string);
                while (!(in.readBoolean())) {
                    System.out.println("Path doesn't exist");
                    t_string = scan.nextLine();
                    out.writeUTF(t_string);
                }
                out.writeLong(file.length());
                FileInputStream fin = new FileInputStream(file);
                byte [] bytes = new byte[1024];
                int noOfBytes;
                while((noOfBytes = fin.read(bytes)) != -1) {
                    out.write(bytes, 0, noOfBytes);
                }
//                out.writeBoolean(true);
                fin.close();
            }
            else if (choice == 3) {
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
            else if (choice == 4) {
                break;
            }
        }

        in.close();
        out.close();
        clientSocket.close();
    }
}
