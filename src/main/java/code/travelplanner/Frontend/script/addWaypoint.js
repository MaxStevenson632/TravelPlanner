import { token } from './auth.js';
import {sanitizedHTML} from "./utils.js";
import {renderTripWaypointsAndMembers} from "./sidebarRenderer.js";
import { selectedPlaceState } from './Map/mapSearch.js';

let tripId = null;
let activeWaypointElement = null;

export function handleReorderKeydown(event) {

    if (!activeWaypointElement) {
        return;
    }

    // User moves the waypoint up in list of waypoints
    if (event.key === 'ArrowUp') {
        event.preventDefault();
        const previousElement = activeWaypointElement.previousElementSibling;
        if (previousElement) {

            // Move waypoint before the element before the waypoint
            activeWaypointElement.parentNode.insertBefore(activeWaypointElement, previousElement);
        }

        // User moves the waypoint down in list of waypoints
    } else if (event.key === 'ArrowDown') {
        event.preventDefault();
        const nextElement = activeWaypointElement.nextElementSibling;
        if (nextElement) {

            // Move waypoint before the waypoint 2 places after it
            activeWaypointElement.parentNode.insertBefore(activeWaypointElement, nextElement.nextElementSibling);
        }

        // User submits the waypoint
    } else if (event.key === 'Enter') {
        event.preventDefault();

        // Stop reordering UI and event listener
        activeWaypointElement.classList.remove('reordering');
        document.removeEventListener('keydown', handleReorderKeydown);

        // Get new waypoint's index
        const allWaypoints = Array.from(document.querySelectorAll('.waypoint-item'));
        const newWaypointIndex = allWaypoints.indexOf(activeWaypointElement);

        // Send waypoint data to backend
        saveWaypointsToTrip({
            placeName: selectedPlaceState.selectedPlace.display_name.split(',')[0],
            latitude: parseFloat(selectedPlaceState.selectedPlace.lat),
            longitude: parseFloat(selectedPlaceState.selectedPlace.lon),
            visitOrder: newWaypointIndex + 1
        });

        // Reset for next waypoint addition
        activeWaypointElement = null;
        selectedPlaceState.selectedPlace = null;

        // User cancels adding new waypoint
    } else if (event.key === 'Backspace' || event.key === 'Delete') {
        event.preventDefault();

        // Reset and remove adding waypoint UI and variables
        activeWaypointElement.classList.remove('reordering');
        document.removeEventListener('keydown', handleReorderKeydown);
        const waypointList = document.getElementById('detailWaypointsList');
        waypointList.removeChild(activeWaypointElement);
        activeWaypointElement = null;
    }
}

async function saveWaypointsToTrip(waypointData) {

    try {
        const response = await fetch(`http://localhost:8080/travelplanner/${tripId}/addWaypoint`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(waypointData)
        });

        if (!response.ok) {
            const errorData = await response.json();
            alert(errorData.message || errorData.error || "An unexpected error occurred");
        } else {
            console.log(response);
            // Render waypoints again
            renderTripWaypointsAndMembers(tripId);
        }
    } catch (error) {
        console.log("Save failed", error);
    }
}

export function displayWaypointForOrdering(passedTripId) {

    if (!selectedPlaceState.selectedPlace) {
        return;
    }

    tripId = passedTripId;

    // Remove button
    document.getElementById('addToTripBtn').classList.add('hidden');

    const waypointList = document.getElementById('detailWaypointsList');
    activeWaypointElement = document.createElement('div');
    activeWaypointElement.className = 'waypoint-item reordering';

    // Display the new waypoint within list of existing waypoints
    activeWaypointElement.innerHTML = `
    <span class = "waypoint-icon"></span>
    <span class = "waypoint-name"> ${sanitizedHTML(selectedPlaceState.selectedPlace.display_name.split(',')[0])}</span>
        `;

    waypointList.appendChild(activeWaypointElement);
}

