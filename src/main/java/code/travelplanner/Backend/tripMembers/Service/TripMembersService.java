package code.travelplanner.Backend.tripMembers.Service;

import code.travelplanner.Backend.tripMembers.Entity.Role;
import code.travelplanner.Backend.tripMembers.Entity.TripMembersEntity;
import code.travelplanner.Backend.tripMembers.Repository.TripMembersRepository;
import code.travelplanner.Backend.user.Entity.UserEntity;
import code.travelplanner.Backend.user.Repository.UserRepository;
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

    public void addPersonToTrip(Long tripId, Long requesterId, String role, Long memberToAddId) {

        // Verify user belongs to trip
        TripMembersEntity memberCheck = tripMembersRepository.findByIdTripIdAndIdUserId(tripId, requesterId)
                .orElseThrow(() -> new IllegalArgumentException("Error matching user with trip"));

        // Verify user has permission to add a member
        if (!(memberCheck.getMemberRole().equals(Role.MEMBER) || memberCheck.getMemberRole().equals(Role.OWNER))) {
            throw new  IllegalArgumentException("User does not have permission to add member");
        }

        Role userRole = null;

        // Remove quotation marks from string
        String cleanRole = role.replace("\"", "").trim();

        if (cleanRole.equals("VIEWER")) {
            userRole = Role.VIEWER;
        } else if (cleanRole.equals("MEMBER")) {
            userRole = Role.MEMBER;
        } else {
            throw new IllegalArgumentException("Invalid role");
        }

        // Save user to trip
        TripMembersEntity member = new TripMembersEntity();
        member.getId().setTripId(tripId);
        member.getId().setUserId(memberToAddId);
        member.setMemberRole(userRole);
        tripMembersRepository.save(member);
    }
}
