package com.project.back_end.services;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public Service(
            TokenService tokenService,
            AdminRepository adminRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            DoctorService doctorService,
            PatientService patientService
    ) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    /* ---------------------------------------------------
     * Token Validation
     * --------------------------------------------------- */

    public ResponseEntity<String> validateToken(String token, String role) {
        try {
            if (!tokenService.validateToken(token, role)) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid or expired token");
            }
            return ResponseEntity.ok("Token is valid");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Token validation failed");
        }
    }

    /* ---------------------------------------------------
     * Admin Login
     * --------------------------------------------------- */

    public ResponseEntity<String> validateAdmin(String username, String password) {
        try {
            Admin admin = adminRepository.findByUsername(username);

            if (admin == null || !admin.getPassword().equals(password)) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid admin credentials");
            }

            String token = tokenService.generateToken(username, "ADMIN");
            return ResponseEntity.ok(token);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Admin authentication failed");
        }
    }

    /* ---------------------------------------------------
     * Doctor Filtering
     * --------------------------------------------------- */

    public List<Doctor> filterDoctor(
            String name,
            String specialty,
            String period
    ) {
        if (name != null && specialty != null && period != null) {
            return doctorService.filterDoctorsByNameSpecilityandTime(
                    name, specialty, period
            );
        }

        if (name != null && specialty != null) {
            return doctorService.filterDoctorByNameAndSpecility(name, specialty);
        }

        if (name != null && period != null) {
            return doctorService.filterDoctorByNameAndTime(name, period);
        }

        if (specialty != null && period != null) {
            return doctorService.filterDoctorByTimeAndSpecility(specialty, period);
        }

        if (name != null) {
            return doctorService.findDoctorByName(name);
        }

        if (specialty != null) {
            return doctorService.filterDoctorBySpecility(specialty);
        }

        if (period != null) {
            return doctorService.filterDoctorsByTime(period);
        }

        return doctorService.getDoctors();
    }

    /* ---------------------------------------------------
     * Appointment Validation
     * --------------------------------------------------- */

    public int validateAppointment(
            Long doctorId,
            LocalDate date,
            LocalTime requestedTime
    ) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) {
            return -1;
        }

        List<LocalTime> availableTimes =
                doctorService.getDoctorAvailability(doctorId, date);

        return availableTimes.contains(requestedTime) ? 1 : 0;
    }

    /* ---------------------------------------------------
     * Patient Validation
     * --------------------------------------------------- */

    public boolean validatePatient(Patient patient) {
        return patientRepository
                .findByEmailOrPhone(
                        patient.getEmail(),
                        patient.getPhone()
                ) == null;
    }

    /* ---------------------------------------------------
     * Patient Login
     * --------------------------------------------------- */

    public ResponseEntity<String> validatePatientLogin(
            String email,
            String password
    ) {
        try {
            Patient patient = patientRepository.findByEmail(email);

            if (patient == null || !patient.getPassword().equals(password)) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid patient credentials");
            }

            String token =
                    tokenService.generateToken(patient.getEmail(), "PATIENT");

            return ResponseEntity.ok(token);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Patient authentication failed");
        }
    }

    /* ---------------------------------------------------
     * Patient Appointment Filtering
     * --------------------------------------------------- */

    public Object filterPatient(
            String token,
            String condition,
            String doctorName
    ) {
        String email = tokenService.extractEmail(token);
        Patient patient = patientRepository.findByEmail(email);

        if (patient == null) {
            throw new IllegalArgumentException("Invalid token");
        }

        if (condition != null && doctorName != null) {
            return patientService.filterByDoctorAndCondition(
                    patient.getId(), doctorName, condition
            );
        }

        if (condition != null) {
            return patientService.filterByCondition(
                    patient.getId(), condition
            );
        }

        if (doctorName != null) {
            return patientService.filterByDoctor(
                    patient.getId(), doctorName
            );
        }

        return patientService.getPatientAppointment(patient.getId());
    }
}
