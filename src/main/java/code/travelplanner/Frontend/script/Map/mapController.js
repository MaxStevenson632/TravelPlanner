let mapInstance = null;

document.addEventListener('DOMContentLoaded', () => {

    mapInstance = createMap();

    const tripList = document.getElementById("tripList");

    if (tripList) {
        tripList.addEventListener("click", (event) => {

            const clickedTrip = event.target.closest("li");

            if (!clickedTrip || clickedTrip.classList.contains("tripListLoading")) {
                return;
            }

            const tripId = clickedTrip.dataset.tripId;

            loadTripMarkers(mapInstance, tripId);
        })
    }
})

async function loadTripMarkers(mapInstance, tripId) {

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

