async function loginToAccount() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const response = await fetch("http://localhost:8080/travelplanner/login", {
        method: "POST",
        body: JSON.stringify({
            username: username,
            password: password,
        }),
        credentials: 'include',
        headers: {
            "content-type": "application/json; charset=UTF-8"
        }
    })
        .then((response) => response.json())
        .then((json) => console.log(json));
}