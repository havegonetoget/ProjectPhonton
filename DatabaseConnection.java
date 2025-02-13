/* Connects to the sql database and allows UDPServer to update player scores */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//database connecting class
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/photon"; 
    private static final String USER = "student"; // Your DB username
    private static final String PASSWORD = "student"; // Your DB password

    //establishes a connecction
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
