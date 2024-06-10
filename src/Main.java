import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("任務管理");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 500);

            CategoryManager categoryManager = new CategoryManager();
            categoryManager.loadCategoriesFromTasks(); // Load categories from tasks.csv file

            List<Task> tasks = TaskManager.loadTasks();

            TaskPanel taskPanel = new TaskPanel(tasks, categoryManager);
            frame.add(taskPanel);

            frame.setVisible(true);
        });
    }
}
