/* Responsible for listening for UDP messages on port 7501 and 
handling game events. */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//UDP server to listen for game-related messages
public class UDPServer implements Runnable {
    private static final int RECEIVE_PORT = 7501;
    private GameProgressScreen gps;

    public UDPServer(GameProgressScreen gps) {
        this.gps = gps;
    }

    @Override
    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(RECEIVE_PORT);
            byte[] receiveBuffer = new byte[1024];

            System.out.println("UDP Server started on port " + RECEIVE_PORT);

            while (true) {
                DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(packet);
                String receivedData = new String(packet.getData(), 0, packet.getLength());

                System.out.println("Received: " + receivedData);
                processMessage(receivedData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("UDP Server socket closed.");
            }
        }
    }

    private void processMessage(String message) {
        try {
            if (message.equals("202")) {
                System.out.println("Game Start!");
            } else if (message.equals("221")) {
                System.out.println("Game Over!");
            } else if (message.contains(":")) {
                String[] parts = message.split(":");
                String attackerEquipId = parts[0];
                String targetEquipId = parts[1];

                System.out.println("Equipment " + attackerEquipId + " hit Equipment " + targetEquipId);
                UDPClient.sendMessage(targetEquipId);

                if (targetEquipId.equals("43") || targetEquipId.equals("53")) {
                    System.out.println("Player " + attackerEquipId + " hit a base!");
                    gps.markPlayerAsBaseHitter(attackerEquipId);
                } else {
                    gps.processHit(attackerEquipId, targetEquipId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

