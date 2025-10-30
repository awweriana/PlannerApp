package gui;

import model.Task;
import service.impl.PlannerServiceImpl;
import controller.PlannerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PlannerPanel extends JPanel {
    String[] days = {"Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс", "План на день"};
    private PlannerController controller;
    private Map<String, JPanel> dayPanels;
    private final PlannerServiceImpl plannerService = new PlannerServiceImpl();
    private int id;

    public PlannerPanel() {

        controller = new PlannerController();// контроллер для хранения задач
        dayPanels = new HashMap<>();// хранит панели каждого дня Ключ(день недели) и значение (панель)

        setLayout(new BorderLayout());

        // заголовок
        JLabel title = new JLabel("Мой план на неделю");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // центральная панель (сетка дней)
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);// говорим что неделя начинается с понедельника

        for (int i = 0; i < days.length; i++) {
            String day = days[i];
            LocalDate date = i < 7 ? monday.plusDays(i) : monday;// от 0 до 6 дни недели если 7=7 то сегодняшняя дата

            JPanel dayPanel = createDayPanel(day, date);// создаем панель для конкретного дня недели
            dayPanels.put(day, dayPanel);// кладем панель в hash map
            panel.add(dayPanel);
        }
        add(panel, BorderLayout.CENTER);// добавление панели на экран

        loadTasksFromDataBase();// метод при запуске возвращает данные из БД
    }
    // прописываю панель для конкретного дня недели
    private JPanel createDayPanel(String dayName, LocalDate date) {

        JPanel dayPanel = new JPanel();// создаем панель задачами
        dayPanel.setLayout(new BorderLayout());
        dayPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // верх панели с названием дня
        JLabel dayLabel = new JLabel(dayName);
        dayLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dayPanel.add(dayLabel, BorderLayout.NORTH);

        // центральная часть панели со списком задач
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        dayPanel.add(taskPanel, BorderLayout.CENTER);

        // слушатель мыши
        dayPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String title = JOptionPane.showInputDialog("Введите название задачи: ");
                    if (title == null || title.trim().isEmpty()) {
                        return;
                    }
                    String description = JOptionPane.showInputDialog("Введите описание задачи: ");
                    if (description == null) {
                        description = "";
                    }

                    // создаем задачу
                    Task task = new Task(id, title, description, date);
                    controller.addTask(task);
                    plannerService.savePlannerService(task);// загружаем в БД

                    // создаем панель для метки и кнопки
                    JPanel taskItemPanel = new JPanel(new BorderLayout());

                    // создаем метку задачи
                    JLabel taskLabel = new JLabel(task.toString());

                    JButton deleteButton = new JButton("Удалить");

                    // слушатель кнопки
                    deleteButton.addActionListener(event -> {
                        int confirm = JOptionPane.showConfirmDialog(dayPanel,
                                "Удалить задачу: " + task.getTitle() + "?",
                                "Подтверждение",
                                JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            controller.removeTask(task);
                            plannerService.deleteTask(task);
                            taskPanel.remove(taskItemPanel);
                            taskPanel.revalidate();
                            taskPanel.repaint();
                        }
                    });

                    taskItemPanel.add(taskLabel, BorderLayout.WEST);
                    taskItemPanel.add(deleteButton, BorderLayout.EAST);

                    taskPanel.add(taskItemPanel);
                    taskPanel.revalidate();
                    taskPanel.repaint();

                    // обновляем UI
                    taskPanel.revalidate();
                    taskPanel.repaint();

                }
            }
        });

        return dayPanel;
    }

    private void loadTasksFromDataBase() {
        java.util.List<Task> tasks = plannerService.loadAllTasks();

        for (Task task : tasks) {
            LocalDate date = task.getDate();
            String dayName = convertDateToDayName(date);

            // если панель для этого дня уже есть
            if (dayPanels.containsKey(dayName)) {
                JPanel dayPanel = dayPanels.get(dayName);

                // создаём панель задачи с кнопкой удаления
                JPanel taskPanel = new JPanel(new BorderLayout());
                JLabel taskLabel = new JLabel("<html><b>" + task.getTitle() + "</b>: " + task.getDescription() + "</html>");
                JButton deleteButton = new JButton("Удалить");

                // обработчик кнопки
                deleteButton.addActionListener(e -> {
                    plannerService.deleteTask(task);// удалить из БД
                    dayPanel.remove(taskPanel);// удалить из панели
                    dayPanel.revalidate();
                    dayPanel.repaint();
                });

                taskPanel.add(taskLabel, BorderLayout.CENTER);
                taskPanel.add(deleteButton, BorderLayout.EAST);

                // добавляем в панель дня
                dayPanel.add(taskPanel);
            }
        }

        revalidate();
        repaint();
    }

    // метод преобразования LocalDate
    private String convertDateToDayName(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        switch (day) {
            case MONDAY: return "Пн";
            case TUESDAY: return "Вт";
            case WEDNESDAY: return "Ср";
            case THURSDAY: return "Чт";
            case FRIDAY: return "Пт";
            case SATURDAY: return "Сб";
            case SUNDAY: return "Вс";
            default: return "План на день";
        }
    }

}
