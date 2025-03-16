/* Connects to the sql database and allows UDPServer to update player scores */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.PrintStream.*; 

//database connecting class
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/photon"; 
    private static final String USER = "student";
    private static final String PASS = "student";
    public static Connection connection;
   

    //establishes a connection
    public static void getConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        getConnection(); 
        if(connection != null){
            try{
                if(connection.isValid(2)){
                    System.out.println("DB connection Valid");
                } else {
                    System.out.println("DB connection InValid");
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}

