# Smart Clinic Management System – Schema Design

## MySQL Database Design

The MySQL database is used to store structured, relational, and transactional data that requires strong consistency and relationships. Core clinic data such as users, doctors, patients, admins, and appointments are stored here using normalized tables and foreign key constraints.

---

### Table: patients

* id: INT, Primary Key, Auto Increment
* name: VARCHAR(100), Not Null
* email: VARCHAR(100), Unique, Not Null
* phone: VARCHAR(15), Not Null
* password: VARCHAR(255), Not Null
* created_at: TIMESTAMP, Default CURRENT_TIMESTAMP

*Patients can have multiple appointments. Patient records are retained even if appointments are deleted.*

---

### Table: doctors

* id: INT, Primary Key, Auto Increment
* name: VARCHAR(100), Not Null
* email: VARCHAR(100), Unique, Not Null
* specialization: VARCHAR(100), Not Null
* phone: VARCHAR(15), Not Null
* available: BOOLEAN, Default TRUE
* created_at: TIMESTAMP, Default CURRENT_TIMESTAMP

*Doctors can have many appointments but should not have overlapping appointment times (validated in service layer).*

---

### Table: admin

* id: INT, Primary Key, Auto Increment
* username: VARCHAR(50), Unique, Not Null
* password: VARCHAR(255), Not Null
* created_at: TIMESTAMP, Default CURRENT_TIMESTAMP

*Admins manage doctors and system-level operations.*

---

### Table: appointments

* id: INT, Primary Key, Auto Increment
* doctor_id: INT, Foreign Key → doctors(id)
* patient_id: INT, Foreign Key → patients(id)
* appointment_time: DATETIME, Not Null
* duration_minutes: INT, Default 60
* status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)
* created_at: TIMESTAMP, Default CURRENT_TIMESTAMP

*If a patient is deleted, appointment history may be retained for auditing. If a doctor is deleted, appointments should be reviewed before removal.*

---

## MongoDB Collection Design

MongoDB is used to store flexible and document-based data that may change in structure over time. Prescriptions are stored in MongoDB because they often contain nested data, optional fields, and doctor-specific notes.

---

### Collection: prescriptions

```json
{
  "_id": "ObjectId('65fa123abc456')",
  "appointmentId": 101,
  "patientId": 12,
  "doctorId": 5,
  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500mg",
      "frequency": "Twice a day",
      "duration": "5 days"
    }
  ],
  "doctorNotes": "Take after meals. Drink plenty of water.",
  "issuedDate": "2026-01-30",
  "refillAllowed": true,
  "tags": ["fever", "pain-relief"]
}
```

*Only references to patient, doctor, and appointment IDs are stored instead of full objects to avoid data duplication. The document structure allows easy future expansion, such as adding lab reports or attachments.*