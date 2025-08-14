import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;

public class LoginGUI {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private HashMap<String, String> users;

    public LoginGUI() {
        loadUsers();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Login");
        frame.setBounds(400, 150, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("Login to Student Management");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setBounds(50, 20, 300, 30);
        frame.getContentPane().add(lblTitle);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(50, 80, 100, 25);
        frame.getContentPane().add(lblUser);

        usernameField = new JTextField();
        usernameField.setBounds(150, 80, 150, 25);
        frame.getContentPane().add(usernameField);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(50, 120, 100, 25);
        frame.getContentPane().add(lblPass);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 120, 150, 25);
        frame.getContentPane().add(passwordField);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(50, 170, 100, 30);
        frame.getContentPane().add(btnLogin);
        btnLogin.addActionListener(e -> login());

        JButton btnSignup = new JButton("Sign Up");
        btnSignup.setBounds(200, 170, 100, 30);
        frame.getContentPane().add(btnSignup);
        btnSignup.addActionListener(e -> {
            frame.dispose();
            new SignupGUI(); // Open the Signup GUI
        });

        frame.setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (users.containsKey(username) && users.get(username).equals(password)) {
            JOptionPane.showMessageDialog(frame, "Login Successful!");
            frame.dispose();
            new MainGUI(); // Open your main Student Management GUI
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"))) {
            users = (HashMap<String, String>) ois.readObject();
        } catch (Exception e) {
            users = new HashMap<>();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new LoginGUI());
    }
}
