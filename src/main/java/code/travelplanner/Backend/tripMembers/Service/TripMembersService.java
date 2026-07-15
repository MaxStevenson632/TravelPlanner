package code.travelplanner.Backend.tripMembers.Service;

import code.travelplanner.Backend.tripMembers.Entity.Role;
import code.travelplanner.Backend.tripMembers.Entity.TripMembersEntity;
import code.travelplanner.Backend.tripMembers.Repository.TripMembersRepository;
import org.springframework.stereotype.Service;

@Service
public class TripMembersService {

    private final TripMembersRepository tripMembersRepository;

    public TripMembersService(TripMembersRepository tripMembersRepository) {
        this.tripMembersRepository = tripMembersRepository;
    }

    public void createOwner(Long tripId, Long userId) {

        TripMembersEntity owner = new TripMembersEntity();
        owner.getId().setTripId(tripId);
        owner.getId().setUserId(userId);
        owner.setMemberRole(Role.OWNER);
        tripMembersRepository.save(owner);
    }
}
