package org.example;

import org.example.utils.ConnectionManager;

import java.sql.*;
import java.util.Scanner;

public class JdbcRunner {


    public static void main(String[] args) throws SQLException {
        //Делаем подключение к БД с помощью специального класса
        try (Connection connection = ConnectionManager.connect();
             Scanner scanner = new Scanner(System.in)
        ) {
            //Запускаем бесконечный цикл
            while (true) {
                //Выводим приветствие
                System.out.print("SQL> ");
                String sqlCommand = scanner.nextLine().trim();

                //Команда для выхода
                if (sqlCommand.equals("QUIT")) {
                    break;
                }

                //Создание главного стэйтмена
                try (Statement mainStatement = connection.createStatement()) {
                    //Запуск sql кода и получения результатов. Если ResultSet, то вернет true, иначе false
                    boolean resultExecute = mainStatement.execute(sqlCommand);

                    //Обработка запроса на выборку. (Только если получили ResultSet)
                    if (resultExecute) {

                        //Достаем ResultSet и создаем вспомогательный стэйтмен для отправки запроса на общее кол-во строк
                        try (ResultSet resultSet = mainStatement.getResultSet();
                             Statement rowsCountStatement = connection.createStatement()) {

                            int columnCount = resultSet.getMetaData().getColumnCount();      // кол-во столбцов
                            String res = "";                                                 // результат для вывода

                            // Получение общего кол-ва строк без необходимости проходить по всем
                            int totalRowsCount = 1;
                            String tableName = resultSet.getMetaData().getTableName(1);
                            if(tableName != "") {
                                ResultSet countRs = rowsCountStatement.executeQuery(
                                        "SELECT COUNT(*) FROM " + tableName);
                                countRs.next();
                                totalRowsCount = countRs.getInt(1);
                            }

                            //Вывод строк
                            while (resultSet.next()) {
                                for (int i = 1; i <= columnCount; i++) {
                                    System.out.print(resultSet.getString(i) + "\t");
                                    res += resultSet.getString(i) + "\t";
                                }
                                res += "\n";
                                System.out.println();

                                //Если строк больше 10 дается выбор: вывести все или нет
                                if (res.split("\\n").length == 10 && tableName != "") {
                                    System.out.printf("В БД есть еще записи (всего %s). Загрузить все? (Y/N)\n", totalRowsCount);
                                    String answer = scanner.nextLine().trim();

                                    if (answer.equalsIgnoreCase("y")) {
                                        System.out.print(res);
                                    } else if (answer.equalsIgnoreCase("n")) {
                                        break;
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                // Если случилась ошибка, программа выводить сообщение об ошибке и продолжает исполнение
                } catch (SQLException e) {
                    String err = e.getMessage().replace("\n", "");
                    System.out.println(err);
                }
            }
        }
    }
}
