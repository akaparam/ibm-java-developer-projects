package com.project.back_end.mvc;

import com.project.back_end.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * MVC controller responsible for routing users
 * to role-specific dashboards after token validation.
 */
@Controller
public class DashboardController {

    /**
     * Shared service used for validating tokens
     * across different user roles.
     */
    @Autowired
    private TokenService authService;

    /**
     * Admin Dashboard Route
     *
     * @param token JWT token passed as path variable
     * @return admin dashboard view if token is valid,
     *         otherwise redirect to root
     */
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {
        boolean isValid = authService.validateToken(token, "ADMIN");

        if (isValid) {
            return "admin/adminDashboard";
        }

        return "redirect:/";
    }

    /**
     * Doctor Dashboard Route
     *
     * @param token JWT token passed as path variable
     * @return doctor dashboard view if token is valid,
     *         otherwise redirect to root
     */
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {
        boolean isValid = authService.validateToken(token, "DOCTOR");

        if (isValid) {
            return "doctor/doctorDashboard";
        }

        return "redirect:/";
    }
}
