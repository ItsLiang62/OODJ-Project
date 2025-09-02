package database;

import customExceptions.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import java.io.FileWriter;
import java.util.function.Function;

public final class Database {

    public static Comparator<Identifiable> getOrderedById() {
        return Comparator.comparingInt(identifiable -> Integer.parseInt(identifiable.getId().substring(1)));
    }

    public static <T extends Entity> void add(T newEntity, Set<T> entitySet, File entityFile) {
        entitySet.add(newEntity);
        try {
            try (FileWriter fileWriter = new FileWriter(entityFile)) {
                List<String> entityRecords = new ArrayList<>();
                for (T entity: entitySet) {
                    String dbEntityRecord = String.join(",", entity.createDbRecord());
                    entityRecords.add(dbEntityRecord);
                }
                String dbEntityRecords = String.join("\n", entityRecords);
                fileWriter.write(dbEntityRecords);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Get

    public static <T extends Identifiable> T getById(
            Set<T> identifiableSet,
            String id,
            String identifiableDescription
    ) {
        for (T identifiable: identifiableSet) {
            if (identifiable.getId().equals(id)) {
                return identifiable;
            }
        }
        throw new IdNotFoundException(String.format(
                "Could not find %s with the given ID!", identifiableDescription
        ));
    }

    public static <T extends Entity, R> Set<T> getFiltered(
            Set<T> entitySet,
            Function<T, R> fieldValReturner,
            R fieldVal) {
        Set<T> filteredEntitySet = new LinkedHashSet<>();
        for (T entity: entitySet) {
            if (fieldValReturner.apply(entity).equals(fieldVal)) {
                filteredEntitySet.add(entity);
            }
        }
        return filteredEntitySet;
    }

    public static <T extends Entity> List<List<String>> getPublicRecords(Set<T> entitySet) {
        List<List<String>> publicRecords = new ArrayList<>();
        for (T entity: entitySet) {
            publicRecords.add(entity.getPublicRecord());
        }
        return publicRecords;
    }

    public static <T extends Entity, R> List<R> getFieldVals(Set<T> entitySet, Function<T, R> fieldValReturner) {
        List<R> fieldVals = new ArrayList<>();
        for (T entity: entitySet) {
            fieldVals.add(fieldValReturner.apply(entity));
        }
        return fieldVals;
    }

    // Remove

    public static <T extends Identifiable> void removeById(
            Set<T> identifiableSet, String id, File identifiableFile
    ) {
        identifiableSet.removeIf(identifiable -> identifiable.getId().equals(id));
        Database.saveRecords(identifiableSet, identifiableFile);
    }

    public static <T extends Identifiable> String createId(Set<T> identifiableSet, char prefix) {
        Queue<String> allIdentifiableId = new PriorityQueue<>(Comparator.comparingInt(
                (String identifiableId) -> Integer.parseInt(identifiableId.substring(1))
        ).reversed());

        allIdentifiableId.addAll(getFieldVals(identifiableSet, Identifiable::getId));
        String newestIdentifiableId = allIdentifiableId.peek();

        if (newestIdentifiableId == null) {
            return String.format("%s%03d", prefix, 1);
        }
        return String.format("%s%03d", prefix, Integer.parseInt(newestIdentifiableId.substring(1)) + 1);
    }

    public static void populateFromRecords(InstantiatableFromRecord instantiatableFromRecord, File inputFile) {
        try {
            try (Scanner fileScanner = new Scanner(inputFile)) {
                while (fileScanner.hasNextLine()) {
                    List<String> record = new ArrayList<>(
                            Arrays.asList(fileScanner.nextLine().split(","))
                    );
                    instantiatableFromRecord.createInstanceFromRecord(record);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T extends Entity> void saveRecords(Set<T> entitySet, File outputFile) {
        try {
            try (FileWriter fileWriter = new FileWriter(outputFile)) {
                List<String> entityRecords = new ArrayList<>();
                for (Entity entity: entitySet) {
                    String dbEntityRecord = String.join(",", entity.createDbRecord());
                    entityRecords.add(dbEntityRecord);
                }
                String dbEntityRecords = String.join("\n", entityRecords);

                fileWriter.write(dbEntityRecords);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void createFile(File file) {
        try {
            File dataFolder = new File("data");
            if (!dataFolder.exists()) {
                dataFolder.mkdirs(); // Create the data folder if it doesn't exist
            }
            if (!file.exists()) {
                file.createNewFile(); // Create the text file in the data folder if it doesn't exist
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}