import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MainGUI {
    private JFrame frame;
    private JTextField nameField;
    private JTextField idField;
    private JTextField emaField;
    private JTextField phonField;
    private JComboBox<CourseType> courseTypeComboBox;

    public MainGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(300, 100, 400, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setPreferredSize(new Dimension(800, 400));
        frame.pack(); 
        frame.getContentPane().setBackground(Color.BLACK);

        Font setFont = new Font("Arial", Font.BOLD,14);

        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(400, 35, 300, 250);  
        frame.getContentPane().add(imageLabel);
        ImageIcon icon = new ImageIcon("image.jpg");  
        Image image = icon.getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(image));

        JLabel lblNewLabel = new JLabel("Student Name:");
        lblNewLabel.setBounds(29, 40, 120, 14);
        lblNewLabel.setFont(setFont);
        lblNewLabel.setForeground(Color.WHITE);
        frame.getContentPane().add(lblNewLabel);

        nameField = new JTextField();
        nameField.setBounds(150, 37, 200, 28);
        frame.getContentPane().add(nameField);
        nameField.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Student ID:");
        lblNewLabel_1.setBounds(29, 80, 120, 14);
        lblNewLabel_1.setFont(setFont);
        lblNewLabel_1.setForeground(Color.WHITE);
        frame.getContentPane().add(lblNewLabel_1);

        idField = new JTextField();
        idField.setBounds(150, 77, 200, 28);
        frame.getContentPane().add(idField);
        idField.setColumns(10);

        JLabel lblEmail = new JLabel("E-mail:");
        lblEmail.setBounds(29, 120, 120, 14);
        lblEmail.setFont(setFont);
        lblEmail.setForeground(Color.WHITE);
        frame.getContentPane().add(lblEmail);

        emaField = new JTextField();
        emaField.setBounds(150, 117, 200, 28);
        frame.getContentPane().add(emaField);
        emaField.setColumns(10);

        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setBounds(29, 160, 120, 14);
        lblPhone.setFont(setFont);
        lblPhone.setForeground(Color.WHITE);
        frame.getContentPane().add(lblPhone);

        phonField = new JTextField();
        phonField.setBounds(150, 157, 200, 28);
        frame.getContentPane().add(phonField);
        phonField.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("Course Type:");
        lblNewLabel_2.setBounds(29, 200, 120, 14);
        lblNewLabel_2.setFont(setFont);
        lblNewLabel_2.setForeground(Color.white);
        frame.getContentPane().add(lblNewLabel_2);

        courseTypeComboBox = new JComboBox<>(CourseType.values());
        courseTypeComboBox.setBounds(150, 197, 200, 28);
        frame.getContentPane().add(courseTypeComboBox);

        

        JButton btnSave = new JButton("Save to File");
        btnSave.setBounds(29, 260, 150, 25);
        btnSave.setBackground(Color.GREEN);
        frame.getContentPane().add(btnSave);
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveToFile();
            }
        });

        JButton btnRetrieve = new JButton("Retrieve from File");
        btnRetrieve.setBounds(200, 260, 150, 25);
        btnRetrieve.setBackground(Color.BLUE);
        btnRetrieve.setForeground(Color.WHITE);
        frame.getContentPane().add(btnRetrieve);
        btnRetrieve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                retrieveFromFile();
            }
        });
    }

    private void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hello.txt"))) {
            Student student = new Student(nameField.getText(), idField.getText(), (CourseType) courseTypeComboBox.getSelectedItem(),
            emaField.getText(), phonField.getText()
            );
            out.writeObject(student);
            JOptionPane.showMessageDialog(frame, "Student data saved to file successfully.");
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error saving data to file." + ex.getMessage());
        }
    }

    private void retrieveFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("hello.txt"))) {
            Student savedStudent = (Student) in.readObject();
            System.out.println(savedStudent); 
            displayStudentDetails(savedStudent);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error retrieving data from file." + ex.getMessage());
        }
    }

    private void displayStudentDetails(Student student) {
        JOptionPane.showMessageDialog(frame, "Student Details:\n" +
                "Name: " + student.getStudentName() +
                "\nID: " + student.getStudentId() +
                "\nEmail: " + student.getStudentEmail() +
                "\nPhone: " + student.getphoneNumber() +
                "\nCourse Type: " + student.getCourseType());
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainGUI window = new MainGUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}