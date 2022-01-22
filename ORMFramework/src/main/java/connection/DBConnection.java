package connection;

import dml.IQueryBuilder;

import java.util.List;

public abstract class DBConnection {
    protected String connectionString;

    public abstract void open();
    public abstract void close();
    public abstract <T> IQueryBuilder<T> select(Class<T> cls);

    public abstract <T> List<T> executeQueryWithoutRelationship(Class<T> cls, String query);
    public abstract <T> List<T> executeQuery(Class<T> cls, String query);
    public abstract <T> boolean insert(T object);
    public abstract <T> boolean update(T object);
    public abstract <T> boolean update(T object, String where);
    public abstract <T> int delete(T object);
    public abstract <T> int delete(T object, String where);
    public abstract <T> int deleteAll(Class<T> cls);
}