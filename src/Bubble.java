import java.awt.Color;
import java.awt.Graphics;

public class Bubble {
    private Task task;
    private int currentDiameter;
    private static final int GROWTH_RATE = 2;  // 每次增长的增量
    private CategoryManager categoryManager;

    public Bubble(Task task, CategoryManager categoryManager) {
        this.task = task;
        this.currentDiameter = 0;
        this.categoryManager = categoryManager;
    }

    public Task getTask() {
        return task;
    }

    public int getDiameter() {
        return currentDiameter;
    }

    public void draw(Graphics g, int x, int y) {
        g.setColor(categoryManager.getColor(task.getCategory()));
        g.drawOval(x, y, currentDiameter, currentDiameter);
        g.setColor(Color.BLACK);
        g.drawString(task.getName(), x + currentDiameter / 2 - g.getFontMetrics().stringWidth(task.getName()) / 2, y + currentDiameter / 2);
    }

    public boolean grow() {
        int targetDiameter = getTargetDiameter();
        if (currentDiameter < targetDiameter) {
            currentDiameter += GROWTH_RATE;
            return true;  // 表明泡泡在生长并需要重绘
        } else {
            return false;
        }
    }

    public int getTargetDiameter() {
        switch (task.getImportance()) {
            case 1:
                return 50; // 不紧急也不重要，尺寸较小
            case 2:
                return 70; // 不紧急但重要
            case 3:
                return 90; // 紧急但不重要
            case 4:
                return 110; // 紧急且重要，尺寸较大
            default:
                return 50; // 默认尺寸
        }
    }
}
