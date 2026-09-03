import { createMap } from './mapInit.js';
import { getMapData } from '../tripService.js';
import { addMarkers } from './mapMarkers.js';
import { renderRouteBetweenTwoPoints } from './mapNavigation.js';

export let mapInstance = null;

document.addEventListener('DOMContentLoaded', async() => {

    mapInstance = await createMap();
});

export function renderTripMarkersAndRouteOnMap(tripId) {

            loadTripMarkersAndRoute(mapInstance, tripId);
}

async function loadTripMarkersAndRoute(mapInstance, tripId) {

    const tripData = await getMapData(tripId);
    const waypointsList = tripData.waypoints;

    addMarkers(mapInstance, waypointsList);

    const waypoints = tripData.waypoints;

    for (let i = 0; i < (waypoints.length - 1); i++) {

        const currentPoint = waypoints[i];
        const nextPoint = waypoints[i + 1];

        renderRouteBetweenTwoPoints(currentPoint.longitude, currentPoint.latitude, nextPoint.longitude, nextPoint.latitude, mapInstance, `route-segment-${i}`);
    }
}

