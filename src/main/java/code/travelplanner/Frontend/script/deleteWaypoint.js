import {token} from "./auth.js";

const deleteWaypointBtn = document.getElementById('deleteWaypointBtn');

export function exitWaypointDeleteMode() {

    deleteWaypointBtn.classList.remove('is-active');
    document.querySelectorAll('.waypoint-item.delete-mode').forEach(item => {
        item.classList.remove('delete-mode');
    });
}

export function waypointDeleteMode() {

    deleteWaypointBtn.classList.add('is-active');
    document.querySelectorAll('.waypoint-item').forEach(item => {
        item.classList.add('delete-mode');
    });
}

export async function deleteWaypoint(tripId, waypointId) {

    try {
        const response  = await fetch(`http://localhost:8080/travelplanner/${tripId}/deleteLinkedWaypoint/${waypointId}`, {
            method: 'DELETE',
            headers: {
                "Authorization" : `Bearer ${token}`,
                "Content-Type" : "application/json"
            }
        });

        if (!response.ok) {
            const errorData = await response.json();
            alert(errorData.message || errorData.error || "An unexpected error occurred");
            return;
        }

        return response.ok;

    } catch (error) {
        console.log("Error deleting waypoint", error);
    }
}