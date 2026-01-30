package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    /**
     * Books a new appointment.
     *
     * @return 1 if successful, 0 if failed
     */
    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * Updates an existing appointment.
     *
     * @param appointmentId appointment id
     * @param patientId     patient performing the update
     * @param updated       updated appointment data
     * @return success or error message
     */
    @Transactional
    public String updateAppointment(Long appointmentId, Long patientId, Appointment updated) {

        Optional<Appointment> existingOpt = appointmentRepository.findById(appointmentId);
        if (existingOpt.isEmpty()) {
            return "Appointment not found";
        }

        Appointment existing = existingOpt.get();

        if (!existing.getPatient().getId().equals(patientId)) {
            return "Unauthorized update attempt";
        }

        if (!isDoctorAvailable(
                existing.getDoctor().getId(),
                updated.getAppointmentTime()
        )) {
            return "Doctor not available at selected time";
        }

        existing.setAppointmentTime(updated.getAppointmentTime());
        existing.setStatus(updated.getStatus());

        appointmentRepository.save(existing);
        return "Appointment updated successfully";
    }

    /**
     * Cancels an appointment.
     */
    @Transactional
    public String cancelAppointment(Long appointmentId, Long patientId) {

        Optional<Appointment> existingOpt = appointmentRepository.findById(appointmentId);
        if (existingOpt.isEmpty()) {
            return "Appointment not found";
        }

        Appointment appointment = existingOpt.get();

        if (!appointment.getPatient().getId().equals(patientId)) {
            return "Unauthorized cancellation attempt";
        }

        appointmentRepository.delete(appointment);
        return "Appointment cancelled successfully";
    }

    /**
     * Retrieves appointments for a doctor on a given day,
     * optionally filtered by patient name.
     */
    @Transactional(readOnly = true)
    public List<Appointment> getAppointments(
            Long doctorId,
            LocalDate date,
            String patientName
    ) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        if (patientName == null || patientName.isBlank()) {
            return appointmentRepository
                    .findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
        }

        return appointmentRepository
                .findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                        doctorId,
                        patientName,
                        start,
                        end
                );
    }

    /**
     * Changes the status of an appointment.
     */
    @Transactional
    public void changeStatus(long appointmentId, int status) {
        appointmentRepository.updateStatus(status, appointmentId);
    }

    /**
     * Checks if a doctor is available at a given time.
     * (Simplified placeholder logic)
     */
    private boolean isDoctorAvailable(Long doctorId, LocalDateTime time) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        return doctorOpt.isPresent();
    }
}
