package gui;
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("PlannerApp");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();// панель с вкладками


        tabbedPane.addTab("Финансы", new FinancesPanel());
        tabbedPane.addTab("Учебное расписание", new SchedulePanel());
        tabbedPane.addTab("Планер", new PlannerPanel());
        tabbedPane.addTab("Дни Рождения", new BirthdayPanel());
        tabbedPane.addTab("Виш-лист", new WishlistPanel());

        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);

    }
}
