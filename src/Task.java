import java.time.LocalDate;
import java.time.Period;

public class Task {
    private String name;
    private String category;
    private String dueDate;
    private int urgencyLevel; // 0: Low, 1: Medium, 2: High

    public Task(String name, String category, String dueDate, int urgencyLevel) {
        this.name = name;
        this.category = category;
        this.dueDate = dueDate;
        this.urgencyLevel = urgencyLevel;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDueDate() {
        return dueDate;
    }

    public int getUrgencyLevel() {
        return urgencyLevel;
    }

    public int getDaysUntilDue() {
        // Assuming dueDate is in format "YYYY-MM-DD"
        LocalDate due = LocalDate.parse(dueDate);
        return Period.between(LocalDate.now(), due).getDays();
    }

    public boolean shouldNotify() {
        // Example notification logic
        return getDaysUntilDue() <= 1; // Notify if due in 1 day or already overdue
    }
}
