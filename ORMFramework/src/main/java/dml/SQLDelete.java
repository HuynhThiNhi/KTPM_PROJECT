package dml;

import annotation.PrimaryKey;
import common.utiilities.Utilities;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Arrays;


public class SQLDelete extends SQLQuery {
    public <T> SQLDelete(Connection connection, String connectionString, T object) {
        super(connection, connectionString);
        SQLMapper mapper = new SQLMapper();
        String tableName = mapper.getTableName(object.getClass());

        try {
            String PKName = Arrays.stream(object.getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(PrimaryKey.class)).findFirst().get().getName();
            Field id = object.getClass().getDeclaredField(PKName);
            id.setAccessible(true);
            Object PKValue = id.get(object);
            this.query = String.format("Delete from %s where %s = %s", tableName, PKName, PKValue);
            System.out.println(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public <T> SQLDelete(Connection connection, String connectionString, T object, String where) {
        super(connection, connectionString);
        SQLMapper mapper = new SQLMapper();
        String tableName = mapper.getTableName(object.getClass());

        try {
            String[] utilitiesName = Utilities.getAttributeName(object).split(", ");
            String[] utilitiesValue = Utilities.getAttributeValue(object, 0).split(", ");
            for (int index = 1; index < utilitiesName.length; index++) {
                if(utilitiesName[index].compareTo(where)==0) {
                    this.query = String.format("Delete from %s where %s = %s", tableName, where, utilitiesValue[index]);
                    System.out.println(query);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public <T> SQLDelete(Connection connection, String connectionString, Class<T> cls) {
        super(connection, connectionString);
        SQLMapper mapper = new SQLMapper();
        String tableName = mapper.getTableName(cls);

        try {

            this.query = String.format("Delete from %s", tableName);
            System.out.println(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
