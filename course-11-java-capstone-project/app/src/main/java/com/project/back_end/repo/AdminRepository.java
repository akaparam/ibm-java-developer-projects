package com.project.back_end.repo;

import com.project.back_end.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    /**
     * Finds an Admin entity by username.
     *
     * @param username the username to search for
     * @return the matching Admin, or null if none found
     */
    Admin findByUsername(String username);
}
