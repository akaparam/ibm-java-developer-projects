/* adminDashboard.js
   Admin dashboard logic for managing doctors
*/

import { openModal, closeModal } from "../modal.js";
import { getDoctors, filterDoctors, saveDoctor } from "../services/doctorServices.js";
import { createDoctorCard } from "../components/doctorCard.js";

/* ================================
   DOM References
   ================================ */
const contentDiv = document.getElementById("content");
const searchBar = document.getElementById("searchBar");
const timeFilter = document.getElementById("timeFilter");
const specialtyFilter = document.getElementById("specialtyFilter");

/* ================================
   Add Doctor Button
   ================================ */
document.addEventListener("click", (e) => {
    if (e.target && e.target.id === "addDocBtn") {
        openModal("addDoctor");
    }
});

/* ================================
   Initial Load
   ================================ */
document.addEventListener("DOMContentLoaded", () => {
    loadDoctorCards();
});

/* ================================
   Load All Doctors
   ================================ */
async function loadDoctorCards() {
    try {
        const doctors = await getDoctors();
        renderDoctorCards(doctors);
    } catch (error) {
        console.error("Error loading doctors:", error);
    }
}

/* ================================
   Filters
   ================================ */
if (searchBar) {
    searchBar.addEventListener("input", filterDoctorsOnChange);
}
if (timeFilter) {
    timeFilter.addEventListener("change", filterDoctorsOnChange);
}
if (specialtyFilter) {
    specialtyFilter.addEventListener("change", filterDoctorsOnChange);
}

async function filterDoctorsOnChange() {
    try {
        const name = searchBar?.value?.trim() || "";
        const time = timeFilter?.value || "";
        const specialty = specialtyFilter?.value || "";

        const result = await filterDoctors(name, time, specialty);
        const doctors = result.doctors || [];

        if (doctors.length === 0) {
            contentDiv.innerHTML = `<p>No doctors found with the given filters.</p>`;
            return;
        }

        renderDoctorCards(doctors);
    } catch (error) {
        console.error("Error filtering doctors:", error);
        alert("Failed to filter doctors.");
    }
}

/* ================================
   Render Doctor Cards
   ================================ */
function renderDoctorCards(doctors) {
    contentDiv.innerHTML = "";

    doctors.forEach(doctor => {
        const card = createDoctorCard(doctor);
        contentDiv.appendChild(card);
    });
}

/* ================================
   Add Doctor Handler
   ================================ */
window.adminAddDoctor = async function () {
    try {
        const name = document.getElementById("doctorName").value;
        const email = document.getElementById("doctorEmail").value;
        const phone = document.getElementById("doctorPhone").value;
        const password = document.getElementById("doctorPassword").value;
        const specialty = document.getElementById("doctorSpecialty").value;
        const availableTimes = document
            .getElementById("doctorAvailableTimes")
            .value
            .split(",")
            .map(t => t.trim())
            .filter(Boolean);

        const token = localStorage.getItem("token");
        if (!token) {
            alert("Unauthorized. Please log in again.");
            return;
        }

        const doctor = {
            name,
            email,
            phone,
            password,
            specialty,
            availableTimes
        };

        const result = await saveDoctor(doctor, token);

        if (result.success) {
            alert(result.message || "Doctor added successfully.");
            closeModal();
            loadDoctorCards();
        } else {
            alert(result.message || "Failed to add doctor.");
        }

    } catch (error) {
        console.error("Error adding doctor:", error);
        alert("Something went wrong while adding doctor.");
    }
};
