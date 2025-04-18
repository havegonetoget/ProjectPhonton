import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

public class GameProgressScreen extends JFrame {
    // Maps to store each player's JLabel by equipment ID for dynamic updates.
    private Map<String, JLabel> redEquipmentLabels = new HashMap<>();
    private Map<String, JLabel> greenEquipmentLabels = new HashMap<>();
    private Map<String, String> equipIDToCodename = new HashMap<>();
    private JPanel redPanel;
    private JPanel greenPanel; 
    private JPanel scoreboardPanel;
    // Event log text area.
    private JTextArea eventLogArea;

    // Timer label and countdown time in seconds.
    private JLabel timerLabel;
    private int timeRemaining = 360; // 6 minutes in seconds

    private Map<String, Integer> teamScores = new HashMap<>() {{
        put("red", 0);
        put("green", 0);
    }};	
    private JLabel redTeamScoreLabel = new JLabel("Team Score: 0", SwingConstants.CENTER);
    private JLabel greenTeamScoreLabel = new JLabel("Team Score: 0", SwingConstants.CENTER);

    public GameProgressScreen(List<String[]> redTeam, List<String[]> greenTeam) {
        setTitle("Game Progress");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout(10, 10));

        // --- North Section: Scoreboard ---
        JPanel scoreboardPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        scoreboardPanel.setBorder(BorderFactory.createTitledBorder("Scoreboard"));

        // Red Team scoreboard panel
        redPanel = new JPanel();   //since there is no local green panel no need to reference the memeber variable by using this.red, same for green
        redPanel.setLayout(new BoxLayout(redPanel, BoxLayout.Y_AXIS));
        redPanel.setBorder(BorderFactory.createTitledBorder("Red Team"));
        for (String[] player : redTeam) {
            String playerId = player[0];
            String playerName = player[1];
            String equipmentId = player[2];
            equipIDToCodename.put(equipmentId, playerName);
            JLabel label = new JLabel("ID:" + playerId + " | Name:" + playerName + " | Score:0");
            label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            redPanel.add(label);
            redEquipmentLabels.put(equipmentId, label);
        }

        // Green Team scoreboard panel
        greenPanel = new JPanel();
        greenPanel.setLayout(new BoxLayout(greenPanel, BoxLayout.Y_AXIS));
        greenPanel.setBorder(BorderFactory.createTitledBorder("Green Team"));
        for (String[] player : greenTeam) {
            String playerId = player[0];
            String playerName = player[1];
            String equipmentId = player[2];
            JLabel label = new JLabel("ID:" + playerId + " | Name:" + playerName + " | Score:0");
            equipIDToCodename.put(equipmentId, playerName);
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
            if (secondsLeft == 0) {
				try {
					UDPClient.sendMessage("221");
					UDPClient.sendMessage("221");
					UDPClient.sendMessage("221");
				} catch (IOException e) {
					e.printStackTrace();
				}
		    // ← schedule window close on the EDT
                SwingUtilities.invokeLater(() -> {
                    setVisible(false);
                    dispose();
                	});
		}
        }
    }

    public void markPlayerAsBaseHitter(String equipId) {
        SwingUtilities.invokeLater(() -> {
            JLabel label = null;
            String team = getTeamOfEquipId(equipId);
            if ("red".equalsIgnoreCase(team)) {
                label = redEquipmentLabels.get(equipId);
            } else if ("green".equalsIgnoreCase(team)) {
                label = greenEquipmentLabels.get(equipId);
            }
    
            if (label != null) {
                String text = label.getText();
                if (!text.contains(" [B]")) {
                    label.setText(text + " [B]");
                    String codename = equipIDToCodename.getOrDefault(equipId, equipId);
                    logEvent("Base hit! " + codename + " hit the base!" );
					String attackerTeam = getTeamOfEquipId(equipId);
                    JLabel attackerLabel = attackerTeam.equals("red") ? redEquipmentLabels.get(equipId) : greenEquipmentLabels.get(equipId);
                    String labelText = attackerLabel.getText().replace(" [B]", "");
					String[] parts = labelText.split("\\|");
					int currentScore = 0;
					if (parts.length >= 3) {
						try {
							currentScore = Integer.parseInt(parts[2].trim().split(":")[1]);
						} catch (NumberFormatException e) {
							logEvent("Failed to parse score for: " + equipId);
						}
					}
                    
                    int scoreChange = 100;
					int newScore = currentScore + scoreChange;

					updateScore(attackerTeam, equipId, newScore);
					updateTeamScores();
                }
            }
        });
    }

    public void updateTeamScores() {
        int redTotal = redEquipmentLabels.values().stream().mapToInt(l -> extractScore(l.getText())).sum();
        int greenTotal = greenEquipmentLabels.values().stream().mapToInt(l -> extractScore(l.getText())).sum();
        teamScores.put("red", redTotal);
        teamScores.put("green", greenTotal);

        redTeamScoreLabel.setText("Team Score: " + redTotal);
        greenTeamScoreLabel.setText("Team Score: " + greenTotal);

        String leadingTeam = redTotal > greenTotal ? "red" : (greenTotal > redTotal ? "green" : "tie");
        flashTeamPanel(leadingTeam);
    }

    public void flashTeamPanel(String team) {
        if ("tie".equals(team)) return;
        JPanel panel = "red".equals(team) ? redPanel : greenPanel;
        Color original = panel.getBackground();
        panel.setBackground(Color.YELLOW);
        Timer timer = new Timer(500, e -> panel.setBackground(original));
        timer.setRepeats(false);
        timer.start();
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
                    sortTeamScores(team);
		    updateTeamScores();
                }
            }
        });
    }

    public void sortTeamScores(String team){
        JPanel panelToSort = "red".equalsIgnoreCase(team) ? redPanel : greenPanel; 
        Map<String, JLabel> equipmentLables = "red".equalsIgnoreCase(team) ? redEquipmentLabels : greenEquipmentLabels;
        java.util.List<JLabel> labels = new java.util.ArrayList<>(equipmentLables.values());

        labels.sort((a, b) -> {
            int scoreA = extractScore(a.getText());
            int scoreB = extractScore(b.getText());
            return Integer.compare(scoreB, scoreA);
        });

        panelToSort.removeAll();
        for (JLabel label : labels){
            panelToSort.add(label);
        }

        panelToSort.revalidate();
        panelToSort.repaint();
    }

    public int extractScore(String labelText) {
        try {
            String cleaned = labelText.replace(" [B]", "");
            String[] parts = cleaned.split("\\|"); 

            if (parts.length >= 3){
                return Integer.parseInt(parts[2].trim().split(":")[1]); 
            }
         } catch (Exception e) {
            e.printStackTrace();
         }

        return 0; 
    }

    public void processHit(String attackerEquipId, String targetEquipId) {
        String attackerTeam = getTeamOfEquipId(attackerEquipId);
        String targetTeam = getTeamOfEquipId(targetEquipId);
        String attackerCodename = equipIDToCodename.getOrDefault(attackerEquipId, attackerEquipId);
        String targetCodename = equipIDToCodename.getOrDefault(targetEquipId, targetEquipId); 
    
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
            logEvent("Friendly fire! " + attackerCodename + " hit teammate " + targetCodename);
        } else {
            logEvent("Enemy hit! " + attackerCodename + " hit enemy " + targetCodename);
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
}
