import { API_BASE_URL } from './configuration.js';

async function loginToAccount() {
    const email = document.getElementById("emailEntry").value;
    const password = document.getElementById("passwordEntry").value;

    try {

        // Wait for response, a raw data stream
        const response = await fetch(`${API_BASE_URL}/travelplanner/login`, {
            method: "POST",
            body: JSON.stringify({
                email: email,
                password: password,
            }),
            credentials: 'include',
            headers: {
                "content-type": "application/json; charset=UTF-8",
            }
        })

        if (response.ok) {
            // Parse raw data into JSON object
            const data = await response.json();
            localStorage.setItem("token", data.token);
            console.log("Login successful");
            // Redirect to /home page after successful login
            window.location.href = "./home.html";

        } else {
            const errorData = await response.json();
            console.log(errorData);
            alert(errorData.message || errorData.error || "An unexpected error occurred");
        }

    } catch (error) {
        console.log("Error logging into account", error);
    }
}

window.loginToAccount = loginToAccount;