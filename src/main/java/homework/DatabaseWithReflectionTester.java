package homework;

import homework.annotations.Column;
import homework.annotations.Id;
import homework.annotations.Table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class DatabaseWithReflectionTester implements DatabaseTester {

    private Connection connection;

    private List<Student> students;

    public DatabaseWithReflectionTester(Connection connection, List<Student> students) {
        this.connection = connection;
        this.students = students;
    }

    public void test() throws SQLException {
        connection.createStatement().execute("drop table if exists students");
        connection.createStatement().execute(
                "create table students (id int, first_name varchar(256), second_name varchar(256), age int)"
        );
        ObjectToDatabaseSaver.save(students.get(1), connection);

    }






}
