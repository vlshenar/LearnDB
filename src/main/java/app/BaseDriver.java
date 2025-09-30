package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**
 * создать базу данных в памяти (не на диске)
 * с помощью h2database, создать таблицы
 * из учебника SQL для простых смертных
 * и сделать запросы
 */
public class BaseDriver {
    public static void main(String[] args) {
        //адрес бд, имя пользователя и пароль
        //строка jdbc:h2:mem создает базу данных в основной памяти
        //не обращаясь к внешней памяти устройства
        //строка DB_CLOSE_DELAY=-1 позволяет базе данных
        //в памяти сохраняться все время работы виртуальной машины
        String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        String userName = "dbuser";
        String passWord = "secret";

        //создание небольшой учебной базы данных
        //в основной памяти sql-командами из файла
        try{
            System.out.println("Welcome to sql-practice");
            fillDataBase(url, userName, passWord);
        } catch (SQLException e){
            System.err.println("Exeption was CAUGHT!!!");
            e.printStackTrace(System.err);
            System.exit(1);
        }

        new UserDB(url, userName, passWord).access();
    }

    //метод создания базы данных и заполнения ее sql-командами из
    //текстового файла
    static void fillDataBase(String url, String usrName, String passWord) throws SQLException {
        try(Connection conn = DriverManager.getConnection(url, usrName, passWord);
            BufferedReader reader = new BufferedReader(
                    new FileReader("src/main/resources/dataconstructor.txt"))){
            Statement stat = conn.createStatement();
            String statSentense;    //команда sql
            while((statSentense = reader.readLine()) != null)
                    stat.executeUpdate(statSentense);

            //вывод таблиц, содержащихся в данной учебной бд
            try(ResultSet result = stat.executeQuery("SELECT * FROM Greetings")){
                while(result.next()) System.out.println("|" +  result.getString(1) + " | " + result.getString(2) + " |");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
