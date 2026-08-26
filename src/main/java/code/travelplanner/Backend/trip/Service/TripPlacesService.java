package code.travelplanner.Backend.trip.Service;

import code.travelplanner.Backend.trip.Entity.TripPlacesEntity;
import code.travelplanner.Backend.trip.Repository.TripPlacesRepository;
import code.travelplanner.Backend.tripMembers.Entity.Role;
import code.travelplanner.Backend.tripMembers.Entity.TripMembersEntity;
import code.travelplanner.Backend.tripMembers.Repository.TripMembersRepository;
import code.travelplanner.Backend.waypoint.Dto.WaypointLinkToPlacesDto;
import code.travelplanner.Backend.waypoint.Dto.WaypointMapDto;
import code.travelplanner.Backend.waypoint.Service.WaypointService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TripPlacesService {

    private final TripPlacesRepository tripPlacesRepository;
    private final WaypointService waypointService;
    private final TripMembersRepository tripMembersRepository;

    public TripPlacesService(TripPlacesRepository tripPlacesRepository
    , WaypointService waypointService,  TripMembersRepository tripMembersRepository) {
        this.tripPlacesRepository = tripPlacesRepository;
        this.waypointService = waypointService;
        this.tripMembersRepository = tripMembersRepository;
    }

    public void linkWaypointToTrip(long tripId, long waypointId, int visitOrder) {

        tripPlacesRepository.save(new TripPlacesEntity(tripId, waypointId, visitOrder));
    }

    @Transactional
    public void addWaypointToTrip(long tripId, WaypointMapDto waypointDto, Long requesterId) {

        TripMembersEntity tripMember = tripMembersRepository.findByIdTripIdAndIdUserId(tripId, requesterId)
                .orElseThrow(() -> new IllegalArgumentException("Error matching user with trip"));

        // Verify user has permission to add a waypoint
        if (!(tripMember.getMemberRole().equals(Role.OWNER) || tripMember.getMemberRole().equals(Role.MEMBER))) {
            throw new IllegalArgumentException("User does not have permission to add member");
        }

        // Increment visit order of waypoints
        tripPlacesRepository.incrementOrders(tripId, waypointDto.getVisitOrder());

        // Add waypoint to trip
        WaypointLinkToPlacesDto waypointLinkDto = waypointService.addNewWaypoints(waypointDto, tripId);

        tripPlacesRepository.save(new TripPlacesEntity(tripId, waypointLinkDto.getWaypointId(), waypointDto.getVisitOrder()));
    }
}
