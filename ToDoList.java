import java.util.ArrayList;
import java.util.List;

public class ToDoList {
    private List<Task> tasks;

    public ToDoList() {
        this.tasks = new ArrayList<>();
    }

    // 新增任務
    public void addTask(Task task) {
        tasks.add(task);
    }

    // 刪除任務
    public void removeTask(Task task) {
        tasks.remove(task);
    }

    // 標記任務為完成/未完成
    public void markTaskAsCompleted(Task task) {
        task.markAsCompleted();
    }

    // 顯示所有任務
    public void displayAllTasks() {
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    // 顯示已完成的任務
    public void displayCompletedTasks() {
        for (Task task : tasks) {
            if (task.isCompleted()) {
                System.out.println(task);
            }
        }
    }

    // 顯示未完成的任務
    public void displayIncompleteTasks() {
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                System.out.println(task);
            }
        }
    }

    public Task[] getTasks() {
        return tasks.toArray(new Task[0]);
    }
}
