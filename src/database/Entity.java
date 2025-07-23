package database;

import java.util.List;

public interface Entity {
    List<String> createDbRecord();
    List<String> createPublicRecord();
}