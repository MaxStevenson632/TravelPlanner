const token = localStorage.getItem('token');

document.addEventListener("DOMContentLoaded", () => {

    const usernameHeader = document.getElementById("usernameHeader");
    const h1 = document.createElement('h1');
    h1.innerHTML = sanitizedHTML(getAccountNameFromToken());
    usernameHeader.appendChild(h1);

    loadUserTripsAndName();

    const AddTripBtn = document.getElementById("addTripBtn");
    const cancelTripBtn = document.getElementById("cancelTripBtn");
    const tripFormContainer = document.getElementById("tripFormContainer");
    const tripForm = document.getElementById("tripForm");
    const tripListItem = document.getElementById("tripList");

    // If user clicks on a trip
    tripListItem.addEventListener('click', () => {
        // Remove active attribute from all other trips
        document.querySelectorAll('.tripListItem.active').forEach(item => {
            item.classList.remove('active');
        });

        // Add active attribute to that trip
        tripListItem.classList.add('active');


    });

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

async function getMapData(tripId) {

    try {
        const response = await fetch(`http://localhost:8080/travelplanner/${tripId}/map-data`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        const tripData = await response.json();

        if (!response.ok) {
            const errorData = await response.json();
            console.log(errorData);
        }

        return tripData;

    } catch(e) {
        console.log("Error");
    }
}

async function loadUserTripsAndName() {

    try {
        const response = await fetch("http://localhost:8080/travelplanner/retrieve-trips", {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })

        if (!response.ok) {
            throw new Error(`Failed to fetch trips: ${response.status}`);
        }

        // Get the array from the object
        const { tripDataDto: tripsArray = [] } = await response.json();
        const tripListElement = document.querySelector('#tripList');

        if (!tripListElement) {
            console.error("Target element 'tripList' not found in HTML.");
            return;
        }

        // Clears existing content, removes duplicates
        tripListElement.innerHTML = '';

        if (tripsArray.length === 0) {
            tripListElement.innerHTML = '<li> No trips found, create a new one or join one </li>';
            return;
        }

        // Output trip name and role to sidebar
        tripsArray.forEach(trip => {

            const li = document.createElement('li');

            li.className = 'tripListItem';
            li.dataset.tripId = trip.id;

            // Different CSS classes for each field
            li.innerHTML = `
            <span class = "tripTitle">${sanitizedHTML(trip.name)}</span>
            <span class = "roleBadge">${sanitizedHTML(trip.role)}</span>
            `;

            tripListElement.appendChild(li);
        });

    } catch(e) {
        console.log("Error loading trips into list", e);
    }
}

// Helper function to sanitize user input - prevent XSS attack
function sanitizedHTML(str) {

    if (!str) {
        return '';
    }
    return str.replace(/[&<>"']/g, match => ({
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;'
    }[match]));
}

function getAccountNameFromToken() {

    try {

        // Split the token apart, use the payload (second part)
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');

        // Decode JSON string
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
        );

        const payload = JSON.parse(jsonPayload);

        return payload.name;

    } catch (error) {
        console.error("Failed to decode JWT:", error);
        return null;
    }
}


