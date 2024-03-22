package homework;

import homework.annotations.Column;
import homework.annotations.Id;
import homework.annotations.Table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectToDatabaseSaver {

    public static void save(Object obj, Connection connection) throws SQLException {
        Table tableAnnotation = obj.getClass().getAnnotation(Table.class);
        if (tableAnnotation != null && !tableAnnotation.name().isEmpty()) {
            String table = tableAnnotation.name();
            Map<String, Field> fields = getObjectFields(obj);
            String requestString = "insert into " + table + " ("
                    + String.join(",", fields.keySet())
                    + ") values ("
                    + String.join(",", fields.values().stream().map(f -> getValueOfField(obj, f)).collect(Collectors.toList())) + ")";
            connection.createStatement().execute(requestString);
            System.out.println(requestString);
        }
    }

    public static void update(Object newObject, Object oldObject, Connection connection) throws SQLException {
        Table newObjectTableAnnotation = newObject.getClass().getAnnotation(Table.class);
        Table oldObjectTableAnnotation = oldObject.getClass().getAnnotation(Table.class);
        if (newObjectTableAnnotation != null
                && oldObjectTableAnnotation != null
                && !newObjectTableAnnotation.name().isEmpty()
                && newObjectTableAnnotation.name().equals(oldObjectTableAnnotation.name())) {
            String table = newObjectTableAnnotation.name();
            Map<String, Field> fieldsFromOldObject = getObjectFields(oldObject);
            Map<String, Field> fieldsFromNewObject = getObjectFields(newObject);
            fieldsFromNewObject.remove("id");
            for (String key : fieldsFromNewObject.keySet()) {
                String requestString = String.format(
                        "update %s set %s = %s where id = %s",
                        table,
                        key,
                        getValueOfField(newObject, fieldsFromNewObject.get(key)),
                        getValueOfField(oldObject, fieldsFromOldObject.get("id"))
                );
                System.out.println(requestString);
                connection.createStatement().execute(requestString);
            }
        }
    }




    private static Map<String, Field> getObjectFields(Object obj) {
        Map<String, Field> fields = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.getAnnotation(Id.class) != null) {
                fields.put("id", field);
            } else if (field.getAnnotation(Column.class) != null) {
                fields.put(field.getAnnotation(Column.class).name(), field);
            }
        }
        return fields;
    }

    private static String getValueOfField(Object obj, Field field) {
        Method getter = null;
        for (Method method :
                obj.getClass().getMethods()) {
            if (method.getName().toLowerCase().equals("get" + field.getName().toLowerCase())) {
                getter = method;
            }
        }
        if (getter != null) {
            try {
                if (field.getAnnotatedType().getType().equals(String.class)) {
                    return "\'" + String.valueOf(getter.invoke(obj)) + "\'";
                }
                return String.valueOf(getter.invoke(obj));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

}
