package gui;

import model.Birthday;
import model.Reminder;
import service.BirthdayService;

import javax.swing.*;
import java.awt.*;
import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BirthdayPanel extends JPanel {

    private CalendarPanel calendarPanel;
    private DefaultListModel<Birthday> birthdayListModel; //добавление удаление
    private JList<Birthday> birthdayList; //список
    private JTextField nameField;// поле ввода имени
    private JButton addButton, removeButton;
    private DatePicker datePicker; // компонент даты
    private final BirthdayService birthdayService = new BirthdayService();

    public BirthdayPanel() {
        setLayout(new BorderLayout());// устанавливаем менеджер компоновки

        // Заголовок
        JLabel title = new JLabel("Дни рождения");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // Центральная панель
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 20, 10);// отступы по сетке

        // создаем календарь
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        calendarPanel = new CalendarPanel();
        calendarPanel.setPreferredSize(new Dimension(400, 300));
        leftPanel.add(calendarPanel);

        JPanel calendarWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));// оборачиваем календарь в calendarWrapper
        calendarWrapper.add(calendarPanel);// добавляем в calendarPanel

        leftPanel.add(Box.createVerticalStrut(50)); // Отступ
        leftPanel.add(calendarWrapper);// добавляем календарь в центральную панель

        // Поля ввода
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));// создаем панель

        inputPanel.add(new JLabel("Имя: "));
        nameField = new JTextField(10);// создаем поле ввода имени
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Дата: "));
        DatePickerSettings settings = new DatePickerSettings();// объект настройки календаря
        settings.setFormatForDatesCommonEra("dd-MM-yyyy");
        datePicker = new DatePicker(settings); // создаем календарь (ввод)
        inputPanel.add(datePicker);

        leftPanel.add(inputPanel);// добавляем в интерфейс

        // Кнопки
        //создаем кнопки
        addButton = new JButton("Добавить");
        removeButton = new JButton("Удалить");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // добавляем кнопки в панель
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        leftPanel.add(buttonPanel);

        // Список
        birthdayListModel = new DefaultListModel<>(); // создаем список
        birthdayList = new JList<>(birthdayListModel); // отображение его элементов
        birthdayList.setVisibleRowCount(8);
        birthdayList.setFixedCellWidth(200);

        JScrollPane scrollPane = new JScrollPane(birthdayList); //оборачиваем список в панель прокрутки
        scrollPane.setPreferredSize(new Dimension(100, 150)); //размер

        // левая панель
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;// прижимаем вверх
        gbc.fill = GridBagConstraints.CENTER;// не растягиваем компонент
        centerPanel.add(leftPanel, gbc);

        //правая панель
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;// если окно увеличится то панель можно растянуть
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.BOTH;// панель растягивается по ширине и высоте
        centerPanel.add(scrollPane, gbc);

        //добавляем все в birthdayPanel
        add(centerPanel, BorderLayout.CENTER);

        // обработчик кнопки добавить
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            LocalDate date = datePicker.getDate();

            if (!name.isEmpty() && date != null) {
                Birthday birthday = new Birthday(name, date); // создаём объект Birthday
                birthdayListModel.addElement(birthday);// добавляем в модель списка
                birthdayService.saveBirthdayService(birthday);// добавляем в бд

                calendarPanel.setSelectedDate(date);// если есть календарь выделим дату
                nameField.setText("");// очищаем поле
                datePicker.clear();// очищаем дату

            } else {
                JOptionPane.showMessageDialog(this, "Введите имя и выберите дату", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        // обработчик кнопки удалить
        removeButton.addActionListener(e -> {
            int selected = birthdayList.getSelectedIndex();
            if (selected != -1) {
                Birthday birthday = birthdayList.getModel().getElementAt(selected);// сохраняем объект до удаления
                birthdayListModel.remove(selected);
                birthdayService.deleteBirthday(birthday);// сначала удалить из БД
                birthdayListModel.remove(selected);// удаляем из списка
            }
        });

        loadBirthdayFromDataBase();

        // достаем элементы birthdayList
        DefaultListModel<Birthday> model = (DefaultListModel<Birthday>) birthdayList.getModel();
        List<Birthday> birthdayList = new ArrayList<>();// создаем новый список
        for (int i = 0; i < model.getSize(); i++) {
            birthdayList.add(model.getElementAt(i));// добавляем все элементы в новый список
        }
        // создаем поток
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                Reminder.checkReminder(birthdayList);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();// метод запускается после 3 сек работы программы

    }
    private void loadBirthdayFromDataBase() {
        List<Birthday> birthdays = birthdayService.loadAllBirthday();// загружаем все дни рождения

        birthdayListModel.clear();// очищаем список если он уже есть

        for (Birthday birthday : birthdays) {
            birthdayListModel.addElement(birthday);// добавляем каждый день рождения в список
        }
    }

    private void checkReminder() {// уведомления
        List<Birthday> birthdayList = new ArrayList<>();
        for (int i = 0; i < birthdayListModel.size(); i++) {
            birthdayList.add(birthdayListModel.getElementAt(i));
        }
        Reminder.checkReminder(birthdayList);

        // проверка каждые 24 часа
        int delay = 86400000;
        Timer dailyReminderTimer = new Timer(delay, e -> checkReminder());
        dailyReminderTimer.setInitialDelay(0);
        dailyReminderTimer.start();
    }

}
