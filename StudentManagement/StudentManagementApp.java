import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class StudentManagementApp {
    public static class Student {
        private String name;
        private int rollNo;
        private String grade;
        public Student(String name, int rollNo, String grade) {
            this.name = name;
            this.rollNo = rollNo;
            this.grade = grade;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getRollno() {
            return rollNo;
        }
        public void setRollno(int rollNo) {
            this.rollNo = rollNo;
        }
        public String getGrade() {
            return grade;
        }
        public void setGrade(String grade) {
            this.grade = grade;
        }
        @Override
        public String toString() {
            return("Roll No: " + rollNo + ", Name: " + name +", Grade: " + grade);
        }
    }
    public class StudentManagementSystem {
        private ArrayList<Student> students;
        public StudentManagementSystem() {
            students = new ArrayList<>();
        }
        public void addStudent(Student stud) {
            students.add(stud);
        }
        public boolean removeStudent(int rollNo) {
        return students.removeIf(stud -> stud.getRollno() == rollNo);
        }
        public Student searchStudent(int rollNo) {
            for(Student stud : students) {
                if(stud.getRollno() == rollNo) {
                    return stud;
                }
            }
            return null;
        }
        public ArrayList<Student>getAllStudents() {
                return students;
            }
            public void saveToFile(String fileName) throws IOException {
                try (FileWriter writer = new FileWriter(fileName)) {
                    for(Student stud : students) {
                       writer.write(stud.getRollno() + "," + stud.getName() + "," + stud.getGrade() + "\n");
                    }
                }
            }
            public void loadFromFile(String fileName) throws IOException {
                students.clear();
                try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                    String line;
                    while((line = reader.readLine())!=null) {
                        String [] parts = line.split(",");
                        if(parts.length == 3) {
                                students.add(new Student(parts[1], Integer.parseInt(parts[0]), parts[2]));
                            }
                        }
                    }
                }
            }
        private JFrame frame;
        private StudentManagementSystem sms;
        public StudentManagementApp() {
            sms = new StudentManagementSystem();
            frame = new JFrame("Student Management System ");
            frame.setSize(600,400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            JPanel panelTop = new JPanel();
            panelTop.setLayout(new FlowLayout());
            JTextField txtname = new JTextField(18);
            JTextField txtrollno = new JTextField(6);
            JTextField txtgrade = new JTextField(5);
            JButton btnAdd = new JButton("Add");
            JButton btnSearch = new JButton("Search");
            JButton btnRemove = new JButton("Remove");
            JButton btnDisplay = new JButton("Display All");
            JButton btnSave = new JButton("Save");
            JButton btnLoad = new JButton("Load");
            panelTop.add(new JLabel("Name: "));
            panelTop.add(txtname);
            panelTop.add(new JLabel("Roll No: "));
            panelTop.add(txtrollno);
            panelTop.add(new JLabel("Grade: "));
            panelTop.add(txtgrade);
            panelTop.add(btnAdd);
            panelTop.add(btnSearch);
            panelTop.add(btnRemove);
            panelTop.add(btnDisplay);
            panelTop.add(btnSave);
            panelTop.add(btnLoad);
            frame.add(panelTop, BorderLayout.NORTH);
            JTextArea outputArea = new JTextArea();
            outputArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(outputArea);
            frame.add(scrollPane, BorderLayout.CENTER);
            btnAdd.addActionListener(e -> {
                String name = txtname.getText().trim();
                String rollStr = txtrollno.getText().trim();
                String grades = txtgrade.getText().trim();
                if(name.isEmpty() || rollStr.isEmpty() || grades.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    int roll = Integer.parseInt(rollStr);
                    sms.addStudent(new Student(name, roll, grades));
                    JOptionPane.showMessageDialog(frame, "Student Added Successfully!");
                } 
                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Roll number must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            btnSearch.addActionListener(e -> {
                String rollStr = txtrollno.getText().trim();
                try {
                    int roll = Integer.parseInt(rollStr);
                    Student stud = sms.searchStudent(roll);
                    if(stud!= null) {
                        outputArea.setText(stud.toString());
                    }
                    else {
                        outputArea.setText("Student not found!");
                    }
                }
                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Roll number must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            btnRemove.addActionListener(e -> {
                String rollStr = txtrollno.getText().trim();
                try {
                    int roll = Integer.parseInt(rollStr);
                    if(sms.removeStudent(roll)) {
                        JOptionPane.showMessageDialog(frame, "Student Removed!");
                    }
                    else {
                        JOptionPane.showMessageDialog(frame, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Roll Number must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            btnDisplay.addActionListener(e -> {
                StringBuilder sb = new StringBuilder("All students: \n");
                for(Student stud : sms.getAllStudents()) {
                    sb.append(stud).append("\n");
                }
                    outputArea.setText(sb.toString());
            });
            btnSave.addActionListener(e -> {
                try {
                    sms.saveToFile("students.txt");
                    JOptionPane.showMessageDialog(frame, "Data saved to students.txt!");
                    }
                catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving data!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            btnLoad.addActionListener(e -> {
                try {
                    sms.loadFromFile("students.txt");
                    JOptionPane.showMessageDialog(frame, "Data loaded from students.txt!");
                }
                catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error loading data!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            frame.setVisible(true);
        }
        public static void main(String[] args) {
            new StudentManagementApp();
        }
}