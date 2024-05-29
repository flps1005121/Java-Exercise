import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Task Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        CategoryManager categoryManager = new CategoryManager();
        List<Bubble> bubbleList = new ArrayList<>();
        TaskPanel taskPanel = new TaskPanel(bubbleList, categoryManager);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(taskPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addTaskButton = new JButton("新增任務");
        JButton addCategoryButton = new JButton("新增分類");

        buttonPanel.add(addTaskButton);
        buttonPanel.add(addCategoryButton);

        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskPanel.addTaskDialog();
            }
        });

        addCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskPanel.addCategory();
            }
        });

        frame.setVisible(true);
    }
}
