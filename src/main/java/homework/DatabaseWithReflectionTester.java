package homework;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DatabaseWithReflectionTester extends DatabaseTester {

    private Connection connection;

    private List<Student> students;

    public DatabaseWithReflectionTester(Connection connection, List<Student> students) {
        super(connection);
        this.connection = connection;
        this.students = students;
    }

    public void test() throws SQLException {
        dropTable(connection);
        createTable(connection);
        for (Student student :
                students) {
            ObjectToDatabaseSaver.save(student, connection);
        }
        ObjectToDatabaseSaver.update(
                new Student(6, "Makar", "Grebnev", 22),
                students.get(1),
                connection);
    }
}
