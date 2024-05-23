import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        // 創建使用者
        User user1 = new User("1", "Alice", "alice@example.com", "password123");
        User user2 = new User("2", "Bob", "bob@example.com", "password123");

        // 顯示使用者資訊
        System.out.println(user1);
        System.out.println(user2);

        // 創建任務
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dueDate = LocalDate.parse("24/05/2024", formatter);
        Task task1 = new Task("1", "Complete project", "Finish the to-do list project", dueDate, Task.Status.PENDING, user1.getUserId(), user2.getUserId());

        // 顯示任務資訊
        System.out.println(task1);

        // 創建專案
        List<String> members = Arrays.asList(user1.getUserId(), user2.getUserId());
        List<String> tasks = Arrays.asList(task1.getTaskId());
        Project project = new Project("1", "To-Do List Project", "A project to manage tasks", members, tasks);

        // 顯示專案資訊
        System.out.println(project);
    }
}
