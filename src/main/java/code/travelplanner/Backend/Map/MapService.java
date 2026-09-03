package code.travelplanner.Backend.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MapService {

    @Value("${mapbox.api.token}")
    private String mapboxKey;
    private final RestTemplate mapboxRestTemplate = new RestTemplate();

    public String getMapToken() {

        return mapboxKey;
    }

    public String getDirections(double longitudeA, double latitudeA, double longitudeB, double latitudeB) {

        try {

            // Clean token, replace unwanted characters with blank
            String cleanToken = mapboxKey.replaceAll("[\"';\\s]", "").trim();

            // Get directions and route in JSON
            String mapBoxUrl = UriComponentsBuilder.fromUriString("https://api.mapbox.com/directions/v5/mapbox/driving/")
                    .path(longitudeA + "," + latitudeA + ";" + longitudeB + "," + latitudeB)
                    .queryParam("geometries", "geojson")
                    .queryParam("access_token", cleanToken)
                    .toUriString();

            return mapboxRestTemplate.getForObject(mapBoxUrl, String.class);

        } catch (Exception e) {
            System.err.println("Unexpected error fetching route: " + e.getMessage());
            return null;
        }
    }
}
