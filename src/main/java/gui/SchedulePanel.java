package gui;

import model.DaySchedule;
import service.impl.ScheduleServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulePanel extends JPanel {

    String[] days = {"Пн", "Вт", "Ср", "Чт", "Пт", "Сб"};
    private Map<String, JPanel> dayPanels;
    private Map<String, List<JTextField>> dayFields;
    private JButton saveButton;
    private final ScheduleServiceImpl scheduleService = new ScheduleServiceImpl();// для сохранения в БД

    public SchedulePanel() {

        dayPanels = new HashMap<>();// ключ- день недели. значение - список панелей
        dayFields = new HashMap<>();// ключ- день недели. значение - список полей

        setLayout(new BorderLayout());

        // заголовок
        JLabel title = new JLabel("Расписание пар");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // центральная панель
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        // добавляем прокрутку
        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        // панель для кнопок сохранить и изменить
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        saveButton = new JButton ("Сохранить");

        buttonPanel.add(saveButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // обработчик кнопки сохранить
        saveButton.addActionListener(e -> saveSchedules());

        //логика дней недели и панелек
        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);

        for (int i = 0; i < days.length; i++) {
            String day = days[i];
            LocalDate date = monday.plusDays(i);
            JPanel dayPanel = createDayPanel(day, date);
            dayPanels.put(day, dayPanel);
            panel.add(dayPanel);
        }
        add(panel, BorderLayout.CENTER);

        loadSchedulesFromDatabase();// метод при запуске возвращет данные из БД
    }

    private JPanel createDayPanel(String day, LocalDate date) {
        JPanel dayPanel = new JPanel();
        dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
        dayPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // название дня
        JLabel dayLabel = new JLabel(day);
        dayLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dayPanel.add(dayLabel, BorderLayout.NORTH);
        dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dayPanel.add(dayLabel);

        // Панели для пар и поля
        JPanel pairPanel = new JPanel();
        pairPanel.setLayout(new BoxLayout(pairPanel, BoxLayout.Y_AXIS));
        pairPanel.setLayout(new GridLayout(7,1,5,5));
        dayPanel.add(pairPanel);

        List<JTextField> fields = new ArrayList<>();// список где хранятся значения панелей

        for(int i=1; i<=5; i++) {// панелей 5 (нумерация)
            JTextField textField = new JTextField( i + ".");
            fields.add(textField);
            pairPanel.add(textField);
        }
        dayFields.put(day, fields);
        return dayPanel;
    }

    // логика кнопки
    private void saveSchedules() {
        for (String day : dayPanels.keySet()) {// проходимся по всем дням недели
            List<String> lessons = new ArrayList<>();// создаем список пар
            for (JTextField field : dayFields.get(day)) {// получаем список полей из dayFields
                String lessonText = field.getText();// получаем текст из поля
                lessons.add(lessonText);// добавляем текст в список
            }
            // создаём расписание
            DaySchedule schedule = new DaySchedule(day, LocalDate.now(), lessons);

            scheduleService.saveSchedule(schedule); // сохраняем в БД

            System.out.println(day + ": " + lessons);
        }
        JOptionPane.showMessageDialog(this, "Расписание сохранено!");
    }

    private void loadSchedulesFromDatabase() {// при запуске программы загружаем данные из БД
        List<DaySchedule> schedules = scheduleService.loadAllSchedules();// вызываем метод который возвращает значения из БД

        for (DaySchedule schedule : schedules) {// проходимся по каждому дню в расписании который вернул БД
            String day = schedule.getDayName();// получаем наименование дня недели
            List<String> lessons = schedule.getLessons();// получаем список уроков

            if (dayFields.containsKey(day)) {// есть ли для этого дня текстовые поля? Из Map<String, List<JTextField>>
                List<JTextField> fields = dayFields.get(day);// получаем список из текущего дня

                for (int i = 0; i < Math.min(fields.size(), lessons.size()); i++) {// составляем поля из значений в БД
                    fields.get(i).setText(lessons.get(i));
                }
            }
        }
    }
}
