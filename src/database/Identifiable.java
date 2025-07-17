package database;

import java.util.*;
import java.util.stream.Collectors;

public interface Identifiable {
    static String createId(char prefix) {
        Queue<String> allSamePrefixId = new PriorityQueue<>(Comparator.comparingInt(
                (String userTypeId) -> Integer.parseInt(userTypeId.substring(1))
        ).reversed());

        List<String> allId = new ArrayList<>();
        allId.addAll(Database.getAllManagerId());
        allId.addAll(Database.getAllStaffId());
        allId.addAll(Database.getAllDoctorId());
        allId.addAll(Database.getAllCustomerId());
        allId.addAll(Database.getAllAppointmentId());
        allId.addAll(Database.getAllCustomerFeedbackId());
        allId.addAll(Database.getAllMedicineId());
        allId.addAll(Database.getAllInvoiceId());

        allSamePrefixId.addAll(allId.stream().filter(
                id -> id.charAt(0) == prefix
        ).collect(Collectors.toSet()));

        String newestSamePrefixId = allSamePrefixId.peek();
        if (newestSamePrefixId == null) {
            return String.format("%s%03d", prefix, 1);
        }
        return String.format("%s%03d", prefix, Integer.parseInt(newestSamePrefixId.substring(1)) + 1);
    }
}
