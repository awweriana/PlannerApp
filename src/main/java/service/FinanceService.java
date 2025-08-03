package service;

import model.FinanceRecord;
import utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FinanceService {
    private final List<FinanceRecord> records = new ArrayList<>();

    public void addIncome(String source, double amount) {
        String description = "Доход: " + source;
        records.add(new FinanceRecord(description, amount));
        saveToDatabase(description, amount);
    }

    public void addExpense(String purpose, double amount) {
        String description = "Расход: " + purpose;
        records.add(new FinanceRecord(description, -amount));
        saveToDatabase(description, -amount);
    }

    public double calculateBalance() {
        return records.stream().mapToDouble(FinanceRecord::getAmount).sum();
    }

    public List<FinanceRecord> getAllRecords() {
        return new ArrayList<>(records);
    }

    //
    public List<FinanceRecord> loadAllRecordsFromDatabase() {
        List<FinanceRecord> loaded = new ArrayList<>();// список в котором временно хранятся записи из БД
        String sql = "SELECT description, amount FROM FinanceRecords";

        try (Connection conn = DBHelper.getConnection();// подключение к БД через БДхэлпера
             PreparedStatement stmt = conn.prepareStatement(sql);// команда для выборки данных
             ResultSet rs = stmt.executeQuery()) {//executeQuery() выполняет запрос и возвращает результат в виде ResultSet

            while (rs.next()) {// перебираем строки из БД
                String description = rs.getString("description");
                double amount = rs.getDouble("amount");
                loaded.add(new FinanceRecord(description, amount));// добавляем объекты в список
            }

            records.clear();
            records.addAll(loaded);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loaded;// возвращаем список
    }

    //сохранения в БД
    private void saveToDatabase(String description, double amount) {
        String sql = "INSERT INTO FinanceRecords (description, amount) VALUES (?, ?)";

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, description);
            statement.setDouble(2, amount);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
