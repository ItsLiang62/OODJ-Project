package database;

import java.util.List;

@FunctionalInterface
public interface InstantiatableFromRecord<T> {
    T createInstanceFromRecord(List<String> record);
}
