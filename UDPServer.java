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
                int attackerID = Integer.parseInt(parts[0]);
                int hitPlayerID = Integer.parseInt(parts[1]);

                //determine if it was a friendly fire hit or an opponent hit
                int points = (isSameTeam(attackerID, hitPlayerID)) ? -10 : 10;
               
                

                System.out.println("Player " + attackerID + " hit Player " + hitPlayerID);
                UDPClient.sendMessage(String.valueOf(hitPlayerID)); // Send hit response
                System.out.println("Player " + attackerID + " hit Player " + hitPlayerID);
            UDPClient.sendMessage(String.valueOf(hitPlayerID));

            
               gps.updateScore();  // replace with actual method call and need to updaste how call works. 
             
        
        
            } else if (message.equals("53")) {
                
            } else if (message.equals("43")) {
               
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //check if two players on the same team
    private static boolean isSameTeam(int attackerID, int hitPlayerID) {
        // Placeholder function: implement logic to check teams from DB
        return false;
    }

}
