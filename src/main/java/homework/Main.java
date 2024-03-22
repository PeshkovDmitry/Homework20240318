package homework;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try (Connection connection = getPostgreSqlConnection()) {
//            DatabaseTester tester = new SimpleDatabaseTester(connection, getStudents());
            DatabaseTester tester = new DatabaseWithReflectionTester(connection, getStudents());
            tester.test();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getH2Connection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:test");
    }

    private static Connection getPostgreSqlConnection() throws SQLException {
//         Настройка PostgreSQL
//         в BASH: sudo -i -u postgres
//         в BASH: psql
//         в POSTGRESQL: create user test with password 'test' superuser;
//         в POSTGRESQL: create database test;
//         Теперь есть пользователь test с паролем test,
//         и есть база данных test
        return DriverManager.getConnection("jdbc:postgresql://localhost/test","test","test");
    }

    public static List<Student> getStudents() {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, "Ivan", "Ivanov", 19));
        list.add(new Student(2, "Petr", "Petrov", 18));
        list.add(new Student(3, "Tatyana", "Semenova", 19));
        list.add(new Student(4, "Alla", "Sidorova", 18));
        return list;
    }

}