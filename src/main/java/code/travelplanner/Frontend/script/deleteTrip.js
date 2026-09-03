import {token} from "./auth.js";

export async function deleteTrip(userId, tripId) {

    try {
        const response = await fetch(`http://localhost:8080/travelplanner/${tripId}/deleteTrip`,
            {
                method: 'DELETE',
                headers: {
                    "Authorization" : `Bearer ${token}`,
                    "Content-Type" : "application/json"
                }
            }
        );

        if (!response.ok) {
            const errorData = await response.json();
            alert(errorData.message || errorData.error || "An unexpected error occurred");
            return;
        }

    } catch (error) {
        console.log("Error deleting member from trip", error);
    }
}

export function deleteTripMode() {

    const deleteButton = document.getElementById("deleteTripBtn");
    deleteButton.classList.add("is-active");

    // Highlight all rows where person is OWNER
    let ownerMembers = document.querySelectorAll(".tripListItem:is([data-user-role='OWNER'])");
    ownerMembers.forEach(row => {
        row.classList.add("delete-mode");
    })
}

export function exitTripDeleteMode() {

    const deleteButton = document.getElementById("deleteTripBtn");
    deleteButton.classList.remove("is-active");

    let highlightedRows = document.querySelectorAll(".tripListItem.delete-mode");
    highlightedRows.forEach(row => {
        row.classList.remove("delete-mode");
    });
}