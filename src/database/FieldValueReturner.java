package database;

public interface FieldValueReturner<T extends Entity> {
    String getFieldValue(T entity);
}
