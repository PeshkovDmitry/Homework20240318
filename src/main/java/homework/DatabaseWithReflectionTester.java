package homework;

import java.sql.Connection;
import java.util.List;

public class DatabaseWithReflectionTester {

    private Connection connection;

    private List<Student> list;

    public DatabaseWithReflectionTester(Connection connection, List<Student> list) {
        this.connection = connection;
        this.list = list;
    }

    public void test() {


    }

    private void save(Object obj) {

    }




}
