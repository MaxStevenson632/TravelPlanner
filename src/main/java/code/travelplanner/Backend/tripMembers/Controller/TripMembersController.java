package code.travelplanner.Backend.tripMembers.Controller;

import code.travelplanner.Backend.tripMembers.Service.TripMembersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/travelplanner/{tripId}/members")
@Controller
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
public class TripMembersController {

    private final TripMembersService tripMembersService;

    public  TripMembersController(TripMembersService tripMembersService) {
        this.tripMembersService = tripMembersService;
    }

    @PostMapping("/addMember/{memberToAddId}")
    public ResponseEntity<?> addMember(@PathVariable Long tripId,
                                       @PathVariable Long memberToAddId,
                                       @RequestBody Map<String, String> body,
                                       @AuthenticationPrincipal Long requesterId) {

        // body contains "role" : "${Whatever the role is}"
        // We need just the role
        String role = body.get("role");
        tripMembersService.addPersonToTrip(tripId, requesterId, role, memberToAddId);
        return ResponseEntity.ok().build();
    }
}
