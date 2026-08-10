function addMarkers(map, waypointsList) {

    if (waypointsList.length === 0) {
        console.log("No waypoints found to add to map");
        return;
    }
    waypointsList.forEach(waypoint => {
        const {longitude, latitude, placeName} = waypoint;

        new mapboxgl.Marker()
            .setLngLat([parseFloat(longitude), parseFloat(latitude)])
            .addTo(map);
    });
}