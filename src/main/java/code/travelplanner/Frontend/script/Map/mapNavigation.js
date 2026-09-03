import {token} from "../auth.js";

export async function renderRouteBetweenTwoPoints(longitudeA, latitudeA, longitudeB, latitudeB, map, routeSegmentId) {

    try {
        const response = await fetch(`http://localhost:8080/travelplanner/map/getRoute?longitudeA=${longitudeA}
        &latitudeA=${latitudeA}&longitudeB=${longitudeB}&latitudeB=${latitudeB}`, {

            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            const errorData = await response.json();
            alert(errorData.message || errorData.error || "An unexpected error occurred");
            return;
        }

        const data = await response.json();

        if (!data.routes || data.routes.length === 0) {
            console.error("No route found");
            return;
        }

        const route = data.routes[0];

        // Call helper function to return duration of segment in hrs and mins format - duration returned from api in seconds
        const formattedDuration = formatDuration(route.duration);

        // Draw line on Mapbox
        const routeGeoJSON = {
            'type' : 'Feature',
            'properties' : {},
            'geometry' : route.geometry
        }

        // If segment layer exists already, update it
        if (map.getSource(routeSegmentId)) {
            map.getSource(routeSegmentId).setData(routeGeoJSON);
        } else {

            // Add new GeoJSON source for line
            map.addSource(routeSegmentId, {
                'type' : 'geojson',
                'data' : routeGeoJSON
            });

            // Add visual layer to render the route
            map.addLayer({
                'id' : routeSegmentId,
                'type' : 'line',
                'source' : routeSegmentId,
                'layout' : {
                    'line-join' : 'round',
                    'line-cap' : 'round'
                },
                'paint' : {
                    'line-color': '#f1c56b',
                    'line-width' : 5,
                    'line-opacity': 0.85
                }
            });
        }

        // Display duration in middle of the segment line
        const midIndex = Math.floor(route.geometry.coordinates.length/2);
        const midPoint = route.geometry.coordinates[midIndex];

        new mapboxgl.Popup({ closeButton : false, closeOnClick : false })
            .setLngLat(midPoint)
            .setHTML(`<div class = "segmentDurationMapTag"> ${formattedDuration} </div>`)
            .addTo(map);


        return formattedDuration;
    } catch (error) {
        console.error("Error fetching directions", error);
    }
}

function formatDuration(seconds) {

    const mins = Math.round(seconds/60);
    const hours = Math.floor(mins/60);
    const remainingMins = mins % 60;

    if (hours > 0) {
        return `${hours}hrs ${remainingMins}mins`;
    } else {
        return `${remainingMins}mins`;
    }
}
