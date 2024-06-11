import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
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
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    int selectedIndex = taskList.locationToIndex(e.getPoint());
                    if (selectedIndex >= 0) {
                        Task task = tasks.get(selectedIndex);
                        editTaskDialog(task);
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    int selectedIndex = taskList.locationToIndex(e.getPoint());
                    if (selectedIndex >= 0) {
                        taskList.setSelectedIndex(selectedIndex);
                        Task task = tasks.get(selectedIndex);
                        if (!task.isCompleted()) {completeTask(task);}
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

        JButton addTaskButton = new JButton("新增任務");
        styleButton(addTaskButton);
        addTaskButton.addActionListener(e -> addTaskDialog());
        buttonPanel.add(addTaskButton);

        JButton addCategoryButton = new JButton("新增類別");
        styleButton(addCategoryButton);
        addCategoryButton.addActionListener(e -> addCategoryDialog());
        buttonPanel.add(addCategoryButton);

        JButton sortTasksUrgencyLevel = new JButton("緊急程度↑");
        styleButton(sortTasksUrgencyLevel);
        sortTasksUrgencyLevel.addActionListener(e -> sortTasksUrgencyLevel());
        buttonPanel.add(sortTasksUrgencyLevel);

        JButton sortByDateButton = new JButton("到期日↑");
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
        int response = JOptionPane.showConfirmDialog(this, "確定完成任務: " + task.getName() + "？", "完成任務", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            task.setCompleted();
            saveTasks();
        }
    }

    private void addTaskDialog() {
        JTextField taskNameField = new JTextField(20);
        JTextField dueDateField = new JTextField(20);
        String[] urgencies = {"低", "中", "高"};
        JComboBox<String> urgencyComboBox = new JComboBox<>(urgencies);

        String[] categories = categoryManager.getCategories().toArray(new String[0]);
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("任務名稱:"));
        panel.add(taskNameField);
        panel.add(new JLabel("類別:"));
        panel.add(categoryComboBox);
        panel.add(new JLabel("到期日 (YYYY-MM-DD):"));
        panel.add(dueDateField);
        panel.add(new JLabel("緊急程度:"));
        panel.add(urgencyComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "新增任務", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = taskNameField.getText();
            String category = (String) categoryComboBox.getSelectedItem();
            String dueDate = dueDateField.getText();
            int urgencyLevel = urgencyComboBox.getSelectedIndex();

            Task newTask = new Task(name, category, dueDate, urgencyLevel);
            newTask.checkOverdue();
            tasks.add(newTask);
            listModel.addElement(newTask);
            saveTasks();
        }
    }

    private void editTaskDialog(Task task) {
        JTextField taskNameField = new JTextField(task.getName(), 20);
        JTextField dueDateField = new JTextField(task.getDueDate(), 20);
        String[] urgencies = {"低", "中", "高"};
        JComboBox<String> urgencyComboBox = new JComboBox<>(urgencies);
        urgencyComboBox.setSelectedIndex(task.getUrgencyLevel());

        String[] categories = categoryManager.getCategories().toArray(new String[0]);
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setSelectedItem(task.getCategory());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("任務名稱:"));
        panel.add(taskNameField);
        panel.add(new JLabel("類別:"));
        panel.add(categoryComboBox);
        panel.add(new JLabel("到期日 (YYYY-MM-DD):"));
        panel.add(dueDateField);
        panel.add(new JLabel("緊急程度:"));
        panel.add(urgencyComboBox);

        Object[] options = {"儲存修改", "刪除任務", "取消"};
        int result = JOptionPane.showOptionDialog(null, panel, "編輯任務", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (result == JOptionPane.YES_OPTION) {
            task.setName(taskNameField.getText());
            task.setCategory((String) categoryComboBox.getSelectedItem());
            task.setDueDate(dueDateField.getText());
            task.setUrgencyLevel(urgencyComboBox.getSelectedIndex());
            task.checkOverdue();

            loadTasksToList();
            saveTasks();
        } else if (result == JOptionPane.NO_OPTION) {
            int confirm = JOptionPane.showConfirmDialog(this, "確定刪除任務: " + task.getName() + "？", "刪除任務", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tasks.remove(task);
                listModel.removeElement(task);
                saveTasks();
            }
        }
    }

    private void addCategoryDialog() {
        JTextField categoryNameField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("類別名稱:"));
        panel.add(categoryNameField);

        int result = JOptionPane.showConfirmDialog(null, panel, "新增類別", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String categoryName = categoryNameField.getText();
            if (!categoryManager.hasCategory(categoryName)) {
                categoryManager.addCategory(categoryName);
                JOptionPane.showMessageDialog(this, "類別添加成功: " + categoryName);
            } else {
                JOptionPane.showMessageDialog(this, "類別已存在: " + categoryName);
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

    private void sortTasksUrgencyLevel() {
        tasks.sort(Comparator.comparing(Task::getUrgencyLevel).reversed());
        loadTasksToList();
        saveTasks();
    }

    private void sortTasksByDueDate() {
        tasks.sort(Comparator.comparing(Task::getDueDate));
        loadTasksToList();
        saveTasks();
    }

    private void startReminderTimer() {
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Task task : tasks) {
                    if (task.shouldNotify()) {
                        NotificationManager.showNotification("提醒", "任務 " + task.getName() + " 還剩 " + task.getDaysUntilDue() + " 天到期！");
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
            categoryLabel.setText("分類: " + task.getCategory());
            dueDateLabel.setText("剩餘天數: " + task.getDaysUntilDue());

            if (task.isOverdue()) {
                nameLabel.setForeground(Color.RED);
                categoryLabel.setForeground(Color.RED);
                dueDateLabel.setForeground(Color.RED);
            } else {
                nameLabel.setForeground(list.getForeground());
                categoryLabel.setForeground(list.getForeground());
                dueDateLabel.setForeground(list.getForeground());
            }

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
                case -1:
                    return Color.BLUE;
                default:
                    return Color.GRAY; // 未定义的紧急程度
            }
        }
    }
}
