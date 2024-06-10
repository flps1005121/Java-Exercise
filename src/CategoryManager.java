import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryManager {
    private List<String> categories;
    private static final String TASK_FILE_PATH = "tasks.csv"; // Path to the CSV file containing tasks

    public CategoryManager() {
        categories = new ArrayList<>();
    }

    public void loadCategoriesFromTasks() {
        categories.clear();
        Set<String> categorySet = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TASK_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String category = parts[1].trim(); // Assuming the second column is the category
                    categorySet.add(category);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        categories.addAll(categorySet);
    }

    public List<String> getCategories() {
        return categories;
    }

    public boolean hasCategory(String categoryName) {
        return categories.contains(categoryName);
    }

    public void addCategory(String categoryName) {
        if (!hasCategory(categoryName)) {
            categories.add(categoryName);
            // Optionally, save the new category to persistent storage
        }
    }

    public void removeCategory(String categoryName) {
        categories.remove(categoryName);
        // Optionally, save the changes to persistent storage
    }
}
