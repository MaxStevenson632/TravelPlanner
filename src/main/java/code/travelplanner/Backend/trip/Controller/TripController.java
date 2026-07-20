package code.travelplanner.Backend.trip.Controller;

import code.travelplanner.Backend.trip.Dto.NewTripDto;
import code.travelplanner.Backend.trip.Service.TripService;
import code.travelplanner.Backend.user.Entity.UserEntity;
import code.travelplanner.Backend.user.Repository.UserRepository;
import code.travelplanner.Backend.waypoint.Service.WaypointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/travelplanner")
@Controller
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
public class TripController {

    private final TripService tripService;
    private final WaypointService waypointService;
    private final UserRepository userRepository;

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
}
