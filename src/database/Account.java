package database;


import java.util.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class Account {
    public static Map<String, String> emailPassword;
    private static Map<String, String> emailUserId;

    private static File accountFile = new File("data/account.txt");

    static {
        emailPassword = new LinkedHashMap<>();
        emailUserId = new LinkedHashMap<>();
        try {
            Account.populate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPassword(String email) {
        return emailPassword.get(email);
    }

    public static String getUserId(String email) {
        return emailUserId.get(email);
    }

    static void addAccount(String email, String password, String userId) {
        emailPassword.put(email, password);
        emailUserId.put(email, userId);
    }

    private static void populate() throws IOException {
        try (Scanner accountFileScanner = new Scanner(accountFile)) {
            while (accountFileScanner.hasNextLine()) {
                List<String> accountData = new ArrayList<>(
                        Arrays.asList(accountFileScanner.nextLine().split(","))
                );
                emailPassword.put(accountData.getFirst(), accountData.get(1));
                emailUserId.put(accountData.getFirst(), accountData.get(2));
            }
        }
    }

    public static void save() throws IOException {
        try (FileWriter accountFileWriter = new FileWriter(accountFile)) {
            List<String> accountRecords = new ArrayList<>();
            for (String email: emailPassword.keySet()) {
                List<String> accountData = new ArrayList<>(Arrays.asList(
                        email, emailPassword.get(email), emailUserId.get(email)
                ));
                String csAccountData = String.join(",", accountData);
                accountRecords.add(csAccountData);
            }
            String nsAccountRecords = String.join("\n", accountRecords);
            accountFileWriter.write(nsAccountRecords);
        }
    }
}
