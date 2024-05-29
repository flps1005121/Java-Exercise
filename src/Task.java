
import java.io.Serializable;

public class Task implements Serializable {
    private String name;
    private int importance;
    private String category;
    private boolean completed;  // 新增字段: 任务是否已完成

    public Task(String name, int importance, String category) {
        this.name = name;
        this.importance = importance;
        this.category = category;
        this.completed = false; // 初始状态为未完成
    }

    public String getName() {
        return name;
    }

    public int getImportance() {
        return importance;
    }

    public String getCategory() {
        return category;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
