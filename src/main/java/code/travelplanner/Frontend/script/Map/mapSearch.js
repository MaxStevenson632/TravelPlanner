import { token } from '../auth.js';
import {mapInstance} from './mapController.js';
import {sanitizedHTML} from "../utils.js";
import {renderTripWaypointsAndMembers} from "../sidebarRenderer.js";


let currentMarker = null;
let selectedCoordinates = null;
export let selectedPlace = null;


export async function fetchPlaces(query, placeInput) {

    try {
        const response = await fetch(

            // Limit response to 4 results
            `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(query)}&limit=4`,
            {
                headers: {

                    // Required by Nominatim policy
                    'User-Agent': 'TravelPlannerApp/1.0 (github.com/MaxStevenson632)'
                }
            }
        );

        const places = await response.json();
        renderSuggestions(places);

    } catch (error) {
        console.error("Geocoding failed", error);
    }
}

function renderSuggestions(places) {

    const suggestionsList = document.getElementById('suggestionsList');
    const placeInput = document.getElementById('placeInput');
    suggestionsList.innerHTML = '';

    places.forEach(place => {

        // Display place on UI
        const li = document.createElement('li');
        li.textContent = place.display_name;

        // If user clicks on a place
        li.addEventListener('click', () => {
            const latitude = parseFloat(place.lat);
            const longitude = parseFloat(place.lon);

            selectedCoordinates = { latitude: latitude, longitude: longitude };

            // Move map to marker/ place
            mapInstance.flyTo({ center: [longitude, latitude], zoom: 8});

            // If marker already exists
            if (currentMarker) {
                currentMarker.setLngLat([longitude, latitude]);
            } else {

                // Create marker
                currentMarker = new mapboxgl.Marker({ color: '#3b82f6' })
                    .setLngLat([longitude, latitude])
                    .addTo(mapInstance);
            }

            placeInput.value = place.display_name;
            suggestionsList.innerHTML = '';

            showAddToTripBtn(place, selectedPlace);
        });

        suggestionsList.appendChild(li);
    });
}

function showAddToTripBtn(place) {

    selectedPlace = place;
    const addToTripBtn = document.getElementById('addToTripBtn')
    addToTripBtn.classList.remove('hidden');
}