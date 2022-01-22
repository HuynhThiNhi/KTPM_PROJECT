package dml;

import annotation.PrimaryKey;
import common.utiilities.Utilities;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Arrays;

public class SQLUpdate extends SQLQuery {
    public <T> SQLUpdate(Connection conn, String connectionString, T object) {
        super(conn, connectionString);
        SQLMapper SQLMapper = new SQLMapper();
        String tableName = SQLMapper.getTableName(object.getClass());

        try {
            String PKName = Arrays.stream(object.getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(PrimaryKey.class)).findFirst().get().getName();
            Field id = object.getClass().getDeclaredField(PKName);
            id.setAccessible(true);
            Object PKValue = id.get(object);
            String[] utilitiesName = Utilities.getAttributeName(object).split(", ");
            String[] utilitiesValue = Utilities.getAttributeValue(object, PKValue).split(", ");
            String statement = "UPDATE " + tableName + " SET ";
            for (int index = 1; index < utilitiesName.length; index++) {
                if (utilitiesValue[index].compareTo("null") != 0) {
                    statement += utilitiesName[index] + " = " + utilitiesValue[index] + ",";
                }
            }
            statement = statement.substring(0, statement.length() - 1);
            statement += " WHERE " + PKName + " = " + PKValue + ";";
            this.query = statement;
            System.out.println("Statement: " + statement);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public <T> SQLUpdate(Connection conn, String connectionString, T object, String where) {
        super(conn, connectionString);
        SQLMapper SQLMapper = new SQLMapper();
        String tableName = SQLMapper.getTableName(object.getClass());
        boolean checkQueryUpdate = false;
        try {
            String[] utilitiesName = Utilities.getAttributeName(object).split(", ");
            String[] utilitiesValue = Utilities.getAttributeValue(object, 0).split(", ");
            String statement = "UPDATE " + tableName + " SET ";
            for (int index = 1; index < utilitiesName.length; index++) {
                if (utilitiesValue[index].compareTo("null") != 0) {
                    if (utilitiesName[index].compareTo(where) != 0) {
                        statement += utilitiesName[index] + " = " + utilitiesValue[index] + ",";
                    }
                }
            }
            statement = statement.substring(0, statement.length() - 1);
            for (int indexCompare = 1; indexCompare < utilitiesName.length; indexCompare++) {
                if (utilitiesName[indexCompare].compareTo(where) == 0) {
                    statement += " WHERE  " + where + " = " + utilitiesValue[indexCompare] + ";";
                    checkQueryUpdate = true;
                }
            }
            if(checkQueryUpdate) {
                this.query = statement;
                System.out.println("Statement: " + statement);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}