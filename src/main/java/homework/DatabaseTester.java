package homework;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseTester {

    Connection connection;

    public DatabaseTester(Connection connection) {
        this.connection = connection;
    }

    public void dropTable(Connection connection) throws SQLException {
        connection.createStatement().execute("drop table if exists students");
    }

    public void createTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(
                "create table students (id int, first_name varchar(256), second_name varchar(256), age int)"
        );
    }

    public abstract void test() throws SQLException;

}
