import { token } from './auth.js';
import { API_BASE_URL } from './configuration.js';
import { sanitizedHTML } from "./utils.js";

export async function getMapData(tripId) {

    try {
        const response = await fetch(`${API_BASE_URL}/travelplanner/${tripId}/map-data`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        const tripData = await response.json();

        if (!response.ok) {
            const errorData = await response.json();
            alert(errorData.message || errorData.error || "An unexpected error occurred");
        }

        return tripData;

    } catch(e) {
        console.log("Error");
    }
}

export async function loadUserTripsAndName() {

    try {
        const response = await fetch(`${API_BASE_URL}/travelplanner/retrieve-trips`, {
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
            li.dataset.userRole = trip.role;

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

export async function createTrip(event) {

    event.preventDefault();

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

    let response = null;

    try {
        response = await fetch(`${API_BASE_URL}/travelplanner/createTrip`,  {
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
}


