# Admin User Stories

## User Story 1

**Title:** Admin Login
*As an admin, I want to log into the portal using my username and password, so that I can securely manage the clinic system.*
**Acceptance Criteria:**

1. Admin can log in with valid credentials.
2. Invalid credentials show an error message.
3. Successful login redirects to admin dashboard.
   **Priority:** High
   **Story Points:** 3
   **Notes:**

* Authentication handled via backend validation.

---

## User Story 2

**Title:** Admin Logout
*As an admin, I want to log out of the portal, so that unauthorized users cannot access the system.*
**Acceptance Criteria:**

1. Admin can log out from any page.
2. Session is invalidated on logout.
3. User is redirected to login page.
   **Priority:** Medium
   **Story Points:** 2
   **Notes:**

* Logout clears session/token.

---

## User Story 3

**Title:** Add Doctor Profile
*As an admin, I want to add doctor profiles, so that doctors can be managed within the system.*
**Acceptance Criteria:**

1. Admin can create a doctor profile.
2. Required fields must be validated.
3. Doctor is saved in the database.
   **Priority:** High
   **Story Points:** 5
   **Notes:**

* Stored in MySQL.

---

## User Story 4

**Title:** Delete Doctor Profile
*As an admin, I want to delete a doctor’s profile, so that inactive doctors are removed from the system.*
**Acceptance Criteria:**

1. Admin can delete an existing doctor.
2. Confirmation is required before deletion.
3. Doctor is removed from the database.
   **Priority:** Medium
   **Story Points:** 3
   **Notes:**

* Appointments should be checked before deletion.

---

## User Story 5

**Title:** View Appointment Statistics
*As an admin, I want to run a stored procedure to view monthly appointment counts, so that I can track system usage.*
**Acceptance Criteria:**

1. Stored procedure executes successfully.
2. Monthly appointment count is displayed.
3. Data is fetched from MySQL.
   **Priority:** Low
   **Story Points:** 5
   **Notes:**

* Read-only analytics operation.

---

# Patient User Stories

## User Story 6

**Title:** View Doctors Without Login
*As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering.*
**Acceptance Criteria:**

1. Doctor list is publicly accessible.
2. Basic doctor details are visible.
3. No authentication required.
   **Priority:** Medium
   **Story Points:** 2
   **Notes:**

* Read-only access.

---

## User Story 7

**Title:** Patient Registration
*As a patient, I want to sign up using my email and password, so that I can book appointments.*
**Acceptance Criteria:**

1. Patient can register with valid details.
2. Duplicate emails are not allowed.
3. Account is saved successfully.
   **Priority:** High
   **Story Points:** 5
   **Notes:**

* Password must be encrypted.

---

## User Story 8

**Title:** Patient Login
*As a patient, I want to log into the portal, so that I can manage my appointments.*
**Acceptance Criteria:**

1. Login works with valid credentials.
2. Invalid login shows error.
3. User is redirected to dashboard.
   **Priority:** High
   **Story Points:** 3
   **Notes:**

* Session-based or token-based auth.

---

## User Story 9

**Title:** Book Appointment
*As a patient, I want to book an hour-long appointment with a doctor, so that I can receive consultation.*
**Acceptance Criteria:**

1. Patient selects doctor and time slot.
2. Slot availability is validated.
3. Appointment is saved successfully.
   **Priority:** High
   **Story Points:** 5
   **Notes:**

* Appointment stored in MySQL.

---

## User Story 10

**Title:** View Upcoming Appointments
*As a patient, I want to view my upcoming appointments, so that I can prepare accordingly.*
**Acceptance Criteria:**

1. Patient can see future appointments.
2. Appointment details are displayed.
3. Data is fetched correctly.
   **Priority:** Medium
   **Story Points:** 3
   **Notes:**

* Sorted by date/time.

---

# Doctor User Stories

## User Story 11

**Title:** Doctor Login
*As a doctor, I want to log into the portal, so that I can manage my schedule.*
**Acceptance Criteria:**

1. Doctor can log in with credentials.
2. Unauthorized access is blocked.
3. Dashboard loads on success.
   **Priority:** High
   **Story Points:** 3
   **Notes:**

* Role-based access control applied.

---

## User Story 12

**Title:** Doctor Logout
*As a doctor, I want to log out of the portal, so that my data remains secure.*
**Acceptance Criteria:**

1. Logout invalidates session.
2. User is redirected to login page.
3. No data is accessible after logout.
   **Priority:** Medium
   **Story Points:** 2
   **Notes:**

* Common logout mechanism.

---

## User Story 13

**Title:** View Appointment Calendar
*As a doctor, I want to view my appointment calendar, so that I can stay organized.*
**Acceptance Criteria:**

1. Doctor sees daily and weekly appointments.
2. Appointments are time-ordered.
3. Only assigned appointments are shown.
   **Priority:** High
   **Story Points:** 5
   **Notes:**

* Read-only calendar view.

---

## User Story 14

**Title:** Mark Unavailability
*As a doctor, I want to mark my unavailable time slots, so that patients can book only available slots.*
**Acceptance Criteria:**

1. Doctor can block time slots.
2. Blocked slots are not bookable.
3. Changes are saved successfully.
   **Priority:** Medium
   **Story Points:** 5
   **Notes:**

* Impacts appointment booking logic.

---

## User Story 15

**Title:** View Patient Details
*As a doctor, I want to view patient details for upcoming appointments, so that I can be prepared.*
**Acceptance Criteria:**

1. Doctor can access patient details.
2. Data is visible only for assigned appointments.
3. Patient information loads correctly.
   **Priority:** High
   **Story Points:** 3
   **Notes:**

* Access restricted by role.