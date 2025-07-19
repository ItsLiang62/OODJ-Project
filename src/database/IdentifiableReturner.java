package database;

@FunctionalInterface
public interface IdentifiableReturner {
    Identifiable getIdentifiable(String Id);
}
