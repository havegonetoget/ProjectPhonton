/*This program is responsible for sending messages over UDP socket 7500 
to brodcast game events and player hits to the server.*/
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
//UDP client to send game-related messages over the network
public class UDPClient {
    private static String BROADCAST_IP = "127.0.0.1"; // Localhost
    private static int BROADCAST_PORT = 7500;
    private static int RECEIVE_PORT = 7501; 

    public static void changeNetwork(){  
         //Check if the user would like the change IP networks
        //Create scanner for user input
        Scanner scanner = new Scanner(System.in);

        //ask if user would like to change IP
        System.out.println("Would you like to change networks (Y/N)");
        String comparator = scanner.nextLine();

        //if the user decides to change the IP
        if (comparator.compareToIgnoreCase("Y") == 0){
            System.out.println("Please input the new IP network");
            BROADCAST_IP = scanner.nextLine();
        }
    }

    //send a message via udp to the specified brodcast port
    public static void sendMessage(String message) throws IOException {

        //Check if the user would like the change IP networks
        //Create scanner for user input
       

        //create a udp socket for sending data
        DatagramSocket socket = new DatagramSocket();
        InetAddress ip = InetAddress.getByName(BROADCAST_IP);
        
        //convert the message into bytes
        byte[] data = message.getBytes();
        
        //creates a udp packet with message data, destination IP, and port
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, BROADCAST_PORT);

        //send the packet through the socket
        socket.send(packet);
        System.out.println("Sent: " + message);

        //close the socket after sending the message 
        socket.close();
    }

    //main method to send message via command line
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java UDPClient <message>");
            return;
        }

        //send the message provided as a command-line argument
        sendMessage(args[0]);
    }
}
