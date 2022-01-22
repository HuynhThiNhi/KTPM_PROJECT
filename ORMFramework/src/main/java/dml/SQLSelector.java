package dml;

import java.sql.Connection;
import java.util.List;

public class SQLSelector<T> extends SQLQuery implements IQueryBuilder<T> {
    private String whereQueryString = "", groupByQueryString = "", havingQueryString = "";
    private final Class<T> cls;

    public SQLSelector(Connection conn, String connectionString, Class<T> cls) {
        super(conn, "", connectionString);

        SQLMapper mapper = new SQLMapper();
        query += "SELECT";

        for (String column: mapper.getColumns(cls)) {
            query = String.format("%s %s,", query, column);
        }
        // remove the latter comma
        query = query.substring(0, query.length() -1);

        query = String.format("%s FROM %s", query, mapper.getTableName(cls));
        this.cls = cls;
    }

    @Override
    public IQueryBuilder<T> where(String condition) {
        whereQueryString = condition;
        return this;
    }

    @Override
    public IQueryBuilder<T> having(String condition) {
        havingQueryString = condition;
        return this;
    }

    @Override
    public IQueryBuilder<T> groupBy(String columnNames) {
        groupByQueryString = columnNames;
        return this;
    }

    @Override
    public List<T> getAll() {
        return this.execute(cls);
    }

    @Override
    public List<T> run() {
        if (!whereQueryString.isEmpty())
            query = String.format("%s WHERE %s", query, whereQueryString);
        if (!groupByQueryString.isEmpty())
            query = String.format("%s GROUP BY %s", query, groupByQueryString);
        if (!havingQueryString.isEmpty())
            query = String.format("%s HAVING %s", query, havingQueryString);
        return this.execute(cls);
    }
}
