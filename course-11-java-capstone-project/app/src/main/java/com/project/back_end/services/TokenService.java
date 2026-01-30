package com.project.back_end.services;

import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class TokenService {

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public TokenService(
            AdminRepository adminRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository
    ) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    /* ---------------------------------------------------
     * Signing Key
     * --------------------------------------------------- */

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /* ---------------------------------------------------
     * Token Generation
     * --------------------------------------------------- */

    public String generateToken(String email, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 7L * 24 * 60 * 60 * 1000); // 7 days

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    /* ---------------------------------------------------
     * Token Parsing
     * --------------------------------------------------- */

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /* ---------------------------------------------------
     * Token Validation
     * --------------------------------------------------- */

    public boolean validateToken(String token, String role) {
        try {
            Claims claims = getClaims(token);
            String email = claims.getSubject();

            if (email == null || role == null) {
                return false;
            }

            return switch (role.toUpperCase()) {
                case "ADMIN" -> adminRepository.findByUsername(email) != null;
                case "DOCTOR" -> doctorRepository.findByEmail(email) != null;
                case "PATIENT" -> patientRepository.findByEmail(email) != null;
                default -> false;
            };

        } catch (Exception e) {
            return false;
        }
    }
}
