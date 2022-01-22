package dml;


import annotation.PrimaryKey;
import common.utiilities.Utilities;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public class SQLInsert extends SQLQuery {
    public <T> SQLInsert(Connection conn, String connectionString, T object) {
        super(conn, connectionString);

        SQLMapper SQLMapper = new SQLMapper();
        String tableName = SQLMapper.getTableName(object.getClass());

      try {
          Object nextPKValue = findNextPK(object.getClass(), tableName);
          String statement = "INSERT INTO " + tableName +
                  " (" + Utilities.getAttributeName(object) + ")" + " VALUES " +
                  " (" + Utilities.getAttributeValue(object, nextPKValue) + ");";
          this.query = statement;
          System.out.println("Statement: " + statement);
      }
      catch (Exception ex){
          ex.printStackTrace();
      }
    }

    private <T>Object findNextPK(Class<T> aClass, String tableName) throws IllegalAccessException, NoSuchFieldException {
       this.query = String.format("select * from %s", tableName);
       List<T> result = executeWithoutRelationship(aClass);
       if(result.size() == 0)
           return 1;
       List<Field> primaryKeyFields = new ArrayList<>();
       List<Integer> primaryKeyFieldValues = new ArrayList<>();

       primaryKeyFields = result.stream().map(t -> Arrays.stream(t.getClass().getDeclaredFields()).
               filter(field -> field.isAnnotationPresent(PrimaryKey.class)).findFirst().get()).collect(Collectors.toList());

       int index = 0;
       if(primaryKeyFields.size() == 0){
           String namePK = Arrays.stream(aClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(PrimaryKey.class)).findFirst().get().getName();
           Field PK = aClass.getDeclaredField(namePK);
           if(PK.getType().getSimpleName().equals("String"))
                return UUID.randomUUID().toString();
           else
               return 1;
       }
       else{
           for (Field field: primaryKeyFields) {
               field.setAccessible(true);
               if(field.get(result.get(index)) instanceof String)
                   return UUID.randomUUID().toString();
               primaryKeyFieldValues.add((Integer) field.get(result.get(index++)));
           }
       }
        return primaryKeyFieldValues.stream().max(Integer::compare).get() + 1;
    }
}
