package code.travelplanner.Backend.waypoint.Controller;

import code.travelplanner.Backend.waypoint.Dto.GeocodingResponse;
import code.travelplanner.Backend.waypoint.Service.GeocodingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/travelplanner")
@RestController
public class WaypointController {

    private final GeocodingService geocodingService;

    public  WaypointController(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @GetMapping("/waypoint/search")
    public List<GeocodingResponse> GetTopFourPlaces(@RequestParam String query) {

        List<GeocodingResponse> places = geocodingService.searchFourPlaces(query);
        return places;
    }

}
