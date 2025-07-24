package user;

import customExceptions.AppointmentDoesNotBelongToDoctorException;
import database.Database;
import database.IdCreator;
import operation.AppointmentMedicine;
import operation.Appointment;

import java.util.*;

public class Doctor extends User {

    public Doctor(String id, String name, String email, String password) {
        super(id, name, email, password);;
    }

    public Doctor(String name, String email) {
        this(IdCreator.createId('D'), name, email, email);
    }

    public Set<List<String>> getAllMyAppointmentRecords() {
        Set<List<String>> allMyAppointmentRecords = new LinkedHashSet<>();
        for (String appointmentId: Database.getAllAppointmentIdOfDoctor(this.id)) {
            allMyAppointmentRecords.add(Database.getAppointment(appointmentId).createPublicRecord());
        }
        return allMyAppointmentRecords;
    }

    public Set<List<String>> getAllMyCustomerFeedbackRecords() {
        Set<List<String>> allMyCustomerFeedbackRecords = new LinkedHashSet<>();
        for (String customerFeedbackId: Database.getAllCustomerFeedbackIdOfNonManagerEmployee(this.getId())) {
            allMyCustomerFeedbackRecords.add(Database.getCustomerFeedback(customerFeedbackId).createPublicRecord());
        }
        return allMyCustomerFeedbackRecords;
    }

    public Set<List<String>> getAllMedicineRecords() {
        Set<List<String>> allMedicineRecords = new LinkedHashSet<>();
        for (String medicineId: Database.getAllMedicineId()){
            allMedicineRecords.add(Database.getMedicine(medicineId).createPublicRecord());
        }
        return allMedicineRecords;
    }

    public Set<List<String>> getAllMyPrescriptionRecords() {
        Set<List<String>> allMyPrescriptionRecords = new LinkedHashSet<>();
        for (List<String> prescriptionInfo: Database.getAllPrescriptionInfoOfDoctor(this.id)) {
            allMyPrescriptionRecords.add(Database.getAppointmentMedicine(prescriptionInfo.getFirst(), prescriptionInfo.getLast()).createPublicRecord());
        }
        return allMyPrescriptionRecords;
    }

    public void prescribeMedicine(String appointmentId, String medicineId, String targetSymptom) {
        if (!Database.getAllAppointmentIdOfDoctor(this.id).contains(appointmentId)) {
            throw new AppointmentDoesNotBelongToDoctorException("Failed to prescribe medicine for appointment. Appointment does not belong to doctor");
        }
        AppointmentMedicine newAppointmentMedicine = new AppointmentMedicine(appointmentId, medicineId, targetSymptom.trim(), true);
        Database.addAppointmentMedicine(newAppointmentMedicine);
    }

    public void setConsultationFee(String appointmentId, double consultationFee) {
        if (!Database.getAllAppointmentIdOfDoctor(this.id).contains(appointmentId)) {
            throw new AppointmentDoesNotBelongToDoctorException("Failed to set consultation fee for appointment. Appointment does not belong to doctor");
        }
        Appointment appointment = Database.getAppointment(appointmentId);
        appointment.setConsultationFee(consultationFee);
    }

    public void provideFeedback(String appointmentId, String feedback) {
        if (!Database.getAllAppointmentIdOfDoctor(this.id).contains(appointmentId)) {
            throw new AppointmentDoesNotBelongToDoctorException("Failed to set feedback for customer of appointment. Appointment does not belong to doctor!");
        }
        Appointment appointment = Database.getAppointment(appointmentId);
        appointment.setDoctorFeedback(feedback.trim());
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        Database.removeDoctor(this.id, false);
        Database.addDoctor(this);
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
        Database.removeDoctor(this.id, false);
        Database.addDoctor(this);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        Database.removeDoctor(this.id, false);
        Database.addDoctor(this);
    }

    public static void createDoctorFromRecord(List<String> record) {
        String doctorId = record.getFirst();
        String doctorName = record.get(1);
        String doctorEmail = record.get(2);
        String doctorPassword = record.getLast();

        Doctor doctor = new Doctor(doctorId, doctorName, doctorEmail, doctorPassword);
        Database.addDoctor(doctor);
    }
}
