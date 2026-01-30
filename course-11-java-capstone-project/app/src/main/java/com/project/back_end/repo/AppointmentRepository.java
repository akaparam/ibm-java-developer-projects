package com.project.back_end.repo;

import com.project.back_end.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Retrieves appointments for a doctor within a time range,
     * eagerly fetching the doctor's available times.
     */
    @Query("""
           SELECT a
           FROM Appointment a
           LEFT JOIN FETCH a.doctor d
           LEFT JOIN FETCH d.availableTimes
           WHERE d.id = :doctorId
             AND a.appointmentTime BETWEEN :start AND :end
           """)
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(
            @Param("doctorId") Long doctorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    /**
     * Retrieves appointments for a doctor and patient name (case-insensitive)
     * within a time range, fetching doctor and patient details.
     */
    @Query("""
           SELECT a
           FROM Appointment a
           LEFT JOIN FETCH a.doctor d
           LEFT JOIN FETCH a.patient p
           WHERE d.id = :doctorId
             AND LOWER(p.name) LIKE LOWER(CONCAT('%', :patientName, '%'))
             AND a.appointmentTime BETWEEN :start AND :end
           """)
    List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
            @Param("doctorId") Long doctorId,
            @Param("patientName") String patientName,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    /**
     * Deletes all appointments for a given doctor.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Appointment a WHERE a.doctor.id = :doctorId")
    void deleteAllByDoctorId(@Param("doctorId") Long doctorId);

    /**
     * Retrieves all appointments for a patient.
     */
    List<Appointment> findByPatient_Id(Long patientId);

    /**
     * Retrieves appointments for a patient by status,
     * ordered by appointment time ascending.
     */
    List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(
            Long patientId,
            int status
    );

    /**
     * Filters appointments by doctor name (LIKE) and patient ID.
     */
    @Query("""
           SELECT a
           FROM Appointment a
           JOIN a.doctor d
           WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%'))
             AND a.patient.id = :patientId
           """)
    List<Appointment> filterByDoctorNameAndPatientId(
            @Param("doctorName") String doctorName,
            @Param("patientId") Long patientId
    );

    /**
     * Filters appointments by doctor name (LIKE), patient ID, and status.
     */
    @Query("""
           SELECT a
           FROM Appointment a
           JOIN a.doctor d
           WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%'))
             AND a.patient.id = :patientId
             AND a.status = :status
           """)
    List<Appointment> filterByDoctorNameAndPatientIdAndStatus(
            @Param("doctorName") String doctorName,
            @Param("patientId") Long patientId,
            @Param("status") int status
    );

    /**
     * Updates the status of an appointment by ID.
     */
    @Modifying
    @Transactional
    @Query("UPDATE Appointment a SET a.status = :status WHERE a.id = :id")
    void updateStatus(
            @Param("status") int status,
            @Param("id") long id
    );
}
