/* doctorCard.js
   Renders a single doctor card with role-based actions
*/

import { openBookingOverlay } from "./loggedPatient.js";
import { deleteDoctor } from "../services/doctorServices.js";
import { fetchPatientDetails } from "../services/patientServices.js";

/* ================================
   Create Doctor Card
   ================================ */
export function createDoctorCard(doctor) {
    /* Main card container */
    const card = document.createElement("div");
    card.className = "doctor-card";

    /* Current user role */
    const role = localStorage.getItem("userRole");

    /* ================================
       Doctor Info Section
       ================================ */
    const info = document.createElement("div");
    info.className = "doctor-info";

    const name = document.createElement("h3");
    name.textContent = doctor.name;

    const specialty = document.createElement("p");
    specialty.textContent = `Specialty: ${doctor.specialty}`;

    const email = document.createElement("p");
    email.textContent = `Email: ${doctor.email}`;

    const times = document.createElement("ul");
    times.className = "available-times";

    if (doctor.availableTimes && doctor.availableTimes.length > 0) {
        doctor.availableTimes.forEach(slot => {
            const li = document.createElement("li");
            li.textContent = slot;
            times.appendChild(li);
        });
    } else {
        const li = document.createElement("li");
        li.textContent = "No available slots";
        times.appendChild(li);
    }

    info.append(name, specialty, email, times);

    /* ================================
       Actions Section
       ================================ */
    const actions = document.createElement("div");
    actions.className = "doctor-actions";

    /* ===== ADMIN ACTIONS ===== */
    if (role === "admin") {
        const deleteBtn = document.createElement("button");
        deleteBtn.textContent = "Delete";
        deleteBtn.className = "adminBtn";

        deleteBtn.addEventListener("click", async () => {
            const token = localStorage.getItem("token");
            if (!token) {
                alert("Unauthorized. Please log in again.");
                return;
            }

            const confirmed = confirm(
                `Are you sure you want to delete Dr. ${doctor.name}?`
            );
            if (!confirmed) return;

            try {
                await deleteDoctor(doctor.id, token);
                alert("Doctor deleted successfully.");
                card.remove();
            } catch (error) {
                alert("Failed to delete doctor.");
                console.error(error);
            }
        });

        actions.appendChild(deleteBtn);
    }

    /* ===== PATIENT (NOT LOGGED-IN) ===== */
    else if (role === "patient") {
        const bookBtn = document.createElement("button");
        bookBtn.textContent = "Book Now";

        bookBtn.addEventListener("click", () => {
            alert("Please log in to book an appointment.");
        });

        actions.appendChild(bookBtn);
    }

    /* ===== LOGGED-IN PATIENT ===== */
    else if (role === "loggedPatient") {
        const bookBtn = document.createElement("button");
        bookBtn.textContent = "Book Now";

        bookBtn.addEventListener("click", async () => {
            const token = localStorage.getItem("token");
            if (!token) {
                alert("Session expired. Please log in again.");
                window.location.href = "/";
                return;
            }

            try {
                const patient = await fetchPatientDetails(token);
                openBookingOverlay({
                    doctor,
                    patient
                });
            } catch (error) {
                alert("Unable to fetch patient details.");
                console.error(error);
            }
        });

        actions.appendChild(bookBtn);
    }

    /* ================================
       Assemble Card
       ================================ */
    card.append(info, actions);

    return card;
}
