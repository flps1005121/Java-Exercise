import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TaskPanel extends JPanel {
    private List<Task> tasks;
    private JList<Task> taskList;
    private DefaultListModel<Task> listModel;
    private CategoryManager categoryManager;
    private Timer timer;

    public TaskPanel(List<Task> tasks, CategoryManager categoryManager) {
        this.tasks = tasks;
        this.categoryManager = categoryManager;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setCellRenderer(new TaskCellRenderer());
        taskList.setBackground(new Color(255, 255, 255));
        taskList.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        taskList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = taskList.locationToIndex(e.getPoint());
                    if (selectedIndex >= 0) {
                        Task task = tasks.get(selectedIndex);
                        completeTask(task);
                    }
                }
            }
        });

        loadTasksToList();
        startReminderTimer();

        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton addTaskButton = new JButton("新增任务");
        styleButton(addTaskButton);
        addTaskButton.addActionListener(e -> addTaskDialog());
        buttonPanel.add(addTaskButton);

        JButton saveTaskButton = new JButton("保存任务");
        styleButton(saveTaskButton);
        saveTaskButton.addActionListener(e -> saveTasks());
        buttonPanel.add(saveTaskButton);

        JButton addCategoryButton = new JButton("新增类别");
        styleButton(addCategoryButton);
        addCategoryButton.addActionListener(e -> addCategoryDialog());
        buttonPanel.add(addCategoryButton);

        JButton sortByNameButton = new JButton("按名称排序");
        styleButton(sortByNameButton);
        sortByNameButton.addActionListener(e -> sortTasksByName());
        buttonPanel.add(sortByNameButton);

        JButton sortByDateButton = new JButton("按到期日排序");
        styleButton(sortByDateButton);
        sortByDateButton.addActionListener(e -> sortTasksByDueDate());
        buttonPanel.add(sortByDateButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadTasksToList() {
        listModel.clear();
        for (Task task : tasks) {
            listModel.addElement(task);
        }
    }

    private void completeTask(Task task) {
        int response = JOptionPane.showConfirmDialog(this, "确定完成任务: " + task.getName() + "？", "完成任务", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            tasks.remove(task);
            listModel.removeElement(task);
            saveTasks();
        }
    }

    private void addTaskDialog() {
        JTextField taskNameField = new JTextField(20);
        JTextField categoryField = new JTextField(20);
        JTextField dueDateField = new JTextField(20);
        String[] urgencies = {"低", "中", "高"};
        JComboBox<String> urgencyComboBox = new JComboBox<>(urgencies);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("任务名称:"));
        panel.add(taskNameField);
        panel.add(new JLabel("类别:"));
        panel.add(categoryField);
        panel.add(new JLabel("到期日 (YYYY-MM-DD):"));
        panel.add(dueDateField);
        panel.add(new JLabel("紧急程度:"));
        panel.add(urgencyComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "新增任务", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = taskNameField.getText();
            String category = categoryField.getText();
            String dueDate = dueDateField.getText();
            int urgencyLevel = urgencyComboBox.getSelectedIndex();

            Task newTask = new Task(name, category, dueDate, urgencyLevel);
            tasks.add(newTask);
            listModel.addElement(newTask);
            saveTasks();
        }
    }

    private void addCategoryDialog() {
        JTextField categoryNameField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("类别名称:"));
        panel.add(categoryNameField);

        int result = JOptionPane.showConfirmDialog(null, panel, "新增类别", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String categoryName = categoryNameField.getText();
            if (!categoryManager.hasCategory(categoryName)) {
                categoryManager.addCategory(categoryName);
                JOptionPane.showMessageDialog(this, "类别添加成功: " + categoryName);
            } else {
                JOptionPane.showMessageDialog(this, "类别已存在: " + categoryName);
            }
        }
    }

    private void saveTasks() {
        TaskManager.saveTasks(tasks);
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(100, 150, 200));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(120, 30));
    }

    private void sortTasksByName() {
        tasks.sort(Comparator.comparing(Task::getName));
        loadTasksToList();
    }

    private void sortTasksByDueDate() {
        tasks.sort(Comparator.comparing(Task::getDueDate));
        loadTasksToList();
    }

    private void startReminderTimer() {
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Task task : tasks) {
                    if (task.shouldNotify()) {
                        NotificationManager.showNotification("提醒", "任务 " + task.getName() + " 还剩 " + task.getDaysUntilDue() + " 天到期！");
                    }
                }
            }
        }, 0L, 24L * 60L * 60L * 1000L); // 每天检查一次
    }

    private static class TaskCellRenderer extends JPanel implements ListCellRenderer<Task> {
        private JLabel nameLabel;
        private JLabel categoryLabel;
        private JLabel dueDateLabel;

        public TaskCellRenderer() {
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            nameLabel = new JLabel();
            categoryLabel = new JLabel();
            dueDateLabel = new JLabel();
            add(nameLabel, BorderLayout.NORTH);
            add(categoryLabel, BorderLayout.CENTER);
            add(dueDateLabel, BorderLayout.SOUTH);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Task> list, Task task, int index, boolean isSelected, boolean cellHasFocus) {
            nameLabel.setText(task.getName());
            categoryLabel.setText("分类: " + task.getCategory());
            dueDateLabel.setText("剩余天数: " + task.getDaysUntilDue());

            // 添加紧急程度圆点
            JPanel urgencyPanel = new JPanel();
            urgencyPanel.setBackground(getUrgencyColor(task.getUrgencyLevel()));
            urgencyPanel.setPreferredSize(new Dimension(10, 10));
            urgencyPanel.setOpaque(true);

            // 创建一行显示详情
            JPanel detailPanel = new JPanel(new BorderLayout());
            detailPanel.add(urgencyPanel, BorderLayout.WEST);
            detailPanel.add(nameLabel, BorderLayout.CENTER);

            removeAll();
            add(detailPanel, BorderLayout.NORTH);
            add(categoryLabel, BorderLayout.CENTER);
            add(dueDateLabel, BorderLayout.SOUTH);

            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

            setOpaque(true);

            return this;
        }

        private Color getUrgencyColor(int urgencyLevel) {
            switch (urgencyLevel) {
                case 0:
                    return Color.GREEN; // 低紧急程度
                case 1:
                    return Color.ORANGE; // 中等紧急程度
                case 2:
                    return Color.RED; // 高紧急程度
                default:
                    return Color.GRAY; // 未定义的紧急程度
            }
        }
    }
}
