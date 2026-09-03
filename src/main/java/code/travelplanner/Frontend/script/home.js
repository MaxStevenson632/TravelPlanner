import * as sidebarRenderer from './sidebarRenderer.js';
import * as tripService from './tripService.js';
import { renderTripMarkersAndRouteOnMap } from './Map/mapController.js';
import { getAccountNameFromToken } from "./auth.js";
import { getUserIdFromToken } from "./auth.js";
import { sanitizedHTML } from "./utils.js";
import { renderTripWaypointsAndMembers } from "./sidebarRenderer.js";
import { openMemberSearch } from './memberSearch.js';
import * as deleteMember from './deleteMember.js';
import { editTripMember } from "./editMember.js";
import {fetchPlaces} from './Map/mapSearch.js';
import { handleReorderKeydown, displayWaypointForOrdering } from "./addWaypoint.js";
import {waypointDeleteMode, exitWaypointDeleteMode, deleteWaypoint} from "./deleteWaypoint.js";
import {deleteTripMode, exitTripDeleteMode, deleteTrip} from "./deleteTrip.js";


document.addEventListener("DOMContentLoaded", () => {

    // Add name to trip list panel
    const usernameHeader = document.getElementById("usernameHeader");
    const h1 = document.createElement('h1');
    h1.innerHTML = sanitizedHTML(getAccountNameFromToken());
    usernameHeader.appendChild(h1);

    sidebarRenderer.showTripListPanel();
    tripService.loadUserTripsAndName();
});

const tripForm = document.getElementById("tripForm");

// User has clicked 'Create Trip', send details to backend
tripForm.addEventListener("submit", async (event) => {
    await tripService.createTrip(event);
});

// User switches between people and waypoints tab
document.querySelectorAll('.tab-button').forEach(button => {
    button.addEventListener('click', (event) => {
        sidebarRenderer.switchWaypointAndMemberTab(event)
    });
});

const backToTripsBtn = document.getElementById("backToTripsBtn");

// User de-selects the trip, go back to list of trips
backToTripsBtn.addEventListener("click", () => {
    sidebarRenderer.showTripListPanel();
    tripId = null;
});

const tripListContainer = document.getElementById("tripList");
let tripId = null;
let tripData = null;

// User clicks on a trip on the sidebar
tripListContainer.addEventListener('click', async (event) => {

    const clickedItem = event.target.closest('.tripListItem');
    if (!clickedItem) {
        return;
    }

    tripId = clickedItem.dataset.tripId;
    if (!tripId) {
        return;
    }

    if (deleteMode) {
        return;
    }

    sidebarRenderer.showTripDetailsPanel(clickedItem);

    tripData = await tripService.getMapData(tripId);

    renderTripMarkersAndRouteOnMap(tripId);
    sidebarRenderer.renderTripWaypointsAndMembers(tripData);

    const userId = getUserIdFromToken();
    let ownerId = null;

    // Get id of owner
    tripData.members.forEach(member => {
        if (member.role === 'OWNER') {
            ownerId = member.id;
        }
    })

    // Delete member button - Owner only
    const deleteButton = document.getElementById('deleteMemberBtn');

    // If user is owner of trip, allow them to see owner-only features
    if (userId === ownerId) {
        deleteButton.classList.remove('hidden');
    } else {
        deleteButton.classList.add('hidden');
    }
});

const placeInput = document.getElementById('placeInput');
const suggestionsList = document.getElementById('suggestionsList');
let debounceTimer = null;

// User searches for a place
placeInput.addEventListener('input', (event) => {

    if (tripId === null) {
        console.log("Must select a trip");
        return;
    }

    // Reset timeout
    clearTimeout(debounceTimer);
    const query = event.target.value.trim();

    // Must have more than 3 characters entered
    if (query.length < 3) {
        suggestionsList.innerHTML = '';
        return;
    }

    // Wait 1000ms per keystroke to stay within Nominatim's 1 req/sec policy
    debounceTimer = setTimeout(() => fetchPlaces(query, placeInput), 600);
});

// User clicks to add waypoint to trip
document.getElementById('addToTripBtn').addEventListener('click', () => {

    displayWaypointForOrdering(tripId);

    // Remove focus on search bar
    document.getElementById('placeInput').blur();

    // Start handling the keydown events
    document.addEventListener('keydown', handleReorderKeydown);
})

