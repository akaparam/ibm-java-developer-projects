package com.project.back_end.controllers;

import com.project.back_end.models.Patient;
import com.project.back_end.DTO.Login;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;
import com.project.back_end.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final TokenService service;

    @Autowired
    private Service filterService;

    public PatientController(
            PatientService patientService,
            TokenService service
    ) {
        this.patientService = patientService;
        this.service = service;
    }

    /* ---------------------------------------------------
     * Get Patient Details
     * --------------------------------------------------- */

    @GetMapping
    public ResponseEntity<?> getPatient(
            @RequestHeader("Authorization") String token
    ) {
        if (!service.validateToken(token, "PATIENT")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        return ResponseEntity.ok(
                patientService.getPatientDetails(token)
        );
    }

    /* ---------------------------------------------------
     * Register Patient
     * --------------------------------------------------- */

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPatient(
            @Valid @RequestBody Patient patient
    ) {
        if (!filterService.validatePatient(patient)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Patient already exists"));
        }

        int result = patientService.createPatient(patient);

        if (result == 1) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("message", "Patient registered successfully"));
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Failed to register patient"));
    }

    /* ---------------------------------------------------
     * Patient Login
     * --------------------------------------------------- */

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody Login login
    ) {
        ResponseEntity<String> response =
                filterService.validatePatientLogin(
                        login.getEmail(),
                        login.getPassword()
                );

        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(
                    Map.of("token", response.getBody())
            );
        }

        return ResponseEntity
                .status(response.getStatusCode())
                .body(
                        Map.of("message", response.getBody())
                );
    }

    /* ---------------------------------------------------
     * Get Patient Appointments
     * --------------------------------------------------- */

    @GetMapping("/appointments")
    public ResponseEntity<?> getPatientAppointment(
            @RequestHeader("Authorization") String token
    ) {
        if (!service.validateToken(token, "PATIENT")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        return ResponseEntity.ok(
                filterService.filterPatient(token, null, null)
        );
    }

    /* ---------------------------------------------------
     * Filter Patient Appointments
     * --------------------------------------------------- */

    @GetMapping("/appointments/filter")
    public ResponseEntity<?> filterPatientAppointment(
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String name,
            @RequestHeader("Authorization") String token
    ) {
        if (!service.validateToken(token, "PATIENT")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        return ResponseEntity.ok(
                filterService.filterPatient(token, condition, name)
        );
    }
}
