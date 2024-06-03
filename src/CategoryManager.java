import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CategoryManager {
    private Map<String, Color> categories;

    public CategoryManager() {
        categories = new HashMap<>();
    }

    public void addCategory(String name) {
        categories.put(name, null); // 这里只是保持分类名称，不再管理颜色
    }

    public void removeCategory(String name) {
        categories.remove(name);
    }

    public boolean hasCategory(String name) {
        return categories.containsKey(name);
    }

    public Set<String> getCategories() {
        return categories.keySet();
    }
}