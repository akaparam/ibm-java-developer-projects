package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.services.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public PatientService(
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService
    ) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    /* ---------------------------------------------------
     * Create Patient
     * --------------------------------------------------- */

    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            // log.error("Failed to create patient", e);
            return 0;
        }
    }

    /* ---------------------------------------------------
     * Appointments
     * --------------------------------------------------- */

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getPatientAppointment(Long patientId) {
        try {
            return appointmentRepository.findByPatient_Id(patientId)
                    .stream()
                    .map(this::toDTO)
                    .toList();
        } catch (Exception e) {
            // log.error("Failed to fetch appointments", e);
            throw new RuntimeException("Unable to fetch appointments");
        }
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> filterByCondition(Long patientId, String condition) {

        int status;
        if ("future".equalsIgnoreCase(condition)) {
            status = 0;
        } else if ("past".equalsIgnoreCase(condition)) {
            status = 1;
        } else {
            throw new IllegalArgumentException("Invalid condition: " + condition);
        }

        return appointmentRepository
                .findByPatient_IdAndStatusOrderByAppointmentTimeAsc(patientId, status)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> filterByDoctor(Long patientId, String doctorName) {

        return appointmentRepository
                .filterByDoctorNameAndPatientId(doctorName, patientId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> filterByDoctorAndCondition(
            Long patientId,
            String doctorName,
            String condition
    ) {
        int status;
        if ("future".equalsIgnoreCase(condition)) {
            status = 0;
        } else if ("past".equalsIgnoreCase(condition)) {
            status = 1;
        } else {
            throw new IllegalArgumentException("Invalid condition: " + condition);
        }

        return appointmentRepository
                .filterByDoctorNameAndPatientIdAndStatus(doctorName, patientId, status)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /* ---------------------------------------------------
     * Patient Details
     * --------------------------------------------------- */

    @Transactional(readOnly = true)
    public Patient getPatientDetails(String token) {

        String email = tokenService.extractEmail(token);

        Optional<Patient> patientOpt = Optional.ofNullable(
                patientRepository.findByEmail(email)
        );

        return patientOpt.orElseThrow(
                () -> new IllegalArgumentException("Patient not found")
        );
    }

    /* ---------------------------------------------------
     * DTO Mapper
     * --------------------------------------------------- */

    private AppointmentDTO toDTO(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getDoctor().getId(),
                appointment.getDoctor().getName(),
                appointment.getPatient().getId(),
                appointment.getPatient().getName(),
                appointment.getPatient().getEmail(),
                appointment.getPatient().getPhone(),
                appointment.getPatient().getAddress(),
                appointment.getAppointmentTime(),
                appointment.getStatus()
        );
    }
}
