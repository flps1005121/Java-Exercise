import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;

public class ToDoListGUI extends JFrame {
    private ToDoList toDoList;
    private JTextArea taskTextArea;
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField deadlineField;
    private static final String TASKS_FILE = "tasks.txt";

    public ToDoListGUI() {
        super("To-Do List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        toDoList = new ToDoList();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("To-Do List");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        JLabel nameLabel = new JLabel("Name:");
        JLabel descriptionLabel = new JLabel("Description:");
        JLabel deadlineLabel = new JLabel("Deadline (YYYY-MM-DD):");
        nameField = new JTextField();
        descriptionField = new JTextField();
        deadlineField = new JTextField();
        JButton addTaskButton = new JButton("Add Task");

        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String description = descriptionField.getText();
                String deadlineStr = deadlineField.getText();
                if (!name.isEmpty() && !description.isEmpty() && !deadlineStr.isEmpty()) {
                    try {
                        LocalDate deadline = LocalDate.parse(deadlineStr);
                        Task task = new Task(name, description, deadline);
                        toDoList.addTask(task);
                        updateTaskTextArea();
                        nameField.setText("");
                        descriptionField.setText("");
                        deadlineField.setText("");
                        saveTasksToFile(); // Save tasks to file after adding a new task
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(ToDoListGUI.this, "Invalid deadline format. Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(ToDoListGUI.this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(descriptionLabel);
        inputPanel.add(descriptionField);
        inputPanel.add(deadlineLabel);
        inputPanel.add(deadlineField);
        inputPanel.add(new JLabel()); // Empty label for spacing
        inputPanel.add(addTaskButton);

        JPanel displayPanel = new JPanel(new BorderLayout());
        JLabel displayLabel = new JLabel("Tasks:");
        taskTextArea = new JTextArea(10, 30);
        taskTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taskTextArea);
        displayPanel.add(displayLabel, BorderLayout.NORTH);
        displayPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(displayPanel, BorderLayout.SOUTH);

        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        loadTasksFromFile(); // Move the call to loadTasksFromFile to the end of the constructor
    }

    private void updateTaskTextArea() {
        StringBuilder sb = new StringBuilder();
        for (Task task : toDoList.getTasks()) {
            sb.append(task).append("\n");
        }
        taskTextArea.setText(sb.toString());
    }

    private void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE))) {
            for (Task task : toDoList.getTasks()) {
                writer.write(task.getName() + "," + task.getDescription() + "," + task.getDeadline());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving tasks to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTasksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    String description = parts[1];
                    LocalDate deadline = LocalDate.parse(parts[2]);
                    Task task = new Task(name, description, deadline);
                    toDoList.addTask(task);
                }
            }
            updateTaskTextArea(); // Update the task text area after loading tasks
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading tasks from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ToDoListGUI();
            }
        });
    }
}
