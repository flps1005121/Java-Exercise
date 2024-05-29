import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;

public class TaskPanel extends JPanel {
    private List<Bubble> bubbles;
    private Timer timer;
    private CategoryManager categoryManager;

    public TaskPanel(List<Bubble> bubbles, CategoryManager categoryManager) {
        this.bubbles = bubbles;
        this.categoryManager = categoryManager;
        this.timer = new Timer(30, e -> animate());
        this.timer.start();

        // 添加鼠标点击事件来完成任务
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Bubble bubble : bubbles) {
                    int x = 50;
                    int y = 50 + bubbles.indexOf(bubble) * (bubble.getTargetDiameter());
                    Rectangle bubbleBounds = new Rectangle(x, y, bubble.getDiameter(), bubble.getDiameter());
                    if (bubbleBounds.contains(e.getPoint())) {
                        completeTask(bubble.getTask());
                        break;
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = 25;
        int y = 25;

        for (Bubble bubble : bubbles) {
            bubble.draw(g, x, y);
            y += bubble.getTargetDiameter() + 10;
        }
    }

    public void addTask(Task task) {
        bubbles.add(new Bubble(task, categoryManager));
        repaint(); // 重绘界面以便显示新任务
    }

    public void addTaskDialog() {
        JTextField taskNameField = new JTextField(10);
        JSlider importanceSlider = new JSlider(1, 4, 1);
        importanceSlider.setMajorTickSpacing(1);
        importanceSlider.setPaintTicks(true);
        importanceSlider.setPaintLabels(true);

        JComboBox<String> categoryComboBox = new JComboBox<>(categoryManager.getCategories().keySet().toArray(new String[0]));

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("任務名稱:"));
        panel.add(taskNameField);
        panel.add(new JLabel("重要性:"));
        panel.add(importanceSlider);
        panel.add(new JLabel("分類:"));
        panel.add(categoryComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Task", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String taskName = taskNameField.getText();
            int importance = importanceSlider.getValue();
            String category = (String) categoryComboBox.getSelectedItem();
            Task task = new Task(taskName, importance, category);
            addTask(task);
        }
    }

    private void animate() {
        boolean needRepaint = false;

        for (Bubble bubble : bubbles) {
            if (bubble.grow()) {
                needRepaint = true;  // 如果有泡泡在生长，则需要重绘
            }
        }

        // 移除已完成任务的泡泡
        Iterator<Bubble> iterator = bubbles.iterator();
        while (iterator.hasNext()) {
            Bubble bubble = iterator.next();
            if (bubble.getTask().isCompleted()) {
                iterator.remove();
                needRepaint = true;
            }
        }

        if (needRepaint) {
            repaint();
        }
    }

    public void completeTask(Task task) {
        task.setCompleted(true);
        animate();
    }

    // 增加分类方法
    public void addCategory() {
        JTextField categoryNameField = new JTextField(10);
        JComboBox<Color> colorComboBox = new JComboBox<>(new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA});
        colorComboBox.setRenderer(new ColorRenderer());

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("分類名稱:"));
        panel.add(categoryNameField);
        panel.add(new JLabel("選擇顏色:"));
        panel.add(colorComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "增加分類", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String categoryName = categoryNameField.getText();
            Color categoryColor = (Color) colorComboBox.getSelectedItem();
            categoryManager.addCategory(categoryName, categoryColor);
        }
    }

    // 色彩渲染器
    class ColorRenderer extends JLabel implements ListCellRenderer<Color> {
        public ColorRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Color> list, Color value, int index, boolean isSelected, boolean cellHasFocus) {
            setBackground(value);
            setText(" ");
            return this;
        }
    }
}
