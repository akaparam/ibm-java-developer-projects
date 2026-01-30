package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.DTO.Login;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
import com.project.back_end.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final TokenService service;

    @Autowired
    private Service filterService;

    public DoctorController(
            DoctorService doctorService,
            TokenService service
    ) {
        this.doctorService = doctorService;
        this.service = service;
    }

    /* ---------------------------------------------------
     * Doctor Availability
     * --------------------------------------------------- */

    @GetMapping("/availability/{doctorId}/{date}")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable Long doctorId,
            @PathVariable LocalDate date,
            @RequestHeader("Authorization") String token,
            @RequestParam String user
    ) {
        if (!service.validateToken(token, user.toUpperCase())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        List<LocalTime> availability =
                doctorService.getDoctorAvailability(doctorId, date);

        return ResponseEntity.ok(
                Map.of("availableTimes", availability)
        );
    }

    /* ---------------------------------------------------
     * Get All Doctors
     * --------------------------------------------------- */

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctor() {
        return ResponseEntity.ok(
                Map.of("doctors", doctorService.getDoctors())
        );
    }

    /* ---------------------------------------------------
     * Register Doctor (Admin)
     * --------------------------------------------------- */

    @PostMapping
    public ResponseEntity<Map<String, Object>> saveDoctor(
            @Valid @RequestBody Doctor doctor,
            @RequestHeader("Authorization") String token
    ) {
        if (!service.validateToken(token, "ADMIN")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Unauthorized"));
        }

        int result = doctorService.saveDoctor(doctor);

        if (result == -1) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Doctor already exists"));
        }

        if (result == 1) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("message", "Doctor registered successfully"));
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Failed to register doctor"));
    }

    /* ---------------------------------------------------
     * Doctor Login
     * --------------------------------------------------- */

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> doctorLogin(
            @Valid @RequestBody Login login
    ) {
        String token =
                doctorService.validateDoctor(
                        login.getEmail(),
                        login.getPassword()
                );

        if ("Invalid credentials".equals(token)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", token));
        }

        return ResponseEntity.ok(
                Map.of("token", token)
        );
    }

    /* ---------------------------------------------------
     * Update Doctor (Admin)
     * --------------------------------------------------- */

    @PutMapping("/{doctorId}")
    public ResponseEntity<Map<String, Object>> updateDoctor(
            @PathVariable Long doctorId,
            @Valid @RequestBody Doctor doctor,
            @RequestHeader("Authorization") String token
    ) {
        if (!service.validateToken(token, "ADMIN")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Unauthorized"));
        }

        int result = doctorService.updateDoctor(doctorId, doctor);

        if (result == -1) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Doctor not found"));
        }

        return ResponseEntity.ok(
                Map.of("message", "Doctor updated successfully")
        );
    }

    /* ---------------------------------------------------
     * Delete Doctor (Admin)
     * --------------------------------------------------- */

    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Map<String, Object>> deleteDoctor(
            @PathVariable Long doctorId,
            @RequestHeader("Authorization") String token
    ) {
        if (!service.validateToken(token, "ADMIN")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Unauthorized"));
        }

        int result = doctorService.deleteDoctor(doctorId);

        if (result == -1) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Doctor not found"));
        }

        if (result == 1) {
            return ResponseEntity.ok(
                    Map.of("message", "Doctor deleted successfully")
            );
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Failed to delete doctor"));
    }

    /* ---------------------------------------------------
     * Filter Doctors
     * --------------------------------------------------- */

    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> filterDoctors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String time,
            @RequestParam(required = false) String speciality
    ) {
        return ResponseEntity.ok(
                Map.of(
                        "doctors",
                        filterService.filterDoctor(name, speciality, time)
                )
        );
    }
}
