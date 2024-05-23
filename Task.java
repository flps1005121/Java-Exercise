import java.time.LocalDate;

public class Task {
    private String taskId;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Status status;
    private String assigneeId;
    private String creatorId;

    public enum Status {
        PENDING, IN_PROGRESS, COMPLETED
    }

    // Constructor
    public Task(String taskId, String title, String description, LocalDate dueDate, Status status, String assigneeId, String creatorId) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.assigneeId = assigneeId;
        this.creatorId = creatorId;
    }

    // Getters and Setters
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", status=" + status +
                ", assigneeId='" + assigneeId + '\'' +
                ", creatorId='" + creatorId + '\'' +
                '}';
    }
}
