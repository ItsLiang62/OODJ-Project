package database;

import java.util.List;

@FunctionalInterface
public interface InstantiatableFromRecord {
    void createInstanceFromRecord(List<String> record);
}
