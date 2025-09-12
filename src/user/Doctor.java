package user;

import customExceptions.AppointmentDoesNotBelongToDoctorException;
import database.Database;
import operation.AppointmentMedicine;
import operation.Appointment;
import operation.CustomerFeedback;
import operation.Medicine;

import java.io.File;
import java.util.*;
import java.util.function.Function;

public class Doctor extends User {

    private static final Set<Doctor> doctors = new TreeSet<>(Database.getOrderedById());
    private static final File doctorFile = new File("data/doctor.txt");

    static {
        Database.populateFromRecords(Doctor::createFromDbRecord, doctorFile);
    }

    public Doctor(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public Doctor(String name, String email) {
        this(Doctor.createId(), name, email, email);
    }

    public List<List<String>> getMyAppointmentRecords() {
        return Appointment.getPublicRecords(getMyAppointments());
    }

    public List<List<String>> getMyCustomerFeedbackRecords() {
        return CustomerFeedback.getPublicRecords(getMyCustomerFeedbacks());
    }

    public List<List<String>> getMedicineRecords() {
        return Medicine.getPublicRecords(Medicine.getAll());
    }

    public List<List<String>> getMyPrescriptionRecords() {
        Set<AppointmentMedicine> myPrescriptions = new LinkedHashSet<>();
        for (Appointment appointment: getMyAppointments()) {
            myPrescriptions.addAll(AppointmentMedicine.getFiltered(AppointmentMedicine::getAppointmentId, appointment.getId()));
        }
        return AppointmentMedicine.getPublicRecords(myPrescriptions);
    }

    public void prescribeMedicine(String appointmentId, String medicineId, String targetSymptom) {
        if (isMyAppointment(appointmentId)) {
            AppointmentMedicine newAppointmentMedicine = new AppointmentMedicine(appointmentId, medicineId, targetSymptom.trim(), true);
            AppointmentMedicine.add(newAppointmentMedicine);
        } else {
            throw new AppointmentDoesNotBelongToDoctorException("Failed to prescribe medicine for appointment. Appointment does not belong to doctor");
        }
    }

    public void setConsultationFee(String appointmentId, double consultationFee) {
        if (isMyAppointment(appointmentId)) {
            Appointment appointment = Appointment.getById(appointmentId);
            appointment.setConsultationFee(consultationFee);
        } else {
            throw new AppointmentDoesNotBelongToDoctorException("Failed to set consultation fee for appointment. Appointment does not belong to doctor");
        }

    }

    public void provideFeedback(String appointmentId, String feedback) {
        if (isMyAppointment(appointmentId)) {
            Appointment appointment = Appointment.getById(appointmentId);
            appointment.setDoctorFeedback(feedback.trim());
        } else {
            throw new AppointmentDoesNotBelongToDoctorException("Failed to set feedback for customer of appointment. Appointment does not belong to doctor!");
        }
    }

    public Set<Appointment> getMyAppointments() {
        return Appointment.getFiltered(Appointment::getDoctorId, this.id);
    }

    public Set<CustomerFeedback> getMyCustomerFeedbacks() {
        return CustomerFeedback.getFiltered(CustomerFeedback::getNonManagerEmployeeId, this.id);
    }

    public boolean isMyAppointment(String appointmentId) {
        return Appointment.getFieldVals(getMyAppointments(), Appointment::getId).contains(appointmentId);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        Doctor.removeById(this.id, false);
        Doctor.add(this);
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
        Doctor.removeById(this.id, false);
        Doctor.add(this);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        Doctor.removeById(this.id, false);
        Doctor.add(this);
    }

    public static void createFromDbRecord(List<String> record) {
        String doctorId = record.getFirst();
        String doctorName = record.get(1);
        String doctorEmail = record.get(2);
        String doctorPassword = record.getLast();

        Doctor doctor = new Doctor(doctorId, doctorName, doctorEmail, doctorPassword);
        Doctor.add(doctor);
    }

    public static String createId() {
        return Database.createId(doctors, 'D');
    }

    public static void add(Doctor newDoctor) {
        Database.add(newDoctor, doctors, doctorFile);
    }

    public static Set<Doctor> getAll() {
        return new LinkedHashSet<>(doctors);
    }

    public static Doctor getById(String doctorId) {
        return Database.getById(doctors, doctorId, "doctor");
    }

    public static <R> Set<Doctor> getFiltered(
            Function<Doctor, R> fieldValReturner,
            R fieldVal) {
        return Database.getFiltered(doctors, fieldValReturner, fieldVal);
    }

    public static <R> List<R> getFieldVals(Set<Doctor> doctors, Function<Doctor, R> fieldValReturner) {
        return Database.getFieldVals(doctors, fieldValReturner);
    }

    public static List<List<String>> getPublicRecords(Set<Doctor> doctors) {
        return Database.getPublicRecords(doctors);
    }

    public static void removeById(String id, boolean removeDependencies) {
        Database.removeById(doctors, id, doctorFile);

        if (removeDependencies) {
            for (CustomerFeedback customerFeedback: CustomerFeedback.getAll()) {
                if (customerFeedback.getNonManagerEmployeeId().equals(id)) {
                    CustomerFeedback.removeById(customerFeedback.getId());
                }
            }
            for (Appointment appointment: Appointment.getAll()) {
                if (appointment.getDoctorId().equals(id)) {
                    CustomerFeedback.removeById(appointment.getId());
                }
            }
        }
    }
}
