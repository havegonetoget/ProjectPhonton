import javax.swing.ImageIcon;
import javax.swing.JWindow;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class GameCountdown {

    public static void main(String[] Args) {
        showGameCountdown();
    }

    public static void showGameCountdown() {
        // Create the window for displaying
        JWindow window = new JWindow();
        JLabel countdownLabel = new JLabel();
        
        try {
            // Load the image from a file
            File backgroundfile = new File("background.jpg");
            BufferedImage image = ImageIO.read(backgroundfile);

            // Create and resize the JWindow to image size
            window.setSize(image.getWidth(), image.getHeight());
            JLabel iconLabel = new JLabel(new ImageIcon(image));

            // Create a GridBagLayout to put the countdownLabel in
            iconLabel.setLayout(new GridBagLayout());

            // Set the size, font, and color of the countdownLabel
            countdownLabel.setForeground(Color.GREEN);
            countdownLabel.setFont(new Font("Arial", Font.BOLD, 50));

            // Add the countdownLabel to the iconLabel
            iconLabel.add(countdownLabel);

            // Add the iconLabel to the window
            window.getContentPane().add(iconLabel);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Make the window centered, to the front, and visible
        window.setLocationRelativeTo(null);
        window.toFront();
        window.setVisible(true);

        try {
            // Loop through to change number for countdownLabel
            for (int i = 30; i > 0; i--) {
                countdownLabel.setText(String.valueOf(i));
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Make window no longer visible and dispose of it
        window.setVisible(false);
        window.dispose();
        
        // Create dummy data for the teams (replace with actual data as needed)
        List<String[]> redTeam = new ArrayList<>();
        List<String[]> greenTeam = new ArrayList<>();
        
        // Example entries: {playerID, name, equipment}
        redTeam.add(new String[]{"1", "RedPlayer1", "Equip1"});
        greenTeam.add(new String[]{"2", "GreenPlayer1", "Equip2"});
        
        // Launch the Game Progress Screen
        new GameProgressScreen(redTeam, greenTeam);
    }
}
