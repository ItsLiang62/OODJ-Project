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

    public List<List<String>> getAllMyAppointmentRecords() { return Database.getAllAppointmentPublicRecordsOfDoctor(id); }
    public List<List<String>> getAllMyCustomerFeedbackRecords() { return Database.getAllCustomerFeedbackPublicRecordsOfNonManagerEmployee(id); }
    public List<List<String>> getAllMedicineRecords() { return Database.getAllMedicinePublicRecords(); }
    public List<List<String>> getAllMyPrescriptionRecords() { return Database.getAllAppointmentMedicinePublicRecordsOfDoctor(id); }

    public void prescribeMedicine(String appointmentId, String medicineId, String targetSymptom) {
        if (!Database.getAllAppointmentIdOfDoctor(id).contains(appointmentId)) {
            throw new AppointmentDoesNotBelongToDoctorException("Failed to prescribe medicine for appointment. Appointment does not belong to doctor");
        }
        AppointmentMedicine newAppointmentMedicine = new AppointmentMedicine(appointmentId, medicineId, targetSymptom.trim(), true);
        Database.addAppointmentMedicine(newAppointmentMedicine);
    }

    public void setConsultationFee(String appointmentId, double consultationFee) {
        if (!Database.getAllAppointmentIdOfDoctor(id).contains(appointmentId)) {
            throw new AppointmentDoesNotBelongToDoctorException("Failed to set consultation fee for appointment. Appointment does not belong to doctor");
        }
        Appointment appointment = Database.getAppointment(appointmentId);
        appointment.setConsultationFee(consultationFee);
    }

    public void provideFeedback(String appointmentId, String feedback) {
        if (!Database.getAllAppointmentIdOfDoctor(id).contains(appointmentId)) {
            throw new AppointmentDoesNotBelongToDoctorException("Failed to set feedback for customer of appointment. Appointment does not belong to doctor!");
        }
        Appointment appointment = Database.getAppointment(appointmentId);
        appointment.setDoctorFeedback(feedback.trim());
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        Database.removeDoctor(id, false);
        Database.addDoctor(this);
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
        Database.removeDoctor(id, false);
        Database.addDoctor(this);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        Database.removeDoctor(id, false);
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
