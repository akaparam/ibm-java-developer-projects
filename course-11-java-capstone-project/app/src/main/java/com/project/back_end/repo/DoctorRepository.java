package com.project.back_end.repo;

import com.project.back_end.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    /**
     * Retrieves a Doctor by email.
     *
     * @param email doctor's email
     * @return the matching Doctor, or null if none found
     */
    Doctor findByEmail(String email);

    /**
     * Retrieves doctors whose name contains the given value (case-sensitive).
     *
     * @param name partial name to search
     * @return list of matching doctors
     */
    List<Doctor> findByNameLike(String name);

    /**
     * Retrieves doctors whose name contains the given value (case-insensitive)
     * and whose specialty matches exactly (case-insensitive).
     *
     * @param name doctor name fragment
     * @param specialty doctor specialty
     * @return list of matching doctors
     */
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
            String name,
            String specialty
    );

    /**
     * Retrieves doctors by specialty (case-insensitive).
     *
     * @param specialty doctor specialty
     * @return list of matching doctors
     */
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}
