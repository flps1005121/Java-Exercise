import java.util.List;

public class Project {
    private String projectId;
    private String projectName;
    private String description;
    private List<String> members;
    private List<String> tasks;

    // Constructor
    public Project(String projectId, String projectName, String description, List<String> members, List<String> tasks) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.description = description;
        this.members = members;
        this.tasks = tasks;
    }

    // Getters and Setters
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", description='" + description + '\'' +
                ", members=" + members +
                ", tasks=" + tasks +
                '}';
    }
}
