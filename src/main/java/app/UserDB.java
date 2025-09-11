package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

/**
 * Пользователь учебной базы данных.
 * Осуществляет доступ к БД с помощью метода
 * access()
 */
public class UserDB {
    private String url;
    private String userName;
    private String passWord;
    private boolean working = true;

    //constructor
    public UserDB(String url, String userName, String passWord) {
        this.passWord = passWord;
        this.url = url;
        this.userName = userName;
    }

    //получение пользователем доступа к базе данных
    //для выполнения запросов из консоли и вывода результатов
    //в нее
    public void access() {
        String s;
        System.out.println("Insert your query:");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (working) {
                try (Connection conn = DriverManager.getConnection(url, userName, passWord)) {
                    Statement stat = conn.createStatement();
                    s = reader.readLine();
                    //для выхода из программы введите quit
                    if (s.equals("quit")) working = false;
                    else {
                        try (ResultSet result = stat.executeQuery(s)) {
                            while (result.next()) {
                                //количество столбцов в ответе на запрос
                                int columns = result.getMetaData().getColumnCount();
                                System.out.print("| ");
                                for (int i = 1; i <= columns; i++) System.out.print(result.getString(i) + " | ");
                                System.out.println();
                            }
                        }
                    }
                } catch (SQLException e) {
                    System.err.println(e + "but we still working.");
                }
                System.out.println("Insert next query (help table - Greetings) or quit for stop work:");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
