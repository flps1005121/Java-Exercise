import java.time.LocalDate;

public class Task {
    private String name;
    private String category;
    private String dueDate;
    private int urgencyLevel; // 0: Low, 1: Medium, 2: High
    private boolean overdue;
    private boolean completed;

    public Task(String name, String category, String dueDate, int urgencyLevel, boolean overdue, boolean completed) {
        this.name = name;
        this.category = category;
        this.dueDate = dueDate;
        this.urgencyLevel = urgencyLevel;
        this.overdue = overdue;
        this.completed = completed;
    }

    public Task(String name, String category, String dueDate, int urgencyLevel) {
        this.name = name;
        this.category = category;
        this.dueDate = dueDate;
        this.urgencyLevel = urgencyLevel;
        this.overdue = false;
        this.completed = false;
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

    public boolean isOverdue() {
        return overdue;
    }

    public void checkOverdue() {
        LocalDate localDueDate = LocalDate.parse(dueDate);
        this.overdue = localDueDate.isBefore(LocalDate.now());
    }

    public void setCompleted() {
        this.completed = true;
        this.urgencyLevel = -1;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getDaysUntilDue() {
        try {
            if(isCompleted())
                return "已完成";
            else if(isOverdue())
                return "已過期";
            java.time.LocalDate due = java.time.LocalDate.parse(dueDate);
            java.time.LocalDate now = java.time.LocalDate.now();
            return String.valueOf(java.time.temporal.ChronoUnit.DAYS.between(now, due));
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public boolean shouldNotify() {
        return getDaysUntilDue().equals("1");
    }

    public void setName(String text) {
        this.name = text;
    }

    public void setCategory(String text) {
        this.category = text;
    }

    public void setDueDate(String text) {
        this.dueDate = text;
    }

    public void setUrgencyLevel(int selectedIndex) {
        this.urgencyLevel = selectedIndex;
    }

    @Override
    public String toString() {
        return name + " - " + category + " - 到期日: " + dueDate + " - 緊急程度: " + urgencyLevel;
    }

}
