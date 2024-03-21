package homework;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
//        connectToH2();

        /**
         * Настройка PostgreSQL
         * в BASH: sudo -i -u postgres
         * в BASH: psql
         * в POSTGRESQL: create user test with password 'test' superuser;
         * в POSTGRESQL: create database test;
         * Теперь есть пользователь test с паролем test,
         * и есть база данных test
         */

        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost/test",
                "test",
                "test")) {
            createTableInPostgreSQL(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void connectToH2() {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")) {
            createTableInH2(connection);
            insertValuesInH2(connection);
            selectValuesInH2(connection);
            System.out.println("-----------------------");
            updateValuesInH2(connection);
            selectValuesInH2(connection);
            System.out.println("-----------------------");
            deleteValuesInH2(connection);
            selectValuesInH2(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteValuesInH2(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("delete from students where age = $1");
        statement.setInt(1, 20);
        statement.execute();
    }

    private static void updateValuesInH2(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("update students set age = $1 where first_name = $2");
        statement.setInt(1, 20);
        statement.setString(2, "Петр");
        statement.execute();
    }

    private static void selectValuesInH2(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select id, first_name, second_name, age from students");
        while (resultSet.next()) {
            System.out.println(
                    String.format(
                            "%d: %s %s %d лет",
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getInt(4))
            );
        }
    }

    private static void insertValuesInH2(Connection connection) throws SQLException {
        for (Student student:
             getStudents()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into students (id, first_name, second_name, age) values ($1, $2, $3, $4)"
            );
            preparedStatement.setInt(1, student.getId());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getSecondName());
            preparedStatement.setInt(4, student.getAge());
            preparedStatement.execute();
        }
    }

    private static void createTableInH2(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(
                "create table students (id int, first_name varchar(256), second_name varchar(256), age int)"
        );
    }

    private static void createTableInPostgreSQL(Connection connection) throws SQLException {
        connection.createStatement().execute(
                "drop table students"
        );

        connection.createStatement().execute(
                "create table students (id int, first_name varchar(256), second_name varchar(256), age int)"
        );
    }

    private static List<Student> getStudents() {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, "Иван", "Иванов", 19));
        list.add(new Student(2, "Петр", "Петров", 18));
        list.add(new Student(3, "Татьяна", "Семенова", 19));
        list.add(new Student(4, "Алла", "Сидорова", 18));
        return list;
    }


}