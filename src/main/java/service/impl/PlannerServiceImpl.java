package service.impl;

import model.Task;
import service.PlannerService;
import utils.DBHelper;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlannerServiceImpl implements PlannerService {
    public void savePlannerService(Task task) {
        String sql = "INSERT INTO Task (id, title, description, taskDate) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // устанавливаем параметры в sql запросе
            stmt.setInt(1, task.getId());
            stmt.setString(2, task.getTitle());
            stmt.setString(3, task.getDescription());
            stmt.setDate(4, Date.valueOf(task.getDate()));
            stmt.executeUpdate();// сохраняем задачи в БД

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Task> loadAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, title, description, taskDate FROM Task";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                LocalDate date = rs.getDate("taskDate").toLocalDate();

                tasks.add(new Task(id, title, description, date));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }
    // удаление задачи из БД
    public void deleteTask(Task task) {
        String sql = "DELETE FROM task WHERE title = ? AND description = ? AND taskDate = ?";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setDate(3, Date.valueOf(task.getDate()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
