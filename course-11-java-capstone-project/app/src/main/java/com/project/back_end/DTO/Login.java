package com.project.back_end.DTO;

/**
 * DTO used for login requests (email + password).
 * Kept minimal for authentication payloads.
 */
public class Login {

    private String email;
    private String password;

    /* Default constructor required for
       JSON deserialization (Jackson) */
    public Login() {
    }

    /* ================================
       Getters & Setters
       ================================ */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
