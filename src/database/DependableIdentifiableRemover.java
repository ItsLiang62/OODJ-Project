package database;

public interface DependableIdentifiableRemover {
    void removeDependableIdentifiable(String id, boolean removeAllDependencies);
}
