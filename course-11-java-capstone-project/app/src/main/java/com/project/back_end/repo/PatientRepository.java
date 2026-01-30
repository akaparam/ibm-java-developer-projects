package com.project.back_end.repo;

import com.project.back_end.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Retrieves a Patient by email.
     *
     * @param email patient email
     * @return the matching Patient, or null if none found
     */
    Patient findByEmail(String email);

    /**
     * Retrieves a Patient by email or phone number.
     *
     * @param email patient email
     * @param phone patient phone number
     * @return the matching Patient, or null if none found
     */
    Patient findByEmailOrPhone(String email, String phone);
}
