package common;

import annotation.Column;
import annotation.PrimaryKey;
import annotation.Table;
import annotation.*;
import connection.DBConnection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Mapper {
    public <T> T mapWithoutRelationship(Class<T> cls, ResultSet rs) {
        try {
            Field[] fields = cls.getDeclaredFields();
            T obj = cls.getConstructor().newInstance();

            for (Field field : fields) {
                Column column = field.getAnnotation(Column.class);
                PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);

                if (column != null) {
                    field.setAccessible(true);
                    field.set(obj, rs.getObject(column.name()));
                }

                if (primaryKey != null) {
                    field.setAccessible(true);
                    field.set(obj, rs.getObject(primaryKey.name()));
                }
            }
            return obj;
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }

    public final <T> T mapWithRelationship(DBConnection conn, Class<T> cls, ResultSet rs) {
        T obj = mapWithoutRelationship(cls, rs);
        mapManyToOne(conn, rs, obj);
        mapOneToMany(conn, rs, obj);
        mapManyToMany(conn, rs, obj);
        return obj;
    }

    protected abstract <T> void mapOneToMany(DBConnection conn, ResultSet rs, T obj);

    protected abstract <T> void mapManyToOne(DBConnection conn, ResultSet rs, T obj);

    protected abstract <T> void mapManyToMany(DBConnection conn, ResultSet rs, T obj);

    public <T> String getPrimaryKey(Class<T> cls) {
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == PrimaryKey.class) {
                    return field.getName();
                }
            }
        }

        return "";
    }

    public <T> List<String> getColumns(Class<T> cls) {
        Field[] fields = cls.getDeclaredFields();
        List<String> cols = new ArrayList<>();

        for (Field field : fields) {
            PrimaryKey pKey = field.getAnnotation(PrimaryKey.class);
            if (pKey != null) {
                field.setAccessible(true);
                cols.add(pKey.name());
            }

            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                field.setAccessible(true);
                cols.add(column.name());
            }

            ManyToOne manyToOneRelationship = field.getAnnotation(ManyToOne.class);
            if (manyToOneRelationship != null) {
                field.setAccessible(true);
                cols.add(manyToOneRelationship.tableName());
            }
        }
        if (cols.size() > 0)
            return cols;
        else
            return null;
    }

    public <T> String getTableName(Class<T> cls) {
        Table table = cls.getAnnotation(Table.class);
        if (table != null)
            return table.name();
        return "";
    }

    public static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>(Arrays.asList(type.getDeclaredFields()));

        Class<?> supperClass = type.getSuperclass();
        if (supperClass != null) {
            fields.addAll(getAllFields(supperClass));
        }
        return fields;
    }

}
