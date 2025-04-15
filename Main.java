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
       /* Start the UDP Server in a separate thread
           new Thread(() -> {
            try {
                UDPServer.main(new String[0]);  // Start UDP Server
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start(); */

        
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

//This part is not integral at the moment of working the project 
/*    public static void startGameSim() {
        new Thread(() -> {
            runPythonTrafficGenerator();  // This method will run the Python script
        }).start(); 
    }

    // Method to run the Python game simulator (traffic generator)
    private static void runPythonTrafficGenerator() {
        try {
            // Command to run the Python script (ensure Python is installed and added to the system's PATH)
            ProcessBuilder builder = new ProcessBuilder("python3", "python_trafficgenarator_v2.py"); // Adjust the path if necessary
            builder.redirectErrorStream(true);  // Merge stdout and stderr

            // Start the process
            Process process = builder.start();

            // Read the output from the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Python Simulator Output: " + line);
            }

            // Wait for the Python script to finish 
            process.waitFor();  // This line ensures the main method waits for the Python script to finish
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } */
}
