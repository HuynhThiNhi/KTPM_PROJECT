package dml;

import annotation.ManyToMany;
import annotation.ManyToOne;
import annotation.OneToMany;
import annotation.Table;
import common.FETCHTYPE;
import connection.DBConnection;
import common.Mapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.util.List;

public class SQLMapper extends Mapper {
    @Override
    protected <T> void mapOneToMany(DBConnection conn, ResultSet rs, T obj) {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field: fields) {
                OneToMany oneToMany = field.getAnnotation(OneToMany.class);
                // Nếu có annotation OneToMany
                if (oneToMany != null && oneToMany.fetch().equals(FETCHTYPE.EAGER)) {
                    String tableName = oneToMany.tableName();
                    String refColumn = oneToMany.refColumn();

                    // Lấy primary key của class hiện tại
                    String primaryKey = getPrimaryKey(obj.getClass());

                    Field fieldPK = obj.getClass().getDeclaredField(primaryKey);
                    fieldPK.setAccessible(true);
                    String pkValue = fieldPK.get(obj).toString();

                    // Tạo câu lệnh query dữ liệu
                    String whereStr = String.format("%s.%s = %s", tableName, refColumn, pkValue);
                    String query = String.format("SELECT * FROM %s WHERE %s", tableName, whereStr);

                    // Lấy class của field
                    ParameterizedType listClasses = (ParameterizedType) field.getGenericType();
                    Class<?> fieldClass = (Class<?>) listClasses.getActualTypeArguments()[0];

                    conn.open();
                    List<Object> list = conn.executeQueryWithoutRelationship((Class<Object>) fieldClass, query);
                    field.setAccessible(true);
                    field.set(obj, list);
                    conn.close();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    protected <T> void mapManyToOne(DBConnection conn, ResultSet rs, T obj) {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field: fields) {
                ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
                // Nếu có annotation ManyToOne
                if (manyToOne != null && manyToOne.fetch().equals(FETCHTYPE.EAGER)) {
                    String tableName = manyToOne.tableName();
                    String refColumn = manyToOne.refColumn();
                    String columnName = manyToOne.columnName();

                    if(rs.getObject(columnName) == null){
                        return;
                    }
                    // Giá trị của khoá ngoại
                    String fieldValue = rs.getObject(columnName).toString();

                    // Tạo câu lệnh query dữ liệu
                    String whereStr = String.format("%s.%s = %s", tableName, refColumn, fieldValue);
                    String query = String.format("SELECT * FROM %s WHERE %s", tableName, whereStr);

                    conn.open();
                    List<Object> list = conn.executeQueryWithoutRelationship((Class<Object>) field.getType(), query);
                    field.setAccessible(true);
                    // Nếu tìm thấy thì trả phần tử đầu tiên không thì null
                    field.set(obj, (list.size()) == 0 ? null : list.get(0));
                    conn.close();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    protected <T> void mapManyToMany(DBConnection conn, ResultSet rs, T obj) {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field: fields) {
                ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
                // Nếu có annotation ManyToOne
                if (manyToMany != null && manyToMany.fetch().equals(FETCHTYPE.EAGER)) {
                    String refTableName = manyToMany.refTableName();
                    String refColumnName = manyToMany.refColumnName();
                    String columnName = manyToMany.columnName();

                    // Lấy primary key của class hiện tại
                    String currentTable = getTableName(obj.getClass());
                    String primaryKey = getPrimaryKey(obj.getClass());

                    Field fieldPK = obj.getClass().getDeclaredField(primaryKey);
                    fieldPK.setAccessible(true);
                    String pkValue = fieldPK.get(obj).toString();

                    ParameterizedType listClasses = (ParameterizedType) field.getGenericType();
                    Class<?> fieldClass = (Class<?>) listClasses.getActualTypeArguments()[0];
                    // Lấy tên table mà liên kết many-to-many kết nối đến
                    String targetTable = getTableName(fieldClass);
                    String targetColumn = getPrimaryKey(fieldClass);

                    // Tạo câu lệnh query
                    String joinStr1 = String.format("%s on (%s.%s=%s.%s)", refTableName, targetTable, targetColumn, refTableName, refColumnName);
                    String joinStr2 = String.format("%s on (%s.%s=%s.%s and %s.%s=%s)", currentTable, currentTable, primaryKey, refTableName, columnName, currentTable, primaryKey, pkValue);
                    String query = String.format("SELECT DISTINCT %s.* FROM %s JOIN %s JOIN %s", targetTable, targetTable, joinStr1, joinStr2);

                    conn.open();
                    List<Object> list = conn.executeQueryWithoutRelationship((Class<Object>) fieldClass, query);
                    field.setAccessible(true);
                    field.set(obj, list);
                    conn.close();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
