package code.travelplanner.Backend.waypoint.Service;

import code.travelplanner.Backend.Exception.PlaceNotFoundException;
import code.travelplanner.Backend.waypoint.Dto.GeocodingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeocodingService{

    private final RestTemplate restTemplate;
    // Nominatim - only 1 call per second, 1.1 seconds for a buffer
    private final long RATE_LIMIT = 1100;
    private long lastRequestTime = 0;

    @Value("${nominatim.api.geo-url}")
    private String geoUrl;

    public GeocodingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GeocodingResponse getCoordinates(String placeName) {

        enforceRateLimit();

        String url = UriComponentsBuilder.fromUriString(geoUrl)
                .queryParam("q", placeName)
                .queryParam("format", "json")
                .queryParam("limit", 1)
                .toUriString();

        // Return list of GeocodingResponse objects
        GeocodingResponse[] response = restTemplate.getForObject(url, GeocodingResponse[].class);

        if (response == null || response.length == 0) {
            throw new PlaceNotFoundException("Could not geocode: " + placeName);
        }

        // return the first object
        return response[0];
    }

    // Temporary rate limiting - breaks if multiple servers/ threads, doesn't rate limit users either
    // Synchronized = only one thread
    private synchronized void enforceRateLimit() {

        long now = System.currentTimeMillis();
        long timeSinceLast = now - lastRequestTime;

        // Not been 1100 seconds yet
        if (timeSinceLast < RATE_LIMIT) {
            try {
                Thread.sleep(RATE_LIMIT - timeSinceLast);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        lastRequestTime = System.currentTimeMillis();
    }
}
