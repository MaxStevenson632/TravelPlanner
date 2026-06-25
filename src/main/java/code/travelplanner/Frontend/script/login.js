async function loginToAccount() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {

        // Wait for response, a raw data stream
        const response = await fetch("http://localhost:8080/travelplanner/login", {
            method: "POST",
            body: JSON.stringify({
                email: email,
                password: password,
            }),
            credentials: 'include',
            headers: {
                "content-type": "application/json; charset=UTF-8"
            }
        })

        if (response.ok) {

            // Parse raw data into JSON object
            const data = await response.json();
            console.log("Login successful");
            // Redirect to /home page after successful login
            window.location.href = "./home.html";

        } else {
            alert("Invalid email or password");
        }

    } catch (error) {
        console.log("Error logging into account", error);
    }
}
