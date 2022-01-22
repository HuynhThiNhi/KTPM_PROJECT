package common.utiilities;


import annotation.Column;
import annotation.ManyToOne;
import annotation.PrimaryKey;
import common.Mapper;

import java.lang.reflect.Field;
import java.util.HashMap;


public class Utilities {

    public static String getAttributeName(Object obj) {
        StringBuilder fieldName = new StringBuilder();
        Class<?> zclass = obj.getClass();
        for (Field field : Mapper.getAllFields(zclass)) {
            if(field.isAnnotationPresent(Column.class)){
                Column column = field.getAnnotation(Column.class);
                fieldName.append(column.name()).append(", ");
            }
            else if(field.isAnnotationPresent(PrimaryKey.class)){
                PrimaryKey column = field.getAnnotation(PrimaryKey.class);
                fieldName.append(column.name()).append(", ");
            }
            else if(field.isAnnotationPresent(ManyToOne.class)){
                ManyToOne column = field.getAnnotation(ManyToOne.class);
                fieldName.append(column.columnName()).append(", ");

                }
            }

        fieldName = new StringBuilder(fieldName.substring(0, fieldName.length() - 2));
        return fieldName.toString();
    }

    public static StringBuilder appendObj(Object o, StringBuilder fieldValue){
        if (o instanceof String) {
            fieldValue.append("'").append(o).append("', ");

        } else {
            fieldValue.append(o).append(", ");
        }
        return fieldValue;
    }
    public static String getAttributeValue(Object obj, Object maxPKValue) throws IllegalAccessException, NoSuchFieldException {
        Field[] fields = obj.getClass().getDeclaredFields();
        StringBuilder fieldValue = new StringBuilder();
        for (Field field : fields) {
            field.setAccessible(true);
        }
        HashMap<String, Object> result = new HashMap<>();

        for (Field field : fields) {
            if(field.isAnnotationPresent(Column.class)){
                Column column = field.getAnnotation(Column.class);
                appendObj(field.get(obj), fieldValue);
            }
            else if(field.isAnnotationPresent(PrimaryKey.class)){
                PrimaryKey column = field.getAnnotation(PrimaryKey.class);
                if(column.autoGenerateId())
                    appendObj(maxPKValue, fieldValue);
                else
                    appendObj(field.get(obj), fieldValue);
            }
            else if(field.isAnnotationPresent(ManyToOne.class)){
                ManyToOne column = field.getAnnotation(ManyToOne.class);
                if(field.get(obj) == null){
                    appendObj(null, fieldValue);
                    return new StringBuilder(fieldValue.substring(0, fieldValue.length() - 2)).toString();
                }
                for(Field foreignField: field.get(obj).getClass().getDeclaredFields()){
                    if(foreignField.isAnnotationPresent(PrimaryKey.class)){
                        foreignField.setAccessible(true);
                        appendObj(foreignField.get(field.get(obj)), fieldValue);
                        break;
                    }
                }
            }
        }
        return new StringBuilder(fieldValue.substring(0, fieldValue.length() - 2)).toString();
    }
}
