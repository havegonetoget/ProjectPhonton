import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PlayerEntryScreen extends JFrame {
    private JTextField playerIdField, equipmentIdField, codeNameField;
    private JButton submitButton, startButton;
    private static final int SLOTS_PER_TEAM = 15;

    // Arrays to hold the text fields for each team's entries
    private JTextField[][] redTeamFields;
    private JTextField[][] greenTeamFields;

    //Adds reference to main class to call the py script when start game is clicked
    private Main mainApp; 

public PlayerEntryScreen(Main mainApp) {

    this.mainApp = mainApp; 

    setTitle("Player Entry");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);

    // Main panel with two columns: one for Red Team and one for Green Team
    JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));

    // --- Red Team Panel ---
    JPanel redTeamPanel = new JPanel();
    redTeamPanel.setBackground(Color.RED);
    redTeamPanel.setBorder(BorderFactory.createTitledBorder("Red Team"));
    // Using a grid layout with one extra row for the header
    redTeamPanel.setLayout(new GridLayout(SLOTS_PER_TEAM + 1, 3, 5, 5));

    // Header row for the red team
    redTeamPanel.add(new JLabel("Player ID", SwingConstants.CENTER));
    redTeamPanel.add(new JLabel("Code Name", SwingConstants.CENTER));
    redTeamPanel.add(new JLabel("Equipment ID", SwingConstants.CENTER));

    // Initialize the red team text fields
    redTeamFields = new JTextField[SLOTS_PER_TEAM][3];
    for (int i = 0; i < SLOTS_PER_TEAM; i++) {
        JTextField playerIdField = new JTextField();
        JTextField codeNameField = new JTextField();
        JTextField equipmentIdField = new JTextField();
        redTeamFields[i][0] = playerIdField;
        redTeamFields[i][1] = codeNameField;
        redTeamFields[i][2] = equipmentIdField;

        redTeamPanel.add(playerIdField);
        redTeamPanel.add(codeNameField);
        redTeamPanel.add(equipmentIdField);
    }

    // --- Green Team Panel ---
    JPanel greenTeamPanel = new JPanel();
    greenTeamPanel.setBackground(Color.GREEN);
    greenTeamPanel.setBorder(BorderFactory.createTitledBorder("Green Team"));
    greenTeamPanel.setLayout(new GridLayout(SLOTS_PER_TEAM + 1, 3, 5, 5));

    // Header row for the green team
    greenTeamPanel.add(new JLabel("Player ID", SwingConstants.CENTER));
    greenTeamPanel.add(new JLabel("Code Name", SwingConstants.CENTER));
    greenTeamPanel.add(new JLabel("Equipment ID", SwingConstants.CENTER));

    // Initialize the green team text fields
    greenTeamFields = new JTextField[SLOTS_PER_TEAM][3];
    for (int i = 0; i < SLOTS_PER_TEAM; i++) {
        JTextField playerIdField = new JTextField();
        JTextField codeNameField = new JTextField();
        JTextField equipmentIdField = new JTextField();
        greenTeamFields[i][0] = playerIdField;
        greenTeamFields[i][1] = codeNameField;
        greenTeamFields[i][2] = equipmentIdField;

        greenTeamPanel.add(playerIdField);
        greenTeamPanel.add(codeNameField);
        greenTeamPanel.add(equipmentIdField);
    }

    // Add both team panels to the main panel
    mainPanel.add(redTeamPanel);
    mainPanel.add(greenTeamPanel);

    // --- Control Panel for Buttons ---
    JPanel controlPanel = new JPanel();
    JButton submitButton = new JButton("Submit");
    JButton startButton = new JButton("Start Game");
    controlPanel.add(submitButton);
    controlPanel.add(startButton);

    // Set up the frame layout
    getContentPane().setLayout(new BorderLayout(10, 10));
    getContentPane().add(mainPanel, BorderLayout.CENTER);
    getContentPane().add(controlPanel, BorderLayout.SOUTH);

    startButton.addActionListener(new StartButtonListener());
    submitButton.addActionListener(new SubmitButtonListener());

    setVisible(true);
}


private class SubmitButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Collect data from the red and green team fields
        StringBuilder redTeamData = new StringBuilder();
        StringBuilder greenTeamData = new StringBuilder();

        // Gather red team data
        for (int i = 0; i < SLOTS_PER_TEAM; i++) {
            String playerId = redTeamFields[i][0].getText();
            String codeName = redTeamFields[i][1].getText();
            String equipmentId = redTeamFields[i][2].getText();

            if (!playerId.isEmpty() && !codeName.isEmpty() && !equipmentId.isEmpty()) {
                redTeamData.append("Player ID: ").append(playerId)
                        .append(", Code Name: ").append(codeName)
                        .append(", Equipment ID: ").append(equipmentId).append("\n");
            }
        }

        // Gather green team data
        for (int i = 0; i < SLOTS_PER_TEAM; i++) {
            String playerId = greenTeamFields[i][0].getText();
            String codeName = greenTeamFields[i][1].getText();
            String equipmentId = greenTeamFields[i][2].getText();

            if (!playerId.isEmpty() && !codeName.isEmpty() && !equipmentId.isEmpty()) {
                greenTeamData.append("Player ID: ").append(playerId)
                        .append(", Code Name: ").append(codeName)
                        .append(", Equipment ID: ").append(equipmentId).append("\n");
            }
        }

        // Display collected data (for testing)
        JOptionPane.showMessageDialog(null, "Red Team:\n" + redTeamData.toString() + "\nGreen Team:\n" + greenTeamData.toString());
    }
}

// Start game button listener (starts the game and sends a message)
private class StartButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
       
       

        // Optionally, send a UDP message to start the game
        try {
            UDPClient.sendMessage("202");
              // "202" could be the message to start the game
              //mainApp.startGameSim();
            // Optionally, close the PlayerEntryScreen after starting the game
            dispose();  // This will close the player entry window
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

/*public static void main(String[] args) {   //removed because we are refering to main to call player entry screen 
    SwingUtilities.invokeLater(() -> new PlayerEntryScreen());
}*/
}
