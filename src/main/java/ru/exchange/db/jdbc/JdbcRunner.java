package ru.exchange.db.jdbc;

import ru.exchange.db.utils.ConnectionManger;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcRunner {
    public static void main(String[] args) {
        getExchange();
    }

    public static void getExchange(){
        String sql = """
                select * from currencies;
                """;

       try (Connection connection = ConnectionManger.open();
        var statement = connection.createStatement()){
           var result = statement.executeQuery(sql);
           while (result.next()){
               System.out.println(result.getString("Code"));
               System.out.println(result.getString("FullName"));
               System.out.println(result.getString("Sign"));
               System.out.println("_______________");
           }
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
    }
}
