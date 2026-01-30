/* doctorDashboard.js
   Handles doctor dashboard appointment management
*/

import { getAllAppointments } from "../services/appointmentServices.js";
import { createPatientRow } from "../components/patientRow.js";

/* ================================
   State & DOM References
   ================================ */
const tableBody = document.getElementById("patientTableBody");
const searchBar = document.getElementById("searchBar");
const todayButton = document.getElementById("todayButton");
const datePicker = document.getElementById("datePicker");

const token = localStorage.getItem("token");

let selectedDate = new Date().toISOString().split("T")[0];
let patientName = null;

/* ================================
   Search Bar – Filter by Patient Name
   ================================ */
if (searchBar) {
    searchBar.addEventListener("input", () => {
        const value = searchBar.value.trim();
        patientName = value !== "" ? value : null;
        loadAppointments();
    });
}

/* ================================
   Today Button
   ================================ */
if (todayButton) {
    todayButton.addEventListener("click", () => {
        selectedDate = new Date().toISOString().split("T")[0];
        datePicker.value = selectedDate;
        loadAppointments();
    });
}

/* ================================
   Date Picker
   ================================ */
if (datePicker) {
    datePicker.value = selectedDate;

    datePicker.addEventListener("change", () => {
        selectedDate = datePicker.value;
        loadAppointments();
    });
}

/* ================================
   Load Appointments
   ================================ */
async function loadAppointments() {
    if (!token) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="5">Unauthorized. Please log in again.</td>
            </tr>
        `;
        return;
    }

    try {
        const appointments = await getAllAppointments(
            selectedDate,
            patientName,
            token
        );

        tableBody.innerHTML = "";

        if (!appointments || appointments.length === 0) {
            tableBody.innerHTML = `
                <tr>
                    <td colspan="5">No Appointments found for today.</td>
                </tr>
            `;
            return;
        }

        appointments.forEach(appt => {
            const patient = {
                id: appt.patient.id,
                name: appt.patient.name,
                phone: appt.patient.phone,
                email: appt.patient.email
            };

            const row = createPatientRow(patient, appt);
            tableBody.appendChild(row);
        });

    } catch (error) {
        console.error("Error loading appointments:", error);
        tableBody.innerHTML = `
            <tr>
                <td colspan="5">Error loading appointments. Try again later.</td>
            </tr>
        `;
    }
}

/* ================================
   Initialize on Page Load
   ================================ */
document.addEventListener("DOMContentLoaded", () => {
    if (typeof renderContent === "function") {
        renderContent();
    }
    loadAppointments();
});
