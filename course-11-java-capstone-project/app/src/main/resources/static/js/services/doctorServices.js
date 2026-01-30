/* doctorServices.js
   Handles API calls related to doctors
*/

import { BASE_API_URL } from "./config.js";

/* ================================
   API Endpoint
   ================================ */
const DOCTOR_API = `${BASE_API_URL}/doctors`;

/* ================================
   Get All Doctors
   ================================ */
export async function getDoctors() {
    try {
        const response = await fetch(DOCTOR_API);

        if (!response.ok) {
            console.error("Failed to fetch doctors");
            return [];
        }

        const data = await response.json();
        return data.doctors || [];
    } catch (error) {
        console.error("Error fetching doctors:", error);
        return [];
    }
}

/* ================================
   Delete Doctor (Admin)
   ================================ */
export async function deleteDoctor(doctorId, token) {
    try {
        const response = await fetch(
            `${DOCTOR_API}/${doctorId}?token=${token}`,
            {
                method: "DELETE"
            }
        );

        const data = await response.json();

        return {
            success: response.ok,
            message: data.message || "Doctor deleted"
        };
    } catch (error) {
        console.error("Error deleting doctor:", error);
        return {
            success: false,
            message: "Failed to delete doctor"
        };
    }
}

/* ================================
   Save Doctor (Create)
   ================================ */
export async function saveDoctor(doctor, token) {
    try {
        const response = await fetch(
            `${DOCTOR_API}?token=${token}`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(doctor)
            }
        );

        const data = await response.json();

        return {
            success: response.ok,
            message: data.message || "Doctor saved successfully"
        };
    } catch (error) {
        console.error("Error saving doctor:", error);
        return {
            success: false,
            message: "Failed to save doctor"
        };
    }
}

/* ================================
   Filter Doctors
   ================================ */
export async function filterDoctors(name = "", time = "", specialty = "") {
    try {
        const response = await fetch(
            `${DOCTOR_API}/filter/${encodeURIComponent(name)}/${encodeURIComponent(time)}/${encodeURIComponent(specialty)}`
        );

        if (!response.ok) {
            console.error("Failed to filter doctors");
            return { doctors: [] };
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Error filtering doctors:", error);
        alert("Unable to fetch filtered doctors.");
        return { doctors: [] };
    }
}
