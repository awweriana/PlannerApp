import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MSSQLConnection {
    public static void main(String[] args) {
        // Строка подключения с использованием встроенной учетной записи
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=PlannerDB;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

        try {
            // Загрузка драйвера JDBC
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Установка соединения
            Connection connection = DriverManager.getConnection(connectionUrl);

            if (connection != null) {
                System.out.println("Соединение установлено успешно.");

                // Создаем Statement для выполнения SQL-запроса
                Statement statement = connection.createStatement();

                // Выполняем запрос к sys.databases для получения списка баз данных
                String query = "SELECT name FROM sys.databases";
                ResultSet resultSet = statement.executeQuery(query);

                // Выводим список баз данных
                System.out.println("Доступные базы данных:");
                while (resultSet.next()) {
                    String dbName = resultSet.getString("name");
                    System.out.println("- " + dbName);
                }

                // Закрываем ресурсы
                resultSet.close();
                statement.close();
                connection.close();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер JDBC не найден.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Ошибка при установке соединения или выполнении запроса.");
            e.printStackTrace();
        }
    }
}