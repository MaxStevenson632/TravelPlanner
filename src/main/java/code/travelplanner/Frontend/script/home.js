document.addEventListener("DOMContentLoaded", () => {

    const AddTripBtn = document.getElementById("addTripBtn");
    const cancelTripBtn = document.getElementById("cancelTripBtn");
    const tripFormContainer = document.getElementById("tripFormContainer");
    const tripForm = document.getElementById("tripForm");

    AddTripBtn.addEventListener("click", () => {
        tripFormContainer.classList.remove("hidden");
    });

    cancelTripBtn.addEventListener("click", () => {
        tripFormContainer.classList.add("hidden");
        tripForm.reset();
    });

    tripForm.addEventListener("submit", async (e) => {

        e.preventDefault();

        const waypointsArray = document.getElementById("waypoints").value
            // Separate all values with commas
            .split(",")
            // Create new array, remove whitespaces on each end of each item
            .map(item => item.trim())
            // No empty strings
            .filter(item => item !== "");

        const title = document.getElementById("title").value;
        const waypoints = waypointsArray;
        const startDate = document.getElementById("startDate").value;
        const endDate = document.getElementById("endDate").value;
        const token = localStorage.getItem("token");

        try {
            const response = await fetch("http://localhost:8080/travelplanner/createTrip",  {
                method: "POST",
                body: JSON.stringify({
                    title: title,
                    waypoints: waypoints,
                    startDate: startDate,
                    endDate: endDate,
                }),
                credentials: 'include',
                headers: {
                    "content-type": "application/json; charset=UTF-8",
                    "Authorization": `Bearer ${token}`
                }
            })


        } catch (e) {
            const errorData = await response.json();
            console.log(errorData);
            alert(errorData.message || errorData.error || "An unexpected error occurred");
        }


    })
})
