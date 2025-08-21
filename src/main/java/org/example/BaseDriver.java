package org.example;

import java.sql.*;

/**
 * создать базу данных в памяти (не на диске)
 * h2database это позволяет, создать таблицы
 * из учебника секуэл для простых смертных
 * и сделать запросы
 */
public class BaseDriver {
    public static void main(String[] args) {
        //адрес бд, имя пользователя и пароль
        //строка DB_CLOSE_DELAY=-1 позволяет базе данных
        //в памяти сохраняться все время работы виртуальной машины
        String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        String userName = "dbuser";
        String passWord = "secret";

        try{
            System.out.println("First connect:");
            runDB(url, userName, passWord);
            System.out.println("\nSecond connect:");
            accessData(url, userName, passWord);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    //метод работы с базой данных
    static void runDB(String url, String usrName, String passWord) throws SQLException {
        try(Connection conn = DriverManager.getConnection(url, usrName, passWord)){
            Statement stat = conn.createStatement();
            stat.executeUpdate("CREATE TABLE Greetings (Message CHAR(80))");
            stat.executeUpdate("INSERT INTO Greetings VALUES ('Hola Amigo!')");
            stat.executeUpdate("INSERT INTO Greetings VALUES ('Testing h2-in-memory resume.')");
            try(ResultSet rez = stat.executeQuery("SELECT * FROM Greetings")){
                while(rez.next()) System.out.println(rez.getString(1));
            }
            // stat.executeUpdate("DROP TABLE Greetings");
        }
    }

    //метод доступа к базе данных в памяти, пока не закрыта виртуальная машина
    static void accessData(String url, String usrName, String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, usrName, password)){
            Statement stat = conn.createStatement();
            try(ResultSet result = stat.executeQuery("select * from Greetings")){
                while (result.next()) System.out.println(result.getString(1));
            }
        }
    }
}
