package model;

public class FinanceRecord {
    private String description;
    private double amount;

    public FinanceRecord(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }
    @Override
    public String toString() {
        return description + " | Сумма: " + amount + " руб.";
    }
}
