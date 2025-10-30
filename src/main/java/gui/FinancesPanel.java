package gui;

import model.FinanceRecord;
import service.impl.FinanceServiceImpl;

import javax.swing.*;
import java.awt.*;
import javax.swing.JTextArea;
import java.util.List;

public class FinancesPanel extends JPanel {

    private FinanceServiceImpl financeService;
    private JLabel balanceLabel;

    public FinancesPanel() {

        this.financeService = new FinanceServiceImpl();

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Мои финансы");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // панель дохода
        JPanel incomePanel = new JPanel();

        incomePanel.add(Box.createVerticalStrut(100));

        incomePanel.add(Box.createRigidArea(new Dimension(10, 0)));//отступ

        JLabel incomeText = new JLabel("Мой заработок");
        incomeText.setHorizontalAlignment(SwingConstants.CENTER);
        incomeText.setFont(new Font("Arial", Font.BOLD, 15));
        incomePanel.add(incomeText, BorderLayout.CENTER);

        JTextField incomeSourceField = new JTextField(10);
        JTextField incomeAmountField = new JTextField(5);
        JButton addIncomeButton = new JButton("Добавить доход");

        incomePanel.add(new JLabel("Источник: "));
        incomePanel.add(incomeSourceField);

        incomePanel.add(new JLabel("Сумма: "));
        incomePanel.add(incomeAmountField);

        incomePanel.add(addIncomeButton);

        add(incomePanel, BorderLayout.CENTER);

        // панель расходов
        JPanel expensesPanel = new JPanel();

        incomePanel.add(Box.createRigidArea(new Dimension(10, 0)));//отступ

        JLabel expensesText = new JLabel("Мои убытки");
        expensesText.setHorizontalAlignment(SwingConstants.CENTER);
        expensesText.setFont(new Font("Arial", Font.BOLD, 15));
        expensesPanel.add(expensesText, BorderLayout.CENTER);

        JTextField expensesPurposeField = new JTextField(10);
        JTextField expensesAmountField = new JTextField(5);
        JButton addExpensesButton = new JButton("Добавить расход");

        expensesPanel.add(new JLabel("Назначение:"));
        expensesPanel.add(expensesPurposeField);
        expensesPanel.add(new JLabel("Сумма:"));
        expensesPanel.add(expensesAmountField);
        expensesPanel.add(addExpensesButton);

        // панель оставшихся денег
        JPanel balancePanel = new JPanel();
        balanceLabel = new JLabel("Баланс: ");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balancePanel.add(balanceLabel);
        balancePanel.add(Box.createVerticalStrut(30));

        //доход + расход
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(incomePanel);
        centerPanel.add(expensesPanel);

        add(centerPanel, BorderLayout.CENTER);
        add(balancePanel, BorderLayout.SOUTH);

        // история операций
        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BorderLayout());

        JLabel storyText = new JLabel("История:");
        storyText.setFont(new Font("Arial", Font.BOLD, 16));
        storyText.setHorizontalAlignment(SwingConstants.CENTER);

        JTextArea historyText = new JTextArea();
        historyText.setEditable(false);
        historyText.setLineWrap(true);
        historyText.setWrapStyleWord(true);
        historyText.setFont(new Font("Arial", Font.PLAIN, 13));
        historyText.setPreferredSize(new Dimension(400, 150));

        JScrollPane historyScrollPane = new JScrollPane(historyText);
        historyScrollPane.setPreferredSize(new Dimension(400, 150));

        historyPanel.add(storyText, BorderLayout.NORTH);
        historyPanel.add(historyScrollPane, BorderLayout.CENTER);

        centerPanel.add(historyPanel);

        // обработчик кнопки
        addIncomeButton.addActionListener(e -> {
            try {
                // считываем текст из полей
                String source = incomeSourceField.getText();
                double amount = Double.parseDouble(incomeAmountField.getText());
                financeService.addIncome(source, amount);//вызываем финансСервис чтобы сохранить запись о доходе
                updateBalance();// метод пересчитываем баланс
                updateHistoryText(historyText, financeService.getAllRecords());// обновляем историю
                // очищаем текстовые поля
                incomeSourceField.setText("");
                incomeAmountField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Введите корректную сумму дохода.");
            }
        });

        addExpensesButton.addActionListener(e -> {
            try {
                String purpose = expensesPurposeField.getText();
                double amount = Double.parseDouble(expensesAmountField.getText());
                financeService.addExpense(purpose, amount);
                updateBalance();
                updateHistoryText(historyText, financeService.getAllRecords());
                expensesPurposeField.setText("");
                expensesAmountField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Введите корректную сумму расхода.");
            }
        });

        List<FinanceRecord> allRecords = financeService.loadAllRecordsFromDatabase();// сохраняем в БД

        for (FinanceRecord record : allRecords) {
            historyText.append(record.getDescription() + " — " + record.getAmount() + "\n");
        }
    }
    private void updateBalance() {
        double balance = financeService.calculateBalance();
        balanceLabel.setText("Баланс: " + balance + " руб.");
    }
    private void updateHistoryText(JTextArea historyText, List<FinanceRecord> records) {
        StringBuilder sb = new StringBuilder();
        for (FinanceRecord record : records) {
            sb.append(record.toString()).append("\n");
        }
        historyText.setText(sb.toString());
    }
}
