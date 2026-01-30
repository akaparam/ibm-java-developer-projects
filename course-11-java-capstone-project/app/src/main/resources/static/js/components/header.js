/* header.js
   Dynamic Header Rendering Logic
*/

/* ================================
   Render Header
   ================================ */
function renderHeader() {
    const headerDiv = document.getElementById("header");
    if (!headerDiv) return;

    /* Root page handling */
    if (window.location.pathname.endsWith("/")) {
        localStorage.removeItem("userRole");
        headerDiv.innerHTML = `
            <header class="header">
                <div class="logo-section">
                    <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
                    <span class="logo-title">Hospital CMS</span>
                </div>
            </header>
        `;
        return;
    }

    /* Session data */
    const role = localStorage.getItem("userRole");
    const token = localStorage.getItem("token");

    /* Session expiry check */
    if (
        (role === "loggedPatient" || role === "admin" || role === "doctor") &&
        !token
    ) {
        localStorage.removeItem("userRole");
        alert("Session expired or invalid login. Please log in again.");
        window.location.href = "/";
        return;
    }

    /* Base header */
    let headerContent = `
        <header class="header">
            <div class="logo-section">
                <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
                <span class="logo-title">Hospital CMS</span>
            </div>
            <nav>
    `;

    /* Role-based rendering */
    if (role === "admin") {
        headerContent += `
            <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">
                Add Doctor
            </button>
            <a href="#" onclick="logout()">Logout</a>
        `;
    } else if (role === "doctor") {
        headerContent += `
            <button class="adminBtn" onclick="selectRole('doctor')">Home</button>
            <a href="#" onclick="logout()">Logout</a>
        `;
    } else if (role === "patient") {
        headerContent += `
            <button id="patientLogin" class="adminBtn">Login</button>
            <button id="patientSignup" class="adminBtn">Sign Up</button>
        `;
    } else if (role === "loggedPatient") {
        headerContent += `
            <button class="adminBtn"
                onclick="window.location.href='/pages/loggedPatientDashboard.html'">
                Home
            </button>
            <button class="adminBtn"
                onclick="window.location.href='/pages/patientAppointments.html'">
                Appointments
            </button>
            <a href="#" onclick="logoutPatient()">Logout</a>
        `;
    }

    headerContent += `
            </nav>
        </header>
    `;

    /* Render */
    headerDiv.innerHTML = headerContent;

    /* Attach listeners */
    attachHeaderButtonListeners();
}

/* ================================
   Header Button Listeners
   ================================ */
function attachHeaderButtonListeners() {
    const patientLoginBtn = document.getElementById("patientLogin");
    const patientSignupBtn = document.getElementById("patientSignup");

    if (patientLoginBtn) {
        patientLoginBtn.addEventListener("click", () => {
            openModal("patientLogin");
        });
    }

    if (patientSignupBtn) {
        patientSignupBtn.addEventListener("click", () => {
            openModal("patientSignup");
        });
    }
}

/* ================================
   Logout Helpers
   ================================ */
function logout() {
    localStorage.removeItem("userRole");
    localStorage.removeItem("token");
    window.location.href = "/";
}

function logoutPatient() {
    localStorage.removeItem("token");
    localStorage.removeItem("userRole");
    window.location.href = "/";
}

/* ================================
   Initialize
   ================================ */
renderHeader();
