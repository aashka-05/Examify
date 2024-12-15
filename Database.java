import java.sql.*;

public class Database {
    private Connection con;

    public Database() {
    try {
        // Step 1: Load MySQL JDBC Driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Step 2: Establish the connection
        con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/onlineexam?useSSL=false", "root", "root");  // Update with your own DB details
        System.out.println("Database connection successful!");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    // Add this method to return the connection to other parts of your application
    public Connection getConnection() {
        return con;
    }

    // Close the connection (Optional method for cleanup)
    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



public boolean validateUser(String username, String password) {
        try {
            String query = "SELECT * FROM users WHERE Username=? AND Password=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addUser(String username, String password) {
        try {
            String query = "INSERT INTO users (Username, Password) VALUES (?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet fetchQuestions() {
        try {
            String query = "SELECT * FROM questions";
            Statement stmt = con.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateResult(String username, String result) {
        try {
            String query = "UPDATE users SET Result=? WHERE Username=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, result);
            pst.setString(2, username);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getUserResult(String username) {
        try {
            String query = "SELECT Result FROM users WHERE Username=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("Result");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

