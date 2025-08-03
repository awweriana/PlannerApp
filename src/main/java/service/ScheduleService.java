package service;

import model.DaySchedule;
import utils.DBHelper;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScheduleService {

    public void saveSchedule(DaySchedule schedule) {
        String sql = "INSERT INTO DaySchedule (day_name, date, lessons) VALUES (?, ?, ?)";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, schedule.getDayName());
            stmt.setDate(2, Date.valueOf(schedule.getDate()));
            stmt.setString(3, String.join(",", schedule.getLessons()));// список в строку

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<DaySchedule> loadAllSchedules() {
        List<DaySchedule> schedules = new ArrayList<>();// список в котором временно хранятся записи из БД
        String sql = "SELECT day_name, date, lessons FROM DaySchedule";

        try (Connection conn = DBHelper.getConnection();// подключение к БД через БДхэлпера
             PreparedStatement stmt = conn.prepareStatement(sql);// команда для выборки данных
             ResultSet rs = stmt.executeQuery()) {//executeQuery() выполняет запрос и возвращает результат в виде ResultSet

            while (rs.next()) {// перебираем строки из БД
                String dayName = rs.getString("day_name");
                LocalDate date = rs.getDate("date").toLocalDate();
                String[] lessonArray = rs.getString("lessons").split(",");
                List<String> lessons = Arrays.asList(lessonArray);
                schedules.add(new DaySchedule(dayName, date, lessons));// добавляем объекты в список
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return schedules;
    }
}
