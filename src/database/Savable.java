package database;

import java.util.List;

public interface Savable<T>{
    List<String> createRecord();
    T createInstanceFromRecord(List<String> record);
}
