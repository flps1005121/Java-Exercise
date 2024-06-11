import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private static final String FILE_PATH = "tasks.csv"; // 保存文件的路径

    // 加载任务列表
    public static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(FILE_PATH);

        // 检查文件是否存在
        if (!file.exists()) {
            // 如果文件不存在，创建一个空文件
            try {
                file.createNewFile();
                System.out.println("任务文件不存在，已创建一个空文件。");
            } catch (IOException e) {
                System.out.println("无法创建任务文件: " + e.getMessage());
            }
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) { // 确保有6个部分
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
            System.out.println("加载任务列表时出错: " + e.getMessage());
        }
        return tasks;
    }

    public static void saveTasks(List<Task> tasks) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Task task : tasks) {
                writer.println(task.getName() + "," + task.getCategory() + "," + task.getDueDate() + "," + task.getUrgencyLevel() + "," + task.isOverdue() + "," + task.isCompleted());
            }
        } catch (IOException e) {
            System.out.println("保存任务列表时出错: " + e.getMessage());
        }
    }
}
