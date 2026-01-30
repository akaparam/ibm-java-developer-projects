package com.project.back_end.services;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    /* ---------------------------------------------------
     * Save Prescription
     * --------------------------------------------------- */

    public ResponseEntity<Map<String, Object>> savePrescription(Prescription prescription) {
        try {
            List<Prescription> existing =
                    prescriptionRepository.findByAppointmentId(
                            prescription.getAppointmentId()
                    );

            if (!existing.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "message", "Prescription already exists for this appointment"
                        ));
            }

            prescriptionRepository.save(prescription);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "Prescription created successfully"
                    ));

        } catch (Exception e) {
            // log.error("Failed to save prescription", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", "Failed to save prescription"
                    ));
        }
    }

    /* ---------------------------------------------------
     * Get Prescription
     * --------------------------------------------------- */

    public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId) {
        try {
            List<Prescription> prescriptions =
                    prescriptionRepository.findByAppointmentId(appointmentId);

            if (prescriptions.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(Map.of(
                                "message", "No prescription found for this appointment"
                        ));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("prescription", prescriptions.get(0));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // log.error("Failed to fetch prescription", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", "Failed to fetch prescription"
                    ));
        }
    }
}
