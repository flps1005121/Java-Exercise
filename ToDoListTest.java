import java.time.LocalDate;

public class ToDoListTest {
    public static void main(String[] args) {
        // 創建待辦清單
        ToDoList toDoList = new ToDoList();

        // 新增一些任務
        Task task1 = new Task("完成專題", "完成期末專題的程式碼", LocalDate.of(2024, 5, 30));
        Task task2 = new Task("準備考試", "準備資工系期末考試的學習", LocalDate.of(2024, 6, 10));
        Task task3 = new Task("健身", "進行每日30分鐘的健身", LocalDate.of(2024, 5, 25));

        // 將任務新增到待辦清單中
        toDoList.addTask(task1);
        toDoList.addTask(task2);
        toDoList.addTask(task3);

        // 顯示所有任務
        System.out.println("所有任務：");
        toDoList.displayAllTasks();

        // 標記一個任務為完成
        toDoList.markTaskAsCompleted(task1);

        // 顯示已完成的任務
        System.out.println("\n已完成的任務：");
        toDoList.displayCompletedTasks();

        // 顯示未完成的任務
        System.out.println("\n未完成的任務：");
        toDoList.displayIncompleteTasks();
    }
}
