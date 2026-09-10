package code.travelplanner.Backend.trip.Controller;

import code.travelplanner.Backend.trip.Dto.NewTripDto;
import code.travelplanner.Backend.trip.Dto.TripMapDto;
import code.travelplanner.Backend.trip.Dto.TripOverviewsDto;
import code.travelplanner.Backend.trip.Dto.TripOverviewsListDto;
import code.travelplanner.Backend.trip.Service.TripService;
import code.travelplanner.Backend.user.Repository.UserRepository;
import code.travelplanner.Backend.waypoint.Service.WaypointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

@RequestMapping("/travelplanner")
@Controller
public class TripController {

    private final TripService tripService;
    private final WaypointService waypointService;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(TripController.class);

    @Autowired
    public TripController(TripService tripService,  WaypointService waypointService,  UserRepository userRepository) {
        this.tripService = tripService;
        this.waypointService = waypointService;
        this.userRepository = userRepository;
    }

    @PostMapping("/createTrip")
    public ResponseEntity<?> addNewTrip(@RequestBody NewTripDto newTripData, @AuthenticationPrincipal Long userId) {

        // loggedInUser populated automatically by Spring
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in.");
        }

        tripService.createTrip(newTripData, userId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{tripId}/map-data")
    public ResponseEntity<TripMapDto> getWaypointData(@PathVariable Long tripId, @AuthenticationPrincipal Long userId) {

        return ResponseEntity.ok(tripService.getTripMapData(tripId, userId));
    }

    @GetMapping("/retrieve-trips")
    public ResponseEntity<TripOverviewsListDto> getTripData(@AuthenticationPrincipal long userId) {

        long start = System.currentTimeMillis();

        TripOverviewsListDto trips = tripService.getTripOverviewsData(userId);

        long duration = System.currentTimeMillis() - start;
        log.info("RESPONSE — retrieve-trips userId={} took {}ms", userId, duration);
        
        return ResponseEntity.ok(trips);
    }

    @DeleteMapping("/{tripId}/deleteTrip")
    public ResponseEntity<?> deleteTrip(@AuthenticationPrincipal Long requesterId, @PathVariable Long tripId) {

        tripService.deleteTrip(requesterId, tripId);
        return ResponseEntity.ok().build();
    }
}
