import * as utils from "./utils.js";

export function renderTripWaypointsAndMembers(tripData) {

    document.getElementById("detailTripName").innerHTML = utils.sanitizedHTML(tripData.name);

    const waypointsContainer = document.getElementById("detailWaypointsList");
    const peopleContainer = document.getElementById("detailPeopleList");

    waypointsContainer.innerHTML = "";
    peopleContainer.innerHTML = "";

    // Render waypoints to sidebar
    if (tripData.waypoints && tripData.waypoints.length > 0) {
        tripData.waypoints.forEach((waypoint, index) => {
            const isLast = index === tripData.waypoints.length - 1;

            const waypointHTML =
                `<div class="waypoint-item" data-waypoint-id = "${waypoint.waypointId}">
                <div class="waypoint-marker">
                    <div class="waypoint-dot"></div>
                    ${!isLast ? `<div class="waypoint-line"></div>` : ''}
                </div>
                <div class="waypoint-copy">
                    <h4 class="waypoint-name">${utils.sanitizedHTML(waypoint.placeName)}</h4>
                </div>
            </div>
            `;

            waypointsContainer.insertAdjacentHTML('beforeend', waypointHTML);
        });
    } else {
        waypointsContainer.innerHTML = "<p class= 'waypoint-note'> No waypoints added yet. </p>";
    }

    // Render members to sidebar
    if (tripData.members && tripData.members.length > 0) {
        tripData.members.forEach(member => {

            let initials = '??';
            if (member.name != null) {
                initials = utils.getInitials(member.name);
            }

            // If member role = OWNER
            const isOwner = member.role === 'OWNER';

            const personHTML =`
                <div class="person-row" data-user-id = "${member.id}"  data-user-role = "${member.role}">
                    <div class="person-initials">${utils.sanitizedHTML(initials)}</div>
                    <div class="person-copy">
                        <span class="person-name">${utils.sanitizedHTML(member.name)}</span>
                        ${isOwner
                            ? `<span class="person-role">${utils.sanitizedHTML(member.role)}</span>`
                            : `<button class="person-role">${utils.sanitizedHTML(member.role)}</button>`
                        }
                    </div>
                </div>
            `;

            peopleContainer.insertAdjacentHTML('beforeend', personHTML);
        });
    } else {
        peopleContainer.innerHTML = "<p class 'person-role'> No members found. </p>";
    }
}


export function showTripListPanel() {

    // Remove any trip details panel section
    document.getElementById("tripDetailPanel").classList.remove("is-visible");
    document.getElementById("usernameHeader").classList.remove("hidden");
    document.querySelector(".tripsHeading").classList.remove("hidden");
    document.getElementById("tripList").classList.remove("hidden");
    document.getElementById("deleteTripBtn").classList.remove("hidden");

    document.querySelectorAll('.tripListItem.active').forEach(item => {
        item.classList.remove('active');
    });
}

export function showTripDetailsPanel(clickedItem) {

    const tripListContainer = document.getElementById("tripList");
    const tripDetailsPanel = document.getElementById("tripDetailPanel");

    document.querySelectorAll('.tripListItem.active').forEach(item => {
        item.classList.remove('active');
    });
    clickedItem.classList.add('active');

    document.getElementById("usernameHeader").classList.add("hidden");
    document.querySelector(".tripsHeading").classList.add("hidden");
    document.getElementById("deleteTripBtn").classList.add("hidden");
    tripListContainer.classList.add("hidden");
    tripDetailsPanel.classList.add("is-visible");
}

export function switchWaypointAndMemberTab(event) {

    document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('is-active'));
    document.querySelectorAll('.detail-view').forEach(view => view.classList.remove('is-visible'));

    // Make the button user clicked active
    const clickedTab = event.currentTarget;
    clickedTab.classList.add('is-active');

    // Display the tab's data (either members or waypoints)
    const targetViewId = clickedTab.dataset.target;
    document.getElementById(targetViewId).classList.add('is-visible');
}