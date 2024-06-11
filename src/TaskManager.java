import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private static final String FILE_PATH = "tasks.csv"; // 保存文件的路徑

    public static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) { // 确保有4个部分
                    String name = parts[0];
                    String category = parts[1];
                    String dueDate = parts[2];
                    int urgencyLevel = Integer.parseInt(parts[3]); // 将紧急程度转换为整数
                    boolean overdue = Boolean.parseBoolean(parts[4]);
                    boolean completed = Boolean.parseBoolean(parts[5]);

                    tasks.add(new Task(name, category, dueDate, urgencyLevel, overdue, completed));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public static void saveTasks(List<Task> tasks) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Task task : tasks) {
                writer.println(task.getName() + "," + task.getCategory() + "," + task.getDueDate() + "," + task.getUrgencyLevel() + "," + task.isOverdue() + "," + task.isCompleted());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
