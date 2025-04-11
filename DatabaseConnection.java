/* Connects to the sql database and allows UDPServer to update player scores */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.PrintStream.*; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//database connecting class
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/photon"; 
    private static final String USER = "student";
    private static final String PASS = "student";
    public static Connection connection;
    private static String insertSQL = "INSERT INTO players (id, codename) VALUES (?, ?)";
    private static String getSQL = "SELECT codename FROM players WHERE id = ?";

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
    
    //adds a entry to the database if the id is empty
    public static void addDatabaseEntry(int id, String codename){
		try {
			//checks if id is empty
			if (getDatabaseEntry(id) != null) {
				return;
			}
			//adds entry to database
			PreparedStatement pstmt = connection.prepareStatement(insertSQL);
			pstmt.setInt(1, id);
			pstmt.setString(2, codename);
			pstmt.executeUpdate();
			System.out.println("Player added: " + codename);
		} catch (SQLException e) {
			System.out.println("Error inserting player: " + e.getMessage());
		}
	}
	
	//checks id and corrosponding codename, return null if id is empty
	public static String getDatabaseEntry(int id){
		try {
			PreparedStatement pstmt = connection.prepareStatement(getSQL);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			System.out.println("Error fetching player: " + e.getMessage());
		}
		return null;
	}
}
