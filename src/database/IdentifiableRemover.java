package database;

public interface IdentifiableRemover {
    void removeIdentifiable(String id, boolean removeAllDependencies);
}
