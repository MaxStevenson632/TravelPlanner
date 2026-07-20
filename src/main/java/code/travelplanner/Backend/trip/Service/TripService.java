package code.travelplanner.Backend.trip.Service;

import code.travelplanner.Backend.trip.Dto.NewTripDto;
import code.travelplanner.Backend.trip.Entity.TripEntity;
import code.travelplanner.Backend.trip.Repository.TripRepository;
import code.travelplanner.Backend.tripMembers.Service.TripMembersService;
import code.travelplanner.Backend.user.Entity.UserEntity;
import code.travelplanner.Backend.user.Repository.UserRepository;
import code.travelplanner.Backend.waypoint.Service.WaypointService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final TripMembersService tripMembersService;
    private final UserRepository userRepository;
    private final WaypointService waypointService;

    public TripService(TripRepository tripRepository,  TripMembersService tripMembersService,  UserRepository userRepository,
                       WaypointService waypointService) {
        this.tripRepository = tripRepository;
        this.tripMembersService = tripMembersService;
        this.userRepository = userRepository;
        this.waypointService = waypointService;
    }

    @Transactional
    public ResponseEntity<?> createTrip (NewTripDto newTripData, Long userId) {

        TripEntity tripEntity = new TripEntity(newTripData.getTitle(),
                newTripData.getStartDate(), newTripData.getEndDate());
        tripRepository.save(tripEntity);
        tripMembersService.createOwner(tripEntity.getTripId(), userId);
        waypointService.addWaypoint(newTripData.getWaypoints(), tripEntity.getTripId());

        return  ResponseEntity.ok().build();
    }
}
