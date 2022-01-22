package dml;

import java.util.List;

public interface IQueryBuilder <T>{
    IQueryBuilder<T> where(String condition);
    IQueryBuilder<T> groupBy(String columnNames);
    IQueryBuilder<T> having(String condition);
    List<T> getAll();
    List<T> run();
}
