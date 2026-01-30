package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final AppointmentService appointmentService;
    private final TokenService service;

    public PrescriptionController(
            PrescriptionService prescriptionService,
            AppointmentService appointmentService,
            TokenService service
    ) {
        this.prescriptionService = prescriptionService;
        this.appointmentService = appointmentService;
        this.service = service;
    }

    /* ---------------------------------------------------
     * Save Prescription (Doctor)
     * --------------------------------------------------- */

    @PostMapping
    public ResponseEntity<?> savePrescription(
            @Valid @RequestBody Prescription prescription,
            @RequestHeader("Authorization") String token
    ) {
        if (!service.validateToken(token, "DOCTOR")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        // Update appointment status (e.g., PRESCRIBED = 2)
        appointmentService.changeStatus(
                prescription.getAppointmentId(),
                2
        );

        return prescriptionService.savePrescription(prescription);
    }

    /* ---------------------------------------------------
     * Get Prescription (Doctor)
     * --------------------------------------------------- */

    @GetMapping("/{appointmentId}")
    public ResponseEntity<?> getPrescription(
            @PathVariable Long appointmentId,
            @RequestHeader("Authorization") String token
    ) {
        if (!service.validateToken(token, "DOCTOR")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        return prescriptionService.getPrescription(appointmentId);
    }
}
