import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.io.IOException;

public class GameProgressScreen extends JFrame {
    private JPanel redTeamPanel, greenTeamPanel;
    private JLabel timerLabel;
    private Timer countdownTimer;
    private int timeRemaining = 360;

    public GameProgressScreen(List<String[]> redTeam, List<String[]> greenTeam) {
        setTitle("Game Progress");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout(10, 10));
        

		//Main content panel with teams
		JPanel teamPanel = new JPanel(new GridLayout(1, 2, 10, 10));
		teamPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Red Team Panel
        redTeamPanel = new JPanel();
        redTeamPanel.setLayout(new BoxLayout(redTeamPanel, BoxLayout.Y_AXIS));
        redTeamPanel.setBorder(BorderFactory.createTitledBorder("Red Team"));
        populateTeamPanel(redTeamPanel, redTeam);

        // Green Team Panel
        greenTeamPanel = new JPanel();
        greenTeamPanel.setLayout(new BoxLayout(greenTeamPanel, BoxLayout.Y_AXIS));
        greenTeamPanel.setBorder(BorderFactory.createTitledBorder("Green Team"));
        populateTeamPanel(greenTeamPanel, greenTeam);

		//add team panels
		teamPanel.add(redTeamPanel);
        teamPanel.add(greenTeamPanel);

		// Timer label at bottom
		timerLabel = new JLabel("6:00", SwingConstants.CENTER);
		timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
		timerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Add panels to frame
        add(teamPanel, BorderLayout.CENTER);
        add(timerLabel, BorderLayout.SOUTH);
        
        //start countdown timer;
        new Thread(() -> {
        startCountdownTimer();
		}).start();
        
        setVisible(true);
    }

    private void populateTeamPanel(JPanel teamPanel, List<String[]> teamData) {
        for (String[] player : teamData) {
            String displayText = "ID: " + player[0] + " | Name: " + player[1] + " | Equip: " + player[2];
            JLabel label = new JLabel(displayText);
            label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            teamPanel.add(label);
        }
    }
    
    private void startCountdownTimer() {
		try{
            for (int i = 360; i > 0; i--) {
				int minutes = i/60;
				int seconds = i%60;
                timerLabel.setText(String.format("%02d%n:%02d%n", minutes, seconds));
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
}


