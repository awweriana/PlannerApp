package service;

import model.FinanceRecord;
import java.util.List;

public interface FinanceService {
    void addIncome(String source, double amount);
    void addExpense(String purpose, double amount);
    double calculateBalance();
    List<FinanceRecord> getAllRecords();
    List<FinanceRecord> loadAllRecordsFromDatabase();
    void saveToDatabase(String description, double amount);
}
