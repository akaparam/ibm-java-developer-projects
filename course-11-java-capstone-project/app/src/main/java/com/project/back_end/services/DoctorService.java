package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.services.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");

    public DoctorService(
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService
    ) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    /* ---------------------------------------------------
     * Availability
     * --------------------------------------------------- */

    @Transactional(readOnly = true)
    public List<LocalTime> getDoctorAvailability(Long doctorId, LocalDate date) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<Appointment> booked =
                appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                        doctorId, start, end
                );

        Set<LocalTime> bookedTimes = booked.stream()
                .map(a -> a.getAppointmentTime().toLocalTime())
                .collect(Collectors.toSet());

        List<LocalTime> availableTimes =
                toLocalTimes(doctor.getAvailableTimes());

        return availableTimes.stream()
                .filter(t -> !bookedTimes.contains(t))
                .sorted()
                .toList();
    }


    private List<LocalTime> toLocalTimes(List<String> times) {
        return times.stream()
                .map(t -> LocalTime.parse(t, TIME_FORMATTER))
                .toList();
    }

    /* ---------------------------------------------------
     * Save / Update / Delete
     * --------------------------------------------------- */

    @Transactional
    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
                return -1; // conflict
            }
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public int updateDoctor(Long doctorId, Doctor updated) {
        Optional<Doctor> existingOpt = doctorRepository.findById(doctorId);
        if (existingOpt.isEmpty()) {
            return -1;
        }

        Doctor existing = existingOpt.get();
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setSpecialty(updated.getSpecialty());
        existing.setAvailableTimes(updated.getAvailableTimes());

        doctorRepository.save(existing);
        return 1;
    }

    @Transactional
    public int deleteDoctor(Long doctorId) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return -1;
        }

        try {
            appointmentRepository.deleteAllByDoctorId(doctorId);
            doctorRepository.deleteById(doctorId);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /* ---------------------------------------------------
     * Authentication
     * --------------------------------------------------- */

    @Transactional(readOnly = true)
    public String validateDoctor(String email, String password) {

        Doctor doctor = doctorRepository.findByEmail(email);
        if (doctor == null) {
            return "Invalid credentials";
        }

        if (!doctor.getPassword().equals(password)) {
            return "Invalid credentials";
        }

        return tokenService.generateToken(doctor.getId().toString(), "DOCTOR");
    }

    /* ---------------------------------------------------
     * Fetch / Search
     * --------------------------------------------------- */

    @Transactional(readOnly = true)
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Doctor> findDoctorByName(String name) {
        return doctorRepository.findByNameLike("%" + name + "%");
    }

    /* ---------------------------------------------------
     * Filtering helpers
     * --------------------------------------------------- */

    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorsByNameSpecilityandTime(
            String name,
            String specialty,
            String period
    ) {
        List<Doctor> doctors =
                doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                        name, specialty
                );
        return filterDoctorByTime(doctors, period);
    }

    public List<Doctor> filterDoctorByTime(List<Doctor> doctors, String period) {
        return doctors.stream()
                .filter(d -> matchesPeriod(
                        toLocalTimeSet(d.getAvailableTimes()),
                        period
                ))
                .toList();
    }

    private Set<LocalTime> toLocalTimeSet(List<String> times) {
        return times.stream()
                .map(t -> LocalTime.parse(t, TIME_FORMATTER))
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorByNameAndTime(String name, String period) {
        List<Doctor> doctors = doctorRepository.findByNameLike("%" + name + "%");
        return filterDoctorByTime(doctors, period);
    }

    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorByNameAndSpecility(String name, String specialty) {
        return doctorRepository
                .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
    }

    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorByTimeAndSpecility(String specialty, String period) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return filterDoctorByTime(doctors, period);
    }

    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorBySpecility(String specialty) {
        return doctorRepository.findBySpecialtyIgnoreCase(specialty);
    }

    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorsByTime(String period) {
        return filterDoctorByTime(doctorRepository.findAll(), period);
    }

    /* ---------------------------------------------------
     * Utility
     * --------------------------------------------------- */

    private boolean matchesPeriod(Set<LocalTime> times, String period) {
        return times.stream().anyMatch(t ->
                "AM".equalsIgnoreCase(period)
                        ? t.isBefore(LocalTime.NOON)
                        : t.equals(LocalTime.NOON) || t.isAfter(LocalTime.NOON)
        );
    }
}
