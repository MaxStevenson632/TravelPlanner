import {token} from "../auth.js";

export async function createMap() {

    // Get mapbox api key
    const rawToken = await getMapboxToken();

    // Clean it, replacing any extra characters with blank
    const cleanToken = rawToken.replace(/["';\s]/g, '');

    // Initiate map
    const map = new mapboxgl.Map({
        accessToken: cleanToken,
        container: 'map',
        style: 'mapbox://styles/mapbox/standard', // Use the standard style for the map
        projection: 'equirectangular', // display the map as a rectangle
        zoom: 1, // initial zoom level, 0 is the world view, higher values zoom in
        center: [30, 15] // center the map on this longitude and latitude
    });

    map.addControl(new mapboxgl.NavigationControl());
    map.scrollZoom.disable();

    map.on('style.load', () => {
        map.setFog({}); // Set the default atmosphere style
    });

    return map;
}

async function getMapboxToken() {

    // Get mapbox token from backend
    const response = await fetch('http://localhost:8080/travelplanner/map/getMapToken', {

        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        console.error("Error retrieving mapbox token");
        const errorData = await response.json();
        alert(errorData.message || errorData.error || "An unexpected error occurred");
        return;
    }

    // Return mapbox api token
    const mapboxToken = await response.text();
    return mapboxToken;
}

