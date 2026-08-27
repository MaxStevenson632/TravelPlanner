package code.travelplanner.Backend.trip.Controller;

import code.travelplanner.Backend.trip.Service.TripPlacesService;
import code.travelplanner.Backend.waypoint.Dto.WaypointMapDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/travelplanner/{tripId}")
@Controller
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
public class TripPlacesController {

    private final TripPlacesService tripPlacesService;

    public TripPlacesController(TripPlacesService tripPlacesService) {
        this.tripPlacesService = tripPlacesService;
    }

    @PostMapping("/addWaypoint")
    public ResponseEntity<?> addWaypointToTrip(@RequestBody WaypointMapDto body,
                                               @PathVariable Long tripId, @AuthenticationPrincipal Long requesterId) {

        tripPlacesService.addWaypointToTrip(tripId, body, requesterId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteLinkedWaypoint/{waypointId}")
    public ResponseEntity<?> deleteWaypointFromTrip(@PathVariable Long tripId,
                                                    @PathVariable Long waypointId, @AuthenticationPrincipal Long requesterId) {

        tripPlacesService.deleteWaypoint(tripId, waypointId, requesterId);
        return  ResponseEntity.ok().build();
    }
}
