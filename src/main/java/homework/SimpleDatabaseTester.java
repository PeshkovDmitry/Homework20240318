package homework;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleDatabaseTester extends DatabaseTester{

    private Connection connection;

    private List<Student> students;

    public SimpleDatabaseTester(Connection connection, List<Student> students) {
        super(connection);
        this.connection = connection;
        this.students = students;
    }

    private void insertValues(Connection connection) throws SQLException {
        for (Student student:
                students) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into students (id, first_name, second_name, age) values (?, ?, ?, ?)"
            );
            preparedStatement.setInt(1, student.getId());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getSecondName());
            preparedStatement.setInt(4, student.getAge());
            preparedStatement.execute();
        }
    }

    private void selectValues(Connection connection) throws SQLException {
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

    private void deleteValues(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("delete from students where age = ?");
        statement.setInt(1, 20);
        statement.execute();
    }

    private void updateValues(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("update students set age = ? where first_name = ?");
        statement.setInt(1, 20);
        statement.setString(2, "Петр");
        statement.execute();
    }

    @Override
    public void test() throws SQLException {
        dropTable(connection);
        createTable(connection);
        insertValues(connection);
        selectValues(connection);
        System.out.println("-----------------------");
        updateValues(connection);
        selectValues(connection);
        System.out.println("-----------------------");
        deleteValues(connection);
        selectValues(connection);
    }
}
