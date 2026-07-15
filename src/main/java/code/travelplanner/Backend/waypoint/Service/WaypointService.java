package code.travelplanner.Backend.waypoint.Service;

import code.travelplanner.Backend.trip.Repository.TripPlacesRepository;
import code.travelplanner.Backend.trip.Service.TripPlacesService;
import code.travelplanner.Backend.waypoint.Dto.GeocodingResponse;
import code.travelplanner.Backend.waypoint.Entity.WaypointEntity;
import code.travelplanner.Backend.waypoint.Repository.WaypointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaypointService {

    private final WaypointRepository waypointRepository;
    private final GeocodingService geocodingService;
    private final TripPlacesService tripPlacesService;
    private final TripPlacesRepository tripPlacesRepository;

    public WaypointService(WaypointRepository waypointRepository, GeocodingService geocodingService,
                           TripPlacesService tripPlacesService, TripPlacesRepository tripPlacesRepository) {
        this.waypointRepository = waypointRepository;
        this.geocodingService = geocodingService;
        this.tripPlacesService = tripPlacesService;
        this.tripPlacesRepository = tripPlacesRepository;
    }

    public void addWaypoint(List<String> waypoints, long tripId) {

        double latitude = 0;
        double longitude = 0;
        WaypointEntity waypointEntity = null;
        int visitOrder = 1;
        // CHECK ALREADY EXISTS
        for (String waypoint : waypoints) {

            // Name already exists in DB, retrieve coordinates from DB
            if (waypointRepository.findByPlaceName(waypoint).isPresent()) {
                latitude = waypointRepository.findByPlaceName(waypoint).get().getLatitude();
                longitude = waypointRepository.findByPlaceName(waypoint).get().getLongitude();
                waypointEntity = waypointRepository.findByPlaceName(waypoint).get();

            // Name doesn't already exist, call Nominatim api to get coordinates and save to DB
            } else {
                GeocodingResponse coordinates = geocodingService.getCoordinates(waypoint);
                latitude = coordinates.getLatitude();
                longitude = coordinates.getLongitude();
                waypointEntity = new WaypointEntity(latitude, longitude, waypoint);
                waypointRepository.save(waypointEntity);
            }

            tripPlacesService.linkWaypointToTrip(tripId, waypointEntity.getWaypointId(), visitOrder);
            visitOrder += 1;
        }
    }
}
