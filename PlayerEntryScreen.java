import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerEntryScreen extends JFrame {
    private JTextField playerIdField, equipmentIdField, codeNameField;
    private JButton submitButton, startButton;
    private static final int SLOTS_PER_TEAM = 15;

    // Arrays to hold the text fields for each team's entries
    private JTextField[][] redTeamFields;
    private JTextField[][] greenTeamFields;

public PlayerEntryScreen() {
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

    setVisible(true);
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new PlayerEntryScreen());
}
}