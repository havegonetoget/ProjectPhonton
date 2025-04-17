/*This program is responsible for sending messages over UDP socket 7500 
to brodcast game events and player hits to the server.*/
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

//UDP client to send game-related messages over the network
public class UDPClient {
    private static String BROADCAST_IP = "127.0.0.1"; // Localhost
    private static int BROADCAST_PORT = 7500;
     

    public static void setBroadcast(String ip) {
        BROADCAST_IP = ip;
        System.out.println("Changing broadcast IP to: " + BROADCAST_IP);
    }

    public static String getBroadcast() {
        return BROADCAST_IP; 
    }


    //send a message via udp to the specified brodcast port
    public static void sendMessage(String message) throws IOException {

        
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
