package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";

    public static Connection connect() {
        try {
            var con =  DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY));
            System.out.println("Подключение установлено, введите SQL выражение.");
            return con;
        } catch (SQLException e) {
            System.out.println("При подключении возникла ошибка.\nПожалуйста, проверьте настройки БД.");
            System.exit(0);
        }
        return null;
    }
}
