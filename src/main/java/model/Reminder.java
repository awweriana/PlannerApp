package model;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class Reminder {
    private static LocalDate lastCheckedDate = null;

    public static void checkReminder(List<Birthday> birthdayList) {
        LocalDate today = LocalDate.now();

        if (today.equals(lastCheckedDate)) {// проверка чтобы не показывать повторно в этот же день
            return;
        }

        boolean reminderShown = false;

        for (Birthday birthday : birthdayList) {
            LocalDate birthdayDay = birthday.getBirthday();

            if (birthdayDay.getDayOfMonth() == today.getDayOfMonth() &&
                    birthdayDay.getMonth() == today.getMonth()) {// проверяем день и месяц
                JOptionPane.showMessageDialog(null,
                        "Сегодня День Рождения у "+ birthday +"!",
                        "Напоминание",
                        JOptionPane.INFORMATION_MESSAGE);
                reminderShown = true;
            }
        }
        if (reminderShown) {
            lastCheckedDate = today;}
    }
}
