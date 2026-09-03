import { token } from './auth.js';
import { getInitials } from './utils.js';

let searchTimeout = null;

export function openMemberSearch(tripId, onMemberAdded) {

    const overlay = document.getElementById("memberSearchOverlay");
    const input   = document.getElementById("memberSearchInput");

    // Show the overlay
    overlay.classList.remove("hidden");

    // Focus input immediately
    input.focus();

    attachListeners(tripId, onMemberAdded);
}

function closeOverlay() {

    const overlay = document.getElementById("memberSearchOverlay");
    const input = document.getElementById("memberSearchInput");
    const resultsList = document.getElementById("memberResultsList");

    overlay.classList.add("hidden");
    input.value = "";
    resultsList.innerHTML = "";

    clearTimeout(searchTimeout);
}

function attachListeners(tripId, onMemberAdded) {

    const input = document.getElementById("memberSearchInput");
    const cancelBtn = document.getElementById("memberCancelBtn");
    const resultsList = document.getElementById("memberResultsList");

    // Cancel button
    // Remove previous listener using cloneNode
    const freshCancel = cancelBtn.cloneNode(true);
    cancelBtn.replaceWith(freshCancel);
    freshCancel.addEventListener("click", closeOverlay);

    // Input
    const freshInput = input.cloneNode(true);
    input.replaceWith(freshInput);
    freshInput.focus();

    freshInput.addEventListener("input", (event) => {

        const query = event.target.value.trim();

        clearTimeout(searchTimeout);
        resultsList.innerHTML = "";

        // Wait until at least 3 characters entered before searching for user
        if (query.length < 3) {
            return;
        }

        // Wait 300ms after last character entered
        searchTimeout = setTimeout(() => {
            searchUsers(query, resultsList, tripId, onMemberAdded);
        }, 300);
    });
}

async function searchUsers(query, resultsList, tripId, onMemberAdded) {

    resultsList.innerHTML = '<li class = "member-result-loading"> Searching... </li>';

    try {
        const response = await fetch(`http://localhost:8080/travelplanner/users/${tripId}/search?query=${query}`,
            {
                method: "GET",
                headers: {
                    "Authorization" : `Bearer ${token}`,
                    "Content-Type" : "application/json"
                    }
                }
        );

        if (response.status === 429) {
            const errorData = await response.json();
            alert(errorData.message || errorData.error || "An unexpected error occurred");
            return;
        }

        if (!response.ok) {
            resultsList.innerHTML = '<li class = "member-result-error"> Search failed. Try again. </li>';
            return;
        }

        const users = await response.json();
        renderResults(users, resultsList, tripId, onMemberAdded);
    } catch (error) {
        console.error("Member search error", error);
        resultsList.innerHTML = '<li class="member-result-error">Something went wrong.</li>';
    }
}

function renderResults(users, resultsList, tripId, onMemberAdded) {

    resultsList.innerHTML = "";

    if (users.length === 0) {
        resultsList.innerHTML = '<li class = "member-result-empty"> No users found. </li>';
        return;
    }

    users.forEach(user => {
        const li = document.createElement("li");
        li.className = "member-result-item";

        // Initials avatar
        const initialsDiv = document.createElement("div");
        initialsDiv.className   = "member-result-initials";
        initialsDiv.textContent = getInitials(user.name);

        // Name + email
        const copyDiv = document.createElement("div");
        copyDiv.className = "member-result-copy";

        const nameSpan = document.createElement("span");
        nameSpan.className   = "member-result-name";
        nameSpan.textContent = user.name;

        const emailSpan = document.createElement("span");
        emailSpan.className   = "member-result-email";
        emailSpan.textContent = user.email;

        copyDiv.appendChild(nameSpan);
        copyDiv.appendChild(emailSpan);

        // Info wrapper
        const infoDiv = document.createElement("div");
        infoDiv.className = "member-result-info";
        infoDiv.appendChild(initialsDiv);
        infoDiv.appendChild(copyDiv);

        // Displaying the buttons
        const actionsDiv = document.createElement("div");
        actionsDiv.className = "member-result-actions";

        // Add 'Add Member' button
        const addMemberBtn = document.createElement("button");
        addMemberBtn.className   = "member-role-btn member-role-btn--member";
        addMemberBtn.setAttribute("aria-label", `Add ${user.name}`);
        addMemberBtn.innerHTML = `
        <span class = "icon"> + </span>
        <span class = "text"> Add Member</span>
        `;
        addMemberBtn.addEventListener("click", () => addPerson(tripId, user.userId, "MEMBER", onMemberAdded));

        // Add 'Add Viewer' button
        const addViewerBtn = document.createElement("button");
        addViewerBtn.className = "member-role-btn member-role-btn--viewer";
        addViewerBtn.setAttribute("aria-label", `add ${user.name}`);
        addViewerBtn.innerHTML = `
        <span class = "icon"> + </span>
        <span class = "text"> Add Viewer </span>
        `;

        addViewerBtn.addEventListener("click", () => addPerson(tripId, user.userId, "VIEWER", onMemberAdded));


        actionsDiv.appendChild(addMemberBtn);
        actionsDiv.appendChild(addViewerBtn);

        li.appendChild(infoDiv);
        li.appendChild(actionsDiv);

        resultsList.appendChild(li);
    });
}

async function addPerson(tripId, userId, role, onMemberAdded) {

    try {
        const response = await fetch( `http://localhost:8080/travelplanner/${tripId}/members/addMember/${userId}`, {
            method: "POST",
            headers: {
                "Authorization" : `Bearer ${token}`,
                "Content-Type" : "application/json"
                },
            // Send role to backend using request body
            body: JSON.stringify({ role })
            }
        );

        if (!response.ok) {
            const error = await response.json();
            alert(error.message || "Could not add member. Try again.");
            return;
        }

        // Refresh people list
        onMemberAdded();

        // Close overlay
        closeOverlay();

    } catch (error) {
        console.error("Add member error", error);
        alert("Something went wrong. Try again.");
    }
}
