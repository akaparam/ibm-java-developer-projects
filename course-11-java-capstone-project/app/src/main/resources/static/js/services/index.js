/* login.js
   Handles Admin and Doctor login logic
*/

import { openModal } from "./modal.js";
import { BASE_API_URL } from "./config.js";

/* ================================
   API Endpoints
   ================================ */
const ADMIN_API = `${BASE_API_URL}/auth/admin/login`;
const DOCTOR_API = `${BASE_API_URL}/auth/doctor/login`;

/* ================================
   Attach Login Button Handlers
   ================================ */
window.onload = () => {
    const adminLoginBtn = document.getElementById("adminLogin");
    const doctorLoginBtn = document.getElementById("doctorLogin");

    if (adminLoginBtn) {
        adminLoginBtn.addEventListener("click", () => {
            openModal("adminLogin");
        });
    }

    if (doctorLoginBtn) {
        doctorLoginBtn.addEventListener("click", () => {
            openModal("doctorLogin");
        });
    }
};

/* ================================
   Admin Login Handler
   ================================ */
window.adminLoginHandler = async function () {
    try {
        const username = document.getElementById("adminUsername").value;
        const password = document.getElementById("adminPassword").value;

        const admin = { username, password };

        const response = await fetch(ADMIN_API, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(admin)
        });

        if (!response.ok) {
            alert("Invalid admin credentials.");
            return;
        }

        const data = await response.json();
        localStorage.setItem("token", data.token);
        localStorage.setItem("userRole", "admin");

        selectRole("admin");

    } catch (error) {
        console.error(error);
        alert("Something went wrong. Please try again later.");
    }
};

/* ================================
   Doctor Login Handler
   ================================ */
window.doctorLoginHandler = async function () {
    try {
        const email = document.getElementById("doctorEmail").value;
        const password = document.getElementById("doctorPassword").value;

        const doctor = { email, password };

        const response = await fetch(DOCTOR_API, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(doctor)
        });

        if (!response.ok) {
            alert("Invalid doctor credentials.");
            return;
        }

        const data = await response.json();
        localStorage.setItem("token", data.token);
        localStorage.setItem("userRole", "doctor");

        selectRole("doctor");

    } catch (error) {
        console.error(error);
        alert("Something went wrong. Please try again later.");
    }
};
