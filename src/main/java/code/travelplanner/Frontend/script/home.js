import * as sidebarRenderer from './sidebarRenderer.js';
import * as tripService from './tripService.js';
import { renderTripMarkersAndRouteOnMap } from './Map/mapController.js';
import { getAccountNameFromToken } from "./auth.js";
import { sanitizedHTML } from "./utils.js";
import {renderTripWaypointsAndMembers} from "./sidebarRenderer.js";

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
});

const tripListContainer = document.getElementById("tripList");

// User clicks on a trip on the sidebar
tripListContainer.addEventListener('click', async (event) => {

    const clickedItem = event.target.closest('.tripListItem');
    if (!clickedItem) {
        return;
    }

    const tripId = clickedItem.dataset.tripId;
    if (!tripId) {
        return;
    }

    sidebarRenderer.showTripDetailsPanel(clickedItem);

    const tripData = await tripService.getMapData(tripId);

    renderTripMarkersAndRouteOnMap(tripId);
    sidebarRenderer.renderTripWaypointsAndMembers(tripData);
});

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




