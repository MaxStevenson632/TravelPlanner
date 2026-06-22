async function registerAccount() {

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const repeatedPassword = document.getElementById("repeatedPassword").value;
    const email = document.getElementById("email").value;
    const registrationUI = document.getElementById("registrationPage");

    if (repeatedPassword === password) {
        try {
            console.log("Javascript code");
            console.log("Username: ", username);
            console.log("Password: ", password);
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
                .then((response) => response.json())
                .then((json) => console.log(json));

        } catch (error) {
            console.log("Error creating account", error);
        }

        //if (response.status === 21) {
         //   console.log("User added succesfully");
        //    //window.location.href = "welcome.html";
        //} else {
       //     console.error("Registration failed");
       // }

    } else {
        console.log("Password not the same");
    }
}