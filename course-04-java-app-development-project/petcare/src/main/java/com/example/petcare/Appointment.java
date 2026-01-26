package com.example.petcare;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;
    private LocalDateTime dateTime;
    private String notes;

    public Appointment(String type, LocalDateTime dateTime, String notes) {
        this.type = type;
        this.dateTime = dateTime;
        this.notes = notes;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "type='" + type + '\'' +
                ", dateTime=" + dateTime +
                (notes == null || notes.isBlank() ? "" : ", notes='" + notes + '\'') +
                '}';
    }
}
