// File: MainGUI.java
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class MainGUI {
    private JFrame frame;
    private JTextField nameField, idField, emaField, phonField;
    private JComboBox<CourseType> courseTypeComboBox;
    private List<Student> studentList = new ArrayList<>();
    private boolean isEditMode = false;
    private int editingIndex = -1;
    private boolean isDarkMode = true;

    public MainGUI() {
        initialize();
        loadList();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(300, 100, 850, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        Font setFont = new Font("Arial", Font.BOLD, 14);

        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(470, 35, 300, 250);
        frame.getContentPane().add(imageLabel);
        ImageIcon icon = new ImageIcon("image.jpg");
        Image image = icon.getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(image));

        JLabel lblName = new JLabel("Student Name:");
        lblName.setBounds(29, 40, 120, 14);
        lblName.setFont(setFont);
        frame.getContentPane().add(lblName);

        nameField = new JTextField();
        nameField.setBounds(150, 37, 250, 28);
        frame.getContentPane().add(nameField);

        JLabel lblId = new JLabel("Student ID:");
        lblId.setBounds(29, 80, 120, 14);
        lblId.setFont(setFont);
        frame.getContentPane().add(lblId);

        idField = new JTextField();
        idField.setBounds(150, 77, 250, 28);
        frame.getContentPane().add(idField);

        JLabel lblEmail = new JLabel("E-mail:");
        lblEmail.setBounds(29, 120, 120, 14);
        lblEmail.setFont(setFont);
        frame.getContentPane().add(lblEmail);

        emaField = new JTextField();
        emaField.setBounds(150, 117, 250, 28);
        frame.getContentPane().add(emaField);

        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setBounds(29, 160, 120, 14);
        lblPhone.setFont(setFont);
        frame.getContentPane().add(lblPhone);

        phonField = new JTextField();
        phonField.setBounds(150, 157, 250, 28);
        frame.getContentPane().add(phonField);

        JLabel lblCourse = new JLabel("Course Type:");
        lblCourse.setBounds(29, 200, 120, 14);
        lblCourse.setFont(setFont);
        frame.getContentPane().add(lblCourse);

        courseTypeComboBox = new JComboBox<>(CourseType.values());
        courseTypeComboBox.setBounds(150, 197, 250, 28);
        frame.getContentPane().add(courseTypeComboBox);

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(50, 320, 100, 30);
        btnSave.setBackground(Color.GREEN);
        frame.getContentPane().add(btnSave);
        btnSave.addActionListener(e -> saveStudent());

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(170, 320, 100, 30);
        btnSearch.setBackground(Color.YELLOW);
        frame.getContentPane().add(btnSearch);
        btnSearch.addActionListener(e -> searchStudent());

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(290, 320, 100, 30);
        btnUpdate.setBackground(Color.ORANGE);
        frame.getContentPane().add(btnUpdate);
        btnUpdate.addActionListener(e -> updateStudent());

        JButton btnRetrieve = new JButton("Retrieve");
        btnRetrieve.setBounds(410, 320, 100, 30);
        btnRetrieve.setBackground(Color.BLUE);
        btnRetrieve.setForeground(Color.WHITE);
        frame.getContentPane().add(btnRetrieve);
        btnRetrieve.addActionListener(e -> retrieveStudents());

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(530, 320, 100, 30);
        btnDelete.setBackground(Color.RED);
        btnDelete.setForeground(Color.WHITE);
        frame.getContentPane().add(btnDelete);
        btnDelete.addActionListener(e -> deleteStudent());

        // 🌗 Theme Toggle Button
        JButton btnToggleTheme = new JButton("lIGHT");
        btnToggleTheme.setBounds(50, 375, 65, 22);
        btnToggleTheme.setBackground(Color.WHITE);
        btnToggleTheme.setForeground(Color.BLACK);
        frame.getContentPane().add(btnToggleTheme);
        btnToggleTheme.addActionListener(e -> {
            isDarkMode = !isDarkMode;
            applyTheme();

            if (isDarkMode) {
                btnToggleTheme.setText("Light");
            } else {
                btnToggleTheme.setText("Dark");
            }
        });

        applyTheme(); // Initial theme
    }

    private void applyTheme() {
        Color bgColor = isDarkMode ? Color.BLACK : Color.WHITE;
        Color fgColor = isDarkMode ? Color.WHITE : Color.BLACK;

        frame.getContentPane().setBackground(bgColor);

        for (java.awt.Component comp : frame.getContentPane().getComponents()) {
            if (comp instanceof JLabel || comp instanceof JTextField || comp instanceof JComboBox) {
                comp.setForeground(fgColor);
                comp.setBackground(bgColor);
            }
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                // Keep original background (green, red, etc.)
                btn.setForeground(Color.BLACK);
            }
        }
    }

    private void saveStudent() {
        Student student = new Student(
                nameField.getText(),
                idField.getText(),
                (CourseType) courseTypeComboBox.getSelectedItem(),
                emaField.getText(),
                phonField.getText()
        );
        studentList.add(student);
        saveList();
        JOptionPane.showMessageDialog(frame, "Student saved successfully.");
        clearForm();
    }

    private void updateStudent() {
        if (!isEditMode || editingIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Please search a student by ID first.");
            return;
        }

        Student updatedStudent = new Student(
                nameField.getText(),
                idField.getText(),
                (CourseType) courseTypeComboBox.getSelectedItem(),
                emaField.getText(),
                phonField.getText()
        );

        studentList.set(editingIndex, updatedStudent);
        saveList();
        JOptionPane.showMessageDialog(frame, "Student info updated successfully.");
        isEditMode = false;
        editingIndex = -1;
        clearForm();
    }

    private void searchStudent() {
        String id = idField.getText();
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).getStudentId().equals(id)) {
                Student s = studentList.get(i);
                nameField.setText(s.getStudentName());
                emaField.setText(s.getStudentEmail());
                phonField.setText(s.getphoneNumber());
                courseTypeComboBox.setSelectedItem(s.getCourseType());
                isEditMode = true;
                editingIndex = i;
                JOptionPane.showMessageDialog(frame, "Student found. Now click Update after editing.");
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Student ID not found.");
    }

    private void retrieveStudents() {
        if (studentList.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No student data available.");
            return;
        }

        String[] columnNames = {"Name", "ID", "Email", "Phone", "Course Type"};
        String[][] data = new String[studentList.size()][5];
        for (int i = 0; i < studentList.size(); i++) {
            Student s = studentList.get(i);
            data[i][0] = s.getStudentName();
            data[i][1] = s.getStudentId();
            data[i][2] = s.getStudentEmail();
            data[i][3] = s.getphoneNumber();
            data[i][4] = s.getCourseType().toString();
        }

        JTable table = new JTable(data, columnNames);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setEnabled(false);
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        JOptionPane.showMessageDialog(frame, scrollPane, "All Student Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteStudent() {
        String id = idField.getText();
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).getStudentId().equals(id)) {
                int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    studentList.remove(i);
                    saveList();
                    clearForm();
                    isEditMode = false;
                    editingIndex = -1;
                    JOptionPane.showMessageDialog(frame, "Student deleted successfully.");
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Student ID not found.");
    }

    private void clearForm() {
        nameField.setText("");
        idField.setText("");
        emaField.setText("");
        phonField.setText("");
        courseTypeComboBox.setSelectedIndex(0);
    }

    private void saveList() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hello.txt"))) {
            out.writeObject(studentList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadList() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("hello.txt"))) {
            studentList = (List<Student>) in.readObject();
        } catch (Exception e) {
            studentList = new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainGUI window = new MainGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
