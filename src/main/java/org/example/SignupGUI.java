import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;

public class SignupGUI {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private HashMap<String, String> users;

    public SignupGUI() {
        loadUsers();
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame("Sign Up");
        frame.setBounds(400, 150, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("Create New Account");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setBounds(100, 20, 200, 30);
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

        JButton btnCreate = new JButton("Create Account");
        btnCreate.setBounds(100, 180, 180, 30);
        frame.getContentPane().add(btnCreate);
        btnCreate.addActionListener(e -> createAccount());

        frame.setVisible(true);
    }

    private void createAccount() {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (users.containsKey(username)) {
            JOptionPane.showMessageDialog(frame, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        users.put(username, password);
        saveUsers();
        JOptionPane.showMessageDialog(frame, "Account Created Successfully!");
        frame.dispose();
        new LoginGUI();
    }

    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"))) {
            users = (HashMap<String, String>) ois.readObject();
        } catch (Exception e) {
            users = new HashMap<>();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
