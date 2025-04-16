/* Responsible for listening for UDP messages on port 7501 and 
handling game events. */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//UDP server to listen for game-related messages
public class UDPServer {
    private static final int RECEIVE_PORT = 7501;
    private static GameProgressScreen gps; 
    

    //main mehtod that starts the UDP server
    public static void main(GameProgressScreen gps) throws IOException {
       DatagramSocket socket = null; 
       try {
        //create udp socket that listens on port 7501
        socket = new DatagramSocket(RECEIVE_PORT);
        byte[] receiveBuffer = new byte[1024];

        System.out.println("UDP Server started on port " + RECEIVE_PORT);

        while (true) {
            //create a datagram packet to recive data
            DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(packet); //wait for incoming data
            System.out.println("waiting for message");

            //converts recived bytes into a string
            String receivedData = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received: " + receivedData);

            //process the recived message 
            processMessage(receivedData);
        } } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (socket != null && socket.isClosed()) {
                socket.close();
                System.out.println("UDP Server socket closed. ");
            }
        }
    }

    //received message and updates the game database accordingly
    private static void processMessage(String message) {
        try  {
            if (message.equals("202")) {
                System.out.println("Game Start!");
            } else if (message.equals("221")) {
                System.out.println("Game Over!");
            } else if (message.contains(":")) {
                String[] parts = message.split(":");
                String attackerEquipId = parts[0];
                String targetEquipId = parts[1];

        // Log raw info
         System.out.println("Equipment " + attackerEquipId + " hit Equipment " + targetEquipId);

        // Tell client the target was hit (optional)
        UDPClient.sendMessage(targetEquipId);

        // Handle scoring and logging
        gps.processHit(attackerEquipId, targetEquipId);
        
        
            } else if (message.equals("53")) {
                
            } else if (message.equals("43")) {
               
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   
   

}
