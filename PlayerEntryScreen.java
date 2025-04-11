import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.NumberFormatException;


public class PlayerEntryScreen extends JFrame {
    private JTextField[][] redTeamFields;
    private JTextField[][] greenTeamFields;
    private static final int SLOTS_PER_TEAM = 15;
    private List<String[]> redTeamList = new ArrayList<>();
    private List<String[]> greenTeamList = new ArrayList<>();

    public PlayerEntryScreen(Main mainApp) {
        setTitle("Player Entry");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        setFocusable(true);
        requestFocusInWindow();

        // Use key bindings instead of key listener to handle F12 key.
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "clearEntries");
        getRootPane().getActionMap().put("clearEntries", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearPlayerEntries();
            }
        });
        
		// Use key bindings instead of key listener to handle F6 key.
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "startGame");
        getRootPane().getActionMap().put("startGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        
        // Use key bindings instead of key listener to handle F7 key.
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), "playerSave");
        getRootPane().getActionMap().put("playerSave", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerSave();
            }
        });
        
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel redTeamPanel = new JPanel(new GridLayout(SLOTS_PER_TEAM + 1, 3, 5, 5));
        redTeamPanel.setBackground(Color.RED);
        redTeamPanel.setBorder(BorderFactory.createTitledBorder("Red Team"));
        redTeamPanel.add(new JLabel("Player ID", SwingConstants.CENTER));
        redTeamPanel.add(new JLabel("Code Name", SwingConstants.CENTER));
        redTeamPanel.add(new JLabel("Equipment ID", SwingConstants.CENTER));

        redTeamFields = new JTextField[SLOTS_PER_TEAM][3];
        for (int i = 0; i < SLOTS_PER_TEAM; i++) {
            redTeamFields[i][0] = new JTextField();
            redTeamFields[i][1] = new JTextField();
            redTeamFields[i][2] = new JTextField();
            attachIdCheckListener(redTeamFields[i][0], redTeamFields[i][1]);
            redTeamPanel.add(redTeamFields[i][0]);
            redTeamPanel.add(redTeamFields[i][1]);
            redTeamPanel.add(redTeamFields[i][2]);
        }

        JPanel greenTeamPanel = new JPanel(new GridLayout(SLOTS_PER_TEAM + 1, 3, 5, 5));
        greenTeamPanel.setBackground(Color.GREEN);
        greenTeamPanel.setBorder(BorderFactory.createTitledBorder("Green Team"));
        greenTeamPanel.add(new JLabel("Player ID", SwingConstants.CENTER));
        greenTeamPanel.add(new JLabel("Code Name", SwingConstants.CENTER));
        greenTeamPanel.add(new JLabel("Equipment ID", SwingConstants.CENTER));

        greenTeamFields = new JTextField[SLOTS_PER_TEAM][3];
        for (int i = 0; i < SLOTS_PER_TEAM; i++) {
            greenTeamFields[i][0] = new JTextField();
            greenTeamFields[i][1] = new JTextField();
            greenTeamFields[i][2] = new JTextField();
            attachIdCheckListener(greenTeamFields[i][0], greenTeamFields[i][1]);
            greenTeamPanel.add(greenTeamFields[i][0]);
            greenTeamPanel.add(greenTeamFields[i][1]);
            greenTeamPanel.add(greenTeamFields[i][2]);
        }
        
        mainPanel.add(redTeamPanel);
        mainPanel.add(greenTeamPanel);

        JPanel controlPanel = new JPanel();
        JButton submitButton = new JButton("Save Entries (F7)");
        JButton startButton = new JButton("Start Game (F6)");
        JButton clearButton = new JButton("Clear Entries (F12)");
        controlPanel.add(submitButton);
        controlPanel.add(startButton);
        controlPanel.add(clearButton);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> playerSave());
        startButton.addActionListener(e -> startGame());
        clearButton.addActionListener(e -> clearPlayerEntries());

        setFocusable(true);
        requestFocusInWindow(); 
      

        setVisible(true);
    }
    
    //adds listener to automatics add codename to field if the id is inputed
    private void attachIdCheckListener(JTextField idField, JTextField codenameField) {
		idField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String idText = idField.getText().trim();
				if (!idText.isEmpty()) {
					try {
						int id = Integer.parseInt(idText);
						String existingName = DatabaseConnection.getDatabaseEntry(id);
						if (existingName != null) {
							codenameField.setText(existingName);
						}
					} catch (NumberFormatException ex) {
						ex.printStackTrace();
					}
				}
			}
		});	
	}

    private void clearPlayerEntries() {
        for (int i = 0; i < SLOTS_PER_TEAM; i++) {
            redTeamFields[i][0].setText("");
            redTeamFields[i][1].setText("");
            redTeamFields[i][2].setText("");
            greenTeamFields[i][0].setText("");
            greenTeamFields[i][1].setText("");
            greenTeamFields[i][2].setText("");
        }
    }

	//adds players to the database if they aren't already their
    private void playerSave() {
		redTeamList.clear();
		greenTeamList.clear();

        for (int i = 0; i < SLOTS_PER_TEAM; i++) {
            String playerId = redTeamFields[i][0].getText();
            String codeName = redTeamFields[i][1].getText();
            String equipmentId = redTeamFields[i][2].getText();
            if (!playerId.isEmpty() && !codeName.isEmpty() && !equipmentId.isEmpty()) {
                redTeamList.add(new String[]{playerId, codeName, equipmentId});
            }
			if (!playerId.isEmpty() && !codeName.isEmpty()) {
                DatabaseConnection.addDatabaseEntry(Integer.parseInt(playerId), codeName);
            }
        }

        for (int i = 0; i < SLOTS_PER_TEAM; i++) {
            String playerId = greenTeamFields[i][0].getText();
            String codeName = greenTeamFields[i][1].getText();
            String equipmentId = greenTeamFields[i][2].getText();
            if (!playerId.isEmpty() && !codeName.isEmpty() && !equipmentId.isEmpty()) {
                greenTeamList.add(new String[]{playerId, codeName, equipmentId});
            }
			if (!playerId.isEmpty() && !codeName.isEmpty()) {
                DatabaseConnection.addDatabaseEntry(Integer.parseInt(playerId), codeName);
            }
        }

        JOptionPane.showMessageDialog(null, "Players have been saved.");
    }

    public void startGame() {
        try {
            UDPClient.sendMessage("202");

            new Thread(() -> {
                try {
                    // Pass the collected team data from the player entry screen
                    GameCountdown.showGameCountdown(redTeamList, greenTeamList);
                } catch (Error ep) {
                    ep.printStackTrace();
                }
            }).start();

            // Removed the duplicate instantiation of GameProgressScreen.
            dispose();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
