import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class GameProgressScreen extends JFrame {
    // Maps to store each player's JLabel by equipment ID for dynamic updates.
    private Map<String, JLabel> redEquipmentLabels = new HashMap<>();
    private Map<String, JLabel> greenEquipmentLabels = new HashMap<>();

    // Event log text area.
    private JTextArea eventLogArea;

    // Timer label and countdown time in seconds.
    private JLabel timerLabel;
    private int timeRemaining = 360; // 6 minutes in seconds

    public GameProgressScreen(List<String[]> redTeam, List<String[]> greenTeam) {
        setTitle("Game Progress");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout(10, 10));

        // --- North Section: Scoreboard ---
        JPanel scoreboardPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        scoreboardPanel.setBorder(BorderFactory.createTitledBorder("Scoreboard"));

        // Red Team scoreboard panel
        JPanel redPanel = new JPanel();
        redPanel.setLayout(new BoxLayout(redPanel, BoxLayout.Y_AXIS));
        redPanel.setBorder(BorderFactory.createTitledBorder("Red Team"));
        for (String[] player : redTeam) {
            String playerId = player[0];
            String playerName = player[1];
            String equipmentId = player[2];
            JLabel label = new JLabel("ID:" + playerId + " | Name:" + playerName + " | Score:0");
            label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            redPanel.add(label);
            redEquipmentLabels.put(equipmentId, label);
        }

        // Green Team scoreboard panel
        JPanel greenPanel = new JPanel();
        greenPanel.setLayout(new BoxLayout(greenPanel, BoxLayout.Y_AXIS));
        greenPanel.setBorder(BorderFactory.createTitledBorder("Green Team"));
        for (String[] player : greenTeam) {
            String playerId = player[0];
            String playerName = player[1];
            String equipmentId = player[2];
            JLabel label = new JLabel("ID:" + playerId + " | Name:" + playerName + " | Score:0");
            label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            greenPanel.add(label);
            greenEquipmentLabels.put(equipmentId, label);
        }

        scoreboardPanel.add(redPanel);
        scoreboardPanel.add(greenPanel);
        add(scoreboardPanel, BorderLayout.NORTH);

        // --- Center Section: Event Log ---
        JPanel eventLogPanel = new JPanel(new BorderLayout());
        eventLogPanel.setBorder(BorderFactory.createTitledBorder("Game Events"));
        eventLogArea = new JTextArea();
        eventLogArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(eventLogArea);
        eventLogPanel.add(scrollPane, BorderLayout.CENTER);
        add(eventLogPanel, BorderLayout.CENTER);

        // --- South Section: Timer ---
        timerLabel = new JLabel(formatTime(timeRemaining), SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(timerLabel, BorderLayout.SOUTH);

        setVisible(true);

        // Start the countdown timer in a new thread.
        new Thread(this::startCountdownTimer).start();
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    private void startCountdownTimer() {
        for (int i = timeRemaining; i >= 0; i--) {
            final int secondsLeft = i;
            SwingUtilities.invokeLater(() -> timerLabel.setText(formatTime(secondsLeft)));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void updatePlayerBaseStatus(String team, String equipId, boolean inBase) {
        SwingUtilities.invokeLater(() -> {
            JLabel label = null;
            if ("red".equalsIgnoreCase(team)) {
                label = redEquipmentLabels.get(equipId);
            } else if ("green".equalsIgnoreCase(team)) {
                label = greenEquipmentLabels.get(equipId);
            }
            if (label != null) {
                String text = label.getText();
                if (inBase) {
                    if (!text.contains(" [B]")) {
                        label.setText(text + " [B]");
                    }
                } else {
                    label.setText(text.replace(" [B]", ""));
                }
            }
        });
    }

    public void updateScore(String team, String equipId, int newScore) {
        SwingUtilities.invokeLater(() -> {
            JLabel label = null;
            if ("red".equalsIgnoreCase(team)) {
                label = redEquipmentLabels.get(equipId);
            } else if ("green".equalsIgnoreCase(team)) {
                label = greenEquipmentLabels.get(equipId);
            }
            if (label != null) {
                String baseText = label.getText();
                boolean hasBase = false;
                if (baseText.contains(" [B]")) {
                    hasBase = true;
                    baseText = baseText.replace(" [B]", "");
                }
                String[] parts = baseText.split("\\|");
                if (parts.length >= 3) {
                    String newText = parts[0].trim() + " | " + parts[1].trim() + " | Score:" + newScore;
                    if (hasBase) {
                        newText += " [B]";
                    }
                    label.setText(newText);
                }
            }
        });
    }

    public void processHit(String attackerEquipId, String targetEquipId) {
        String attackerTeam = getTeamOfEquipId(attackerEquipId);
        String targetTeam = getTeamOfEquipId(targetEquipId);
    
        if (attackerTeam == null || targetTeam == null) {
            logEvent("Unknown equipment ID(s) in hit: " + attackerEquipId + " or " + targetEquipId);
            return;
        }
    
        // Get attacker label
        JLabel attackerLabel = attackerTeam.equals("red") ? redEquipmentLabels.get(attackerEquipId) : greenEquipmentLabels.get(attackerEquipId);
        if (attackerLabel == null) return;
    
        // Extract current score
        String labelText = attackerLabel.getText().replace(" [B]", "");
        String[] parts = labelText.split("\\|");
        int currentScore = 0;
        if (parts.length >= 3) {
            try {
                currentScore = Integer.parseInt(parts[2].trim().split(":")[1]);
            } catch (NumberFormatException e) {
                logEvent("Failed to parse score for: " + attackerEquipId);
            }
        }
    
        boolean friendlyFire = attackerTeam.equals(targetTeam);
        int scoreChange = friendlyFire ? -10 : 10;
        int newScore = currentScore + scoreChange;
    
        updateScore(attackerTeam, attackerEquipId, newScore);
    
        // Log what happened
        if (friendlyFire) {
            logEvent("Friendly fire! " + attackerEquipId + " hit teammate " + targetEquipId);
        } else {
            logEvent("Enemy hit! " + attackerEquipId + " hit enemy " + targetEquipId);
        }
    }
    

    public void logEvent(String message) {
        SwingUtilities.invokeLater(() -> eventLogArea.append(message + "\n"));
    } 
    
    
    public String getTeamOfEquipId(String equipId) {
        if (redEquipmentLabels.containsKey(equipId)) {
            return "red";
        } else if (greenEquipmentLabels.containsKey(equipId)) {
            return "green";
        }
        return null;
    }

    public static void main(String[] args) {
        List<String[]> redTeam = List.of(
            new String[]{"1", "RedPlayer1", "Equip1"},
            new String[]{"2", "RedPlayer2", "Equip2"}
        );
        List<String[]> greenTeam = List.of(
            new String[]{"3", "GreenPlayer1", "Equip3"},
            new String[]{"4", "GreenPlayer2", "Equip4"}
        );

        GameProgressScreen gps = new GameProgressScreen(redTeam, greenTeam);

        try {
            Thread.sleep(2000);
            gps.logEvent("RedPlayer1 scores!");
            gps.updateScore("red", "Equip1", 10);

            Thread.sleep(2000);
            gps.logEvent("GreenPlayer2 enters base.");
            gps.updatePlayerBaseStatus("green", "Equip4", true);

            Thread.sleep(2000);
            gps.logEvent("GreenPlayer2 leaves base.");
            gps.updatePlayerBaseStatus("green", "Equip4", false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

   
}
