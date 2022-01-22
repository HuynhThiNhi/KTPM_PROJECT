package dml;

import java.util.List;

public interface IQuery {
    <T> List<T> executeWithoutRelationship(Class<T> cls);
    <T> List<T> execute(Class<T> cls);
    <T> int executeUpdate();
}
