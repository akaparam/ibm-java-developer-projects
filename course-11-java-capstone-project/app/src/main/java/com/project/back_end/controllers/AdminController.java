package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import com.project.back_end.services.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}admin")
public class    AdminController {

    private final Service service;

    public AdminController(Service service) {
        this.service = service;
    }

    /**
     * Admin login endpoint.
     *
     * @param admin admin login credentials
     * @return JWT token or error message
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> adminLogin(@RequestBody Admin admin) {

        ResponseEntity<String> response =
                service.validateAdmin(admin.getUsername(), admin.getPassword());

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
}
