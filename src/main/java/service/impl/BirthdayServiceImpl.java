package service.impl;

import model.Birthday;
import service.BirthdayService;
import utils.DBHelper;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BirthdayServiceImpl implements BirthdayService {
    public void saveBirthdayService(Birthday birthday) {
        String sql = "INSERT INTO Birthday (name, birthday) VALUES (?, ?)";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // устанавливаем параметры в sql запросе
            stmt.setString(1, birthday.getName());
            stmt.setDate(2, Date.valueOf(birthday.getBirthday()));

            stmt.executeUpdate();// сохраняем задачи в БД

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Birthday> loadAllBirthday() {
        List<Birthday> bd = new ArrayList<>();// список в котором временно хранятся записи из БД
        String sql = "SELECT name, birthday  FROM Birthday";

        try (Connection conn = DBHelper.getConnection();// подключение к БД через БДхэлпера
             PreparedStatement stmt = conn.prepareStatement(sql);// команда для выборки данных
             ResultSet rs = stmt.executeQuery()) {//executeQuery() выполняет запрос и возвращает результат в виде ResultSet

            while (rs.next()) {
                String name = rs.getString("name");
                LocalDate birthday = rs.getDate("birthday").toLocalDate();

                bd.add(new Birthday( name, birthday));// добавляем объекты в список
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return bd;
    }
    // удаление задачи из БД
    public void deleteBirthday(Birthday birthday) {
        String sql = "DELETE FROM Birthday WHERE name = ? AND birthday = ? ";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, birthday.getName());
            stmt.setDate(2, Date.valueOf(birthday.getBirthday()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

