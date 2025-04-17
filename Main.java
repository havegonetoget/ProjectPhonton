import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {
        
        
        
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
            new PlayerEntryScreen();  
        });

    }


}
