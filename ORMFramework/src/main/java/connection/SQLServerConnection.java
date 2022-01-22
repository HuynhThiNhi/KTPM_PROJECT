package connection;

import dml.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class SQLServerConnection extends DBConnection{
    Connection conn;

    public SQLServerConnection(String url, int port, String username, String password, String db) {
        this.connectionString = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;user=%s;password=%s", url, String.valueOf(port), db, username, password);
    }
    public SQLServerConnection(String connectionString) {
        this.connectionString = connectionString;
    }

    @Override
    public void open() {
        try {
            conn = DriverManager.getConnection(connectionString);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void close() {
        try {
            conn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    public <T> IQueryBuilder<T> select(Class<T> cls) {
        return new SQLSelector(conn, connectionString, cls);
    }

    @Override
    public <T> List<T> executeQueryWithoutRelationship(Class<T> cls, String query) {
        SQLQuery sqlQuery = new SQLQuery(conn, query, connectionString);
        return sqlQuery.executeWithoutRelationship(cls);
    }

    @Override
    public <T> List<T> executeQuery(Class<T> cls, String query) {
        SQLQuery sqlQuery = new SQLQuery(conn, query, connectionString);
        return sqlQuery.execute(cls);
    }

    @Override
    public <T> boolean insert(T object) {
        SQLInsert sqlInsert = new SQLInsert(conn, connectionString, object);
        sqlInsert.executeUpdate();
        return true;
    }

    @Override
    public  <T> boolean update(T object){
        SQLUpdate sqlUpdate = new SQLUpdate(conn, connectionString, object);
        sqlUpdate.executeUpdate();
        return true;
    }

    @Override
    public  <T> boolean update(T object, String where){
        SQLUpdate sqlUpdate = new SQLUpdate(conn, connectionString, object, where);
        sqlUpdate.executeUpdate();
        return true;
    }

    @Override
    public <T> int delete(T object) {
        SQLDelete SQLDelete = new SQLDelete(conn, connectionString, object);
        return SQLDelete.executeUpdate();
    }

    @Override
    public <T> int delete(T object, String where) {
        SQLDelete SQLDelete = new SQLDelete(conn, connectionString, object, where);
        return SQLDelete.executeUpdate();
    }

    @Override
    public <T> int deleteAll(Class<T> cls) {
        SQLDelete SQLDelete = new SQLDelete(conn, connectionString, cls);
        return SQLDelete.executeUpdate();
    }
}
