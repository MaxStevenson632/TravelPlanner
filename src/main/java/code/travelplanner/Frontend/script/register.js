async function registerAccount() {

    const username = document.getElementById("usernameEntry").value;
    const password = document.getElementById("passwordEntry").value;
    const repeatedPassword = document.getElementById("confirmPassword").value;
    const email = document.getElementById("emailEntry").value;

    if (repeatedPassword === password) {
        try {

            const response = await fetch("http://localhost:8080/travelplanner/register", {
                method: "POST",
                // The actual data to be sent to the server
                body:JSON.stringify({
                    username: username,
                    password: password,
                    email: email
                }),
                credentials: 'include',
                // The type of content to be sent to the server (JSON)
                headers: {
                    "content-type": "application/json; charset=UTF-8"
                }
            })

            if (response.ok) {
                const data = await response.json();
                // Redirect to /home page after successful login
                console.log("Registration successful");
                window.location.href = "./login.html";

            } else {
                alert("Email already associated with an account");
            }

        } catch (error) {
            console.log("Error creating account", error);
        }

    } else {
        console.log("Password not the same");
    }
}