package code.travelplanner.Backend.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/travelplanner/map")
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
public class MapController {

    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/getMapToken")
    public ResponseEntity<String> getMapConfiguration() {

        return ResponseEntity.ok(mapService.getMapToken());
    }

    @GetMapping("/getRoute")
    public ResponseEntity<?> getRoute(@RequestParam double longitudeA, @RequestParam double latitudeA,
                                      @RequestParam double longitudeB, @RequestParam double latitudeB) {

        String routeServiceResponse = mapService.getDirections(longitudeA, latitudeA, longitudeB, latitudeB);

        // Check response is not null
        if (routeServiceResponse == null || routeServiceResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to retrieve route from Mapbox.");
        }

        return ResponseEntity.ok(routeServiceResponse);
    }
}
