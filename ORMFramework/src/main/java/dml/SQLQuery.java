package dml;

import connection.DBConnection;
import common.Mapper;
import connection.MySQLConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLQuery implements IQuery {
    protected Connection conn; // Đối tượng Connection được lấy từ java.sql.Connection;
    protected String query;
    protected String connectionString;
    
    public SQLQuery(Connection conn, String query, String connectionString) {
        this.conn = conn;
        this.query = query;
        this.connectionString = connectionString;
    }
    public SQLQuery(Connection conn, String connectionString) {
        this.conn = conn;
        this.connectionString = connectionString;
    }

    public <T> List<T> executeWithoutRelationship(Class<T> cls) {
        List<T> result = new ArrayList<T>();
        try {
            Mapper mapper = new SQLMapper();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                result.add(mapper.mapWithoutRelationship(cls, rs));
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return result;
    }

    @Override
    public <T> List<T> execute(Class<T> cls) {
        List<T> result = new ArrayList<T>();
        try {
            Mapper mapper = new SQLMapper();
            DBConnection dbConn = new MySQLConnection(connectionString);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                result.add(mapper.mapWithRelationship(dbConn, cls, rs));
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return result;
    }

    @Override
    public <T> int executeUpdate() {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeUpdate(query);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return 0;
    }
}
