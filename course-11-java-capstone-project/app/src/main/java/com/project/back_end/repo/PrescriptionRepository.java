package com.project.back_end.repo;

import com.project.back_end.models.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {

    /**
     * Retrieves all prescriptions associated with a specific appointment.
     *
     * @param appointmentId appointment identifier
     * @return list of matching prescriptions
     */
    List<Prescription> findByAppointmentId(Long appointmentId);
}
