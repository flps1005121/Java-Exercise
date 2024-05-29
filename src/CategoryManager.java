import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class CategoryManager {
    private Map<String, Color> categories;

    public CategoryManager() {
        categories = new HashMap<>();
        // 预定义的一些分类和对应的颜色
        categories.put("家事", Color.GREEN);
        categories.put("學校", Color.CYAN);
        // 可以添加更多预定义分类
    }

    public void addCategory(String name, Color color) {
        categories.put(name, color);
    }

    public Color getColor(String category) {
        return categories.getOrDefault(category, Color.WHITE); // 默认颜色为白色
    }

    public Map<String, Color> getCategories() {
        return categories;
    }
}
