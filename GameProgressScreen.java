import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameProgressScreen extends JFrame {
    private JPanel redTeamPanel, greenTeamPanel;

    public GameProgressScreen(List<String[]> redTeam, List<String[]> greenTeam) {
        setTitle("Game Progress");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new GridLayout(1, 2, 10, 10));

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

        // Add panels to frame
        add(redTeamPanel);
        add(greenTeamPanel);

        setVisible(true);
    }

    private void populateTeamPanel(JPanel teamPanel, List<String[]> teamData) {
        for (String[] player : teamData) {
            String displayText = "ID: " + player[0] + " | Name: " + player[1] + " | Equip: " + player[2];
            teamPanel.add(new JLabel(displayText));
        }
    }
}
