package code.travelplanner.Backend.trip.Service;

import code.travelplanner.Backend.Exception.PlaceNotFoundException;
import code.travelplanner.Backend.Exception.TripNotFoundException;
import code.travelplanner.Backend.Exception.UnauthorisedException;
import code.travelplanner.Backend.Exception.UserNotFoundException;
import code.travelplanner.Backend.trip.Dto.NewTripDto;
import code.travelplanner.Backend.trip.Dto.TripOverviewsDto;
import code.travelplanner.Backend.trip.Dto.TripMapDto;
import code.travelplanner.Backend.trip.Dto.TripOverviewsListDto;
import code.travelplanner.Backend.trip.Entity.TripEntity;
import code.travelplanner.Backend.trip.Entity.TripPlacesEntity;
import code.travelplanner.Backend.trip.Repository.TripPlacesRepository;
import code.travelplanner.Backend.trip.Repository.TripRepository;
import code.travelplanner.Backend.tripMembers.Dto.MemberInfoDto;
import code.travelplanner.Backend.tripMembers.Entity.TripMembersEntity;
import code.travelplanner.Backend.tripMembers.Repository.TripMembersRepository;
import code.travelplanner.Backend.tripMembers.Service.TripMembersService;
import code.travelplanner.Backend.user.Entity.UserEntity;
import code.travelplanner.Backend.user.Repository.UserRepository;
import code.travelplanner.Backend.waypoint.Dto.WaypointMapDto;
import code.travelplanner.Backend.waypoint.Entity.WaypointEntity;
import code.travelplanner.Backend.waypoint.Repository.WaypointRepository;
import code.travelplanner.Backend.waypoint.Service.WaypointService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final TripMembersService tripMembersService;
    private final UserRepository userRepository;
    private final WaypointService waypointService;
    private final TripMembersRepository tripMembersRepository;
    private final TripPlacesRepository  tripPlacesRepository;
    private final WaypointRepository  waypointRepository;

    public TripService(TripRepository tripRepository,  TripMembersService tripMembersService,  UserRepository userRepository,
                       WaypointService waypointService, TripMembersRepository tripMembersRepository,
                       TripPlacesRepository tripPlacesRepository,  WaypointRepository waypointRepository) {
        this.tripRepository = tripRepository;
        this.tripMembersService = tripMembersService;
        this.userRepository = userRepository;
        this.waypointService = waypointService;
        this.tripMembersRepository = tripMembersRepository;
        this.tripPlacesRepository = tripPlacesRepository;
        this.waypointRepository = waypointRepository;
    }

    @Transactional
    public ResponseEntity<?> createTrip (NewTripDto newTripData, Long userId) {

        TripEntity tripEntity = new TripEntity(newTripData.getTitle(),
                newTripData.getStartDate(), newTripData.getEndDate());
        tripRepository.save(tripEntity);
        tripMembersService.createOwner(tripEntity.getTripId(), userId);
        waypointService.addInitialWaypoints(newTripData.getWaypoints(), tripEntity.getTripId());

        return  ResponseEntity.ok().build();
    }

    public TripMapDto getTripMapData(Long tripId, Long userId) {

        // Check trip exists
        TripEntity trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found"));

        // Check user belongs to trip
        tripMembersRepository.findByIdTripIdAndIdUserId(tripId, userId)
                .orElseThrow(() -> new UnauthorisedException("Not a member of the trip"));

        // List of ordered waypoints of this trip
        List<TripPlacesEntity> tripPlaces = tripPlacesRepository.findByTripIdOrderByVisitOrder(tripId);

        // List of users who belong to the trip
        List<TripMembersEntity> tripMembers = tripMembersRepository.findByIdTripId(tripId);

        // Map each TripPlacesEntity into a WaypointMapDto
        List<WaypointMapDto> waypointDtos = tripPlaces.stream()
                .map(tripPlace -> {

                    // Fetch actual waypoint
                    WaypointEntity waypoint = waypointRepository
                            .findById(tripPlace.getWaypointId())
                            .orElseThrow(() -> new PlaceNotFoundException("Waypoint not found"));

                    // Fill in WaypointMapDto using waypoint object's attributes
                    WaypointMapDto waypointMapDto = new WaypointMapDto();
                    waypointMapDto.setPlaceName(waypoint.getPlaceName());
                    waypointMapDto.setLatitude(waypoint.getLatitude());
                    waypointMapDto.setLongitude(waypoint.getLongitude());
                    waypointMapDto.setVisitOrder(tripPlace.getVisitOrder());
                    waypointMapDto.setWaypointId(waypoint.getWaypointId());

                    return waypointMapDto;
                })
                .toList();

        // Map each user belonging to the trip into a MemberInfoDto
        List<MemberInfoDto> memberDtos = tripMembers.stream()
                .map(tripMember -> {

                    UserEntity user = userRepository
                            .findByUserId(tripMember.getId().getUserId())
                            .orElseThrow(() -> new UserNotFoundException("User not found"));

                    MemberInfoDto memberInfoDto = new MemberInfoDto();
                    memberInfoDto.setName(user.getName());
                    memberInfoDto.setRole(tripMember.getMemberRole());
                    memberInfoDto.setId(tripMember.getId().getUserId());

                    return memberInfoDto;
                })
                .toList();

        // Wrap it all in TripMapDto
        TripMapDto mapData = new TripMapDto();
        mapData.setName(trip.getTitle());
        mapData.setStartDate(trip.getStartDate());
        mapData.setEndDate(trip.getEndDate());
        mapData.setWaypoints(waypointDtos);
        mapData.setMembers(memberDtos);

        return mapData;
    }

    public TripOverviewsListDto getTripOverviewsData(Long userId) {

        List<TripMembersEntity> tripMembers = tripMembersRepository.findByIdUserId(userId);
        if (tripMembers.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        List<TripOverviewsDto> tripDataDtos = tripMembers.stream()
                . map(tripMember -> {

                    TripEntity tripEntity = tripRepository
                            .findById(tripMember.getId().getTripId())
                            .orElseThrow(() -> new TripNotFoundException("Trip not found"));

                    TripOverviewsDto tripDataDto = new TripOverviewsDto();
                    tripDataDto.setName(tripEntity.getTitle());
                    tripDataDto.setRole(tripMember.getMemberRole());
                    tripDataDto.setId(tripEntity.getTripId());

                    return tripDataDto;
                })
                .toList();

        TripOverviewsListDto tripDataList = new TripOverviewsListDto();
        tripDataList.setTripDataDto(tripDataDtos);

        return tripDataList;
    }
}
