import {token} from "./auth.js";

export async function editTripMember(userId, tripId, newRole) {

    try {

        const response = await fetch(`http://localhost:8080/travelplanner/${tripId}/members/editMemberRole/${userId}`, {
            method: 'PUT',
            headers: {
                "Authorization" : `Bearer ${token}`,
                "Content-Type" : "application/json"
            },
            body: JSON.stringify(newRole)
        });

        if (!response.ok) {
            const errorData = await response.json();
            alert(errorData.message || errorData.error || "An unexpected error occurred");
            return;
        }

        return response.ok;

    } catch (error) {
        console.log("Error editing member's role", error);
    }
}