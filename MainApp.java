import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainApp {
    Database db = new Database();
    JFrame frame = new JFrame("Online Examination System");
    JTextField usernameField = new JTextField(10);
    JPasswordField passwordField = new JPasswordField(10);

    public MainApp() {
        // Login Screen Setup
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Sign Up");

        loginBtn.addActionListener(e -> login());
        signupBtn.addActionListener(e -> signUp());

        panel.add(loginBtn);
        panel.add(signupBtn);

        frame.add(panel);
        frame.setSize(700, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (db.validateUser(username, password)) {
            showMainMenu(username);
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid Login. Try Again.");
        }
    }

    public void signUp() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        db.addUser(username, password);
        JOptionPane.showMessageDialog(frame, "Sign-up Successful! Please login.");
    }

    public void showMainMenu(String username) {
        // Close the login window and open the main menu
        frame.dispose();
        JFrame mainFrame = new JFrame("Main Menu");
        JButton takeExamBtn = new JButton("Take Exam");
        JButton showResultBtn = new JButton("Show Result");

        takeExamBtn.addActionListener(e -> startExam(username));
        showResultBtn.addActionListener(e -> showResult(username));

        JPanel panel = new JPanel();
        panel.add(takeExamBtn);
        panel.add(showResultBtn);

        mainFrame.add(panel);
        mainFrame.setSize(700, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    public void startExam(String username) {
        new Exam(username, db);
    }

    public void showResult(String username) {
        String result = db.getUserResult(username);
        if (result != null) {
            JOptionPane.showMessageDialog(null, "Previous Results: " + result);
        } else {
            JOptionPane.showMessageDialog(null, "No previous results found.");
        }
    }

    public static void main(String[] args) {
        new MainApp();
    }
}
