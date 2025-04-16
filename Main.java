import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {
        
        Main mainApp = new Main(); //makes instance of main
        
        // Get Connection to the database
		DatabaseConnection.getConnection();
        if(DatabaseConnection.connection != null){
            try{
                if(DatabaseConnection.connection.isValid(2)){
                    System.out.println("DB connection Valid");
                } else {
                    System.out.println("DB connection InValid");
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
      
        
        // Show the splash screen
        SplashScreen.showSplashScreen();

        // Start the player entry screen (GUI)
        javax.swing.SwingUtilities.invokeLater(() -> {
            new PlayerEntryScreen(mainApp);  //pass instance of main to player entry screen
        });

        
     

        

       

        // Example UDPClient usage - send a test message
        try {
            UDPClient.changeNetwork(); 
            UDPClient.sendMessage("Test message to broadcast");
        } catch (IOException e) {
            e.printStackTrace();
        }
         
    }


}
