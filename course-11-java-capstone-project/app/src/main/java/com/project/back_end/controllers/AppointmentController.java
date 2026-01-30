package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final TokenService service;

    public AppointmentController(
            AppointmentService appointmentService,
            TokenService service
    ) {
        this.appointmentService = appointmentService;
        this.service = service;
    }

    /* ---------------------------------------------------
     * Get Appointments (Doctor)
     * --------------------------------------------------- */

    @GetMapping("/{date}")
    public ResponseEntity<?> getAppointments(
            @PathVariable LocalDate date,
            @RequestParam(required = false) String patientName,
            @RequestHeader("Authorization") String token
    ) {
        if (!service.validateToken(token, "DOCTOR")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        List<Appointment> appointments =
                appointmentService.getAppointments(
                        Long.parseLong(service.extractEmail(token)), // or pass doctorId directly if you already have it
                        date,
                        patientName
                );

        return ResponseEntity.ok(appointments);
    }

    /* ---------------------------------------------------
     * Book Appointment (Patient)
     * --------------------------------------------------- */

    @PostMapping
    public ResponseEntity<?> bookAppointment(
            @Valid @RequestBody Appointment appointment,
            @RequestHeader("Authorization") String token
    ) {
        if (!service.validateToken(token, "PATIENT")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        int result = appointmentService.bookAppointment(appointment);

        if (result == 1) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("message", "Appointment booked successfully"));
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Failed to book appointment"));
    }

    /* ---------------------------------------------------
     * Update Appointment (Patient)
     * --------------------------------------------------- */

    @PutMapping("/{appointmentId}")
    public ResponseEntity<?> updateAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody Appointment appointment,
            @RequestHeader("Authorization") String token
    ) {
        if (!service.validateToken(token, "PATIENT")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        String result =
                appointmentService.updateAppointment(
                        appointmentId,
                        appointment.getPatient().getId(),
                        appointment
                );

        if ("Appointment updated successfully".equals(result)) {
            return ResponseEntity.ok(Map.of("message", result));
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", result));
    }

    /* ---------------------------------------------------
     * Cancel Appointment (Patient)
     * --------------------------------------------------- */

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(
            @PathVariable Long appointmentId,
            @RequestHeader("Authorization") String token
    ) {
        if (!service.validateToken(token, "PATIENT")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        Long patientId = Long.parseLong(service.extractEmail(token)); // or resolve via email → patient

        String result =
                appointmentService.cancelAppointment(
                        appointmentId,
                        patientId
                );

        if ("Appointment cancelled successfully".equals(result)) {
            return ResponseEntity.ok(Map.of("message", result));
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", result));
    }
}
