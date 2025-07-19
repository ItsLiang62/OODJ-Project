package database;

public interface FieldValueReturner<T extends Entity> {
    String getFieldValue(T entity);
    // any Savable class object can call this interface method as in savable.getForeignKeyValue()
    // by foreignKeyValueReturner.getForeignKeyValue(savable)
    // referenced using SavableClass::instanceMethod

    // in simpler words:
    // SavableClass::instance method translates to
    // savable.getForeignKeyValue() to refer
    // foreignKeyValueReturner.getForeignKeyValue(savable)
}
