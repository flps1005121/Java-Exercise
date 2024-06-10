public class Task {
    private String name;
    private String category;
    private String dueDate;
    private int urgencyLevel; // 0: Low, 1: Medium, 2: High
    private boolean isOverdue;

    public Task(String name, String category, String dueDate, int urgencyLevel) {
        this.name = name;
        this.category = category;
        this.dueDate = dueDate;
        this.urgencyLevel = urgencyLevel;
        this.isOverdue = false;
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

    public void setOverdue() {
        this.isOverdue = true;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public String getDaysUntilDue() {
        try {
            if(isOverdue)
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
