
export function createMap() {

    const map = new mapboxgl.Map({
        accessToken: `${MAPBOX_APIKEY}`,
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
