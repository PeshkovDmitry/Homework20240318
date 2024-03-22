package homework;

import homework.annotations.Column;
import homework.annotations.Id;
import homework.annotations.Table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ObjectToDatabaseSaver {

    public static void save(Object obj, Connection connection) throws SQLException {
        Table tableAnnotation = obj.getClass().getAnnotation(Table.class);
        if (tableAnnotation != null && !tableAnnotation.name().isEmpty()) {
            String table = tableAnnotation.name();
            Map<String, Field> fields = getObjectFields(obj);
            StringBuilder namesTemplateBuilder = new StringBuilder().append("(");
            StringBuilder valuesTemplateBuilder = new StringBuilder().append("(");
            for (String key :
                    fields.keySet()) {
                String value = getValueOfField(obj, fields.get(key));
                if (value != null) {
                    namesTemplateBuilder.append(key).append(",");
                    if (fields.get(key).getAnnotatedType().getType().equals(String.class)) {
                        valuesTemplateBuilder.append("\'");
                    }
                    valuesTemplateBuilder.append(value);
                    if (fields.get(key).getAnnotatedType().getType().equals(String.class)) {
                        valuesTemplateBuilder.append("\'");
                    }
                    valuesTemplateBuilder.append(",");
                }
            }
            namesTemplateBuilder.deleteCharAt(namesTemplateBuilder.length() - 1).append(")");
            valuesTemplateBuilder.deleteCharAt(valuesTemplateBuilder.length() - 1).append(")");
            String requestString = "insert into " + table + " " + namesTemplateBuilder.toString()
                    + " values " + valuesTemplateBuilder.toString();
            connection.createStatement().execute(requestString);
            System.out.println(obj);
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
