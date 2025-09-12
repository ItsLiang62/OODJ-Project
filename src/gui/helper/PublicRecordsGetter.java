package gui.helper;

import java.util.List;

@FunctionalInterface
public interface PublicRecordsGetter {
    List<List<String>> getPublicRecords();
}