const AddTripBtn = document.getElementById("addTripBtn");
const tripFormContainer = document.getElementById("tripFormContainer");

// User clicked 'Create Trip' button
AddTripBtn.addEventListener("click", () => {
    tripFormContainer.classList.remove("hidden");
});

const cancelTripBtn = document.getElementById("cancelTripBtn");

// User clicks 'Cancel Trip'
cancelTripBtn.addEventListener("click", () => {
    tripFormContainer.classList.add("hidden");
    tripForm.reset();
});

document.getElementById("addPersonBtn").addEventListener("click", () => {

    openMemberSearch(tripId, () => {

        // After member added, refresh list of members and waypoints
        renderTripWaypointsAndMembers(tripData);
    })
})

let deleteMode = false;
// Owner clicks on the delete member button
document.getElementById("deleteMemberBtn").addEventListener("click", () => {

    if (deleteMode) {
        deleteMode = false;
        deleteMember.exitDeleteMode();
    } else {
        deleteMember.deleteMode();
        deleteMode = true;
    }
});

// Owner clicks on the delete waypoint button
document.getElementById("deleteWaypointBtn").addEventListener("click", () => {

    if (deleteMode) {
        deleteMode = false;
        exitWaypointDeleteMode();
    } else {
        waypointDeleteMode();
        deleteMode = true;
    }
});

// Owner clicks on a member to delete them
document.getElementById("detailPeopleList").addEventListener("click", async(event) => {

    // Must be in delete mode
    if (!deleteMode) {
        return;
    }

    const row = event.target.closest(".person-row.delete-mode");
    const memberId = row.dataset.userId;

    // Remove row before api call - optimistic approach
    row.remove();

    // Remove member from trip in DB
    const response = await deleteMember.deleteMember(memberId, tripId);

    // Re-render the people list - if successful, member removed and if not member re-appears
    await tripService.getMapData(tripId);

    deleteMember.exitDeleteMode();
});

// Owner clicks on the delete trip button
document.getElementById("deleteTripBtn").addEventListener("click", () => {

    if (deleteMode) {
        deleteMode = false;
        exitTripDeleteMode();

    } else {
        deleteTripMode();
        deleteMode = true;
    }
});

// Owner clicks on a trip to delete them
document.getElementById("tripList").addEventListener("click", async(event) => {

    // Must be in delete mode
    if (!deleteMode) {
        return;
    }

    const row = event.target.closest(".tripListItem.delete-mode");
    const memberId = row.dataset.tripId;

    // Remove row before api call - optimistic approach
    row.remove();

    // delete trip in DB
    const response = await deleteTrip(memberId, tripId);

    // Re-render the people list - if successful, trip removed and if not trip re-appears
    await tripService.getMapData(tripId);

    deleteMember.exitDeleteMode();
});

// User clicks on another user's role badge to edit role
document.getElementById("detailPeopleList").addEventListener("click", async(event) => {

    if (deleteMode) {
        return;
    }

    const roleBadge = event.target.closest(".person-role");

    const row = roleBadge.closest(".person-row");
    const memberId = row.dataset.userId;
    const currentRole = row.dataset.userRole;

    let newRole = null;

    if (currentRole === "MEMBER") {
        newRole = "VIEWER";
    } else if (currentRole === "VIEWER") {
        newRole = "MEMBER";
    }

    let response = null;

    if (newRole != null) {
        response = await editTripMember(memberId, tripId, newRole);
    } else {
        console.log("Cannot change your own role");
        return;
    }

    if (response) {
        roleBadge.textContent = newRole;
        roleBadge.dataset.role = newRole;
    } else {
        console.log("Error switching user's role");
    }
});

// User clicks on a waypoint to remove it
document.getElementById('detailWaypointsList').addEventListener('click', async (event) => {

    if (!deleteMode) {
        return;
    }

    const waypointRow = event.target.closest('.waypoint-item');
    if (!waypointRow) {
        return;
    }

    const waypointId = waypointRow.dataset.waypointId;
    console.log(waypointId);
    const response = await deleteWaypoint(tripId, waypointId);

    if (response) {
        waypointRow.remove();
        await tripService.getMapData(tripId);
        exitWaypointDeleteMode();
    }
});

function toggleOwnerControls(isOwner) {
    const ownerControls = document.querySelectorAll(".owner-only");
    ownerControls.forEach(el => {
        el.classList.toggle("hidden", !isOwner);
    });
}


