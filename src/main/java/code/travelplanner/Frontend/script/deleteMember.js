import { token } from "./auth.js";

export async function deleteMember(userId, tripId) {

    try {
        const response = await fetch(`http://localhost:8080/travelplanner/${tripId}/members/deleteMember/${userId}`,
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

export function deleteMode() {

    const deleteButton = document.getElementById("deleteMemberBtn");
    deleteButton.classList.add("is-active");

    // Highlight all rows where person is not OWNER
    let nonOwnerMembers = document.querySelectorAll(".person-row:not([data-user-role='OWNER'])");
    nonOwnerMembers.forEach(row => {
        row.classList.add("delete-mode");
    })
}

export function exitDeleteMode() {

    const deleteButton = document.getElementById("deleteMemberBtn");
    deleteButton.classList.remove("is-active");

    let highlightedRows = document.querySelectorAll(".person-row.delete-mode");
    highlightedRows.forEach(row => {
        row.classList.remove("delete-mode");
    });
}