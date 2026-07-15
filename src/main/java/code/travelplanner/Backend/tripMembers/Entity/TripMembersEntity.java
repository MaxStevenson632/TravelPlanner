package code.travelplanner.Backend.tripMembers.Entity;

import code.travelplanner.Backend.trip.Entity.TripEntity;
import code.travelplanner.Backend.user.Entity.UserEntity;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class TripMembersEntity implements Serializable {

    // Create composite key of tripId and UserId, enforces a user can only appear once per trip
    @EmbeddedId
    private TripMembersId id = new TripMembersId();

    @Column(name = "member_role")
    @Enumerated(EnumType.STRING)
    private Role memberRole;

    public TripMembersEntity() {}

    public TripMembersEntity(Long userId) {
        this.memberRole = memberRole;
    }

    public TripMembersId getId() { return id; }
    public void setId(TripMembersId id) { this.id = id; }

    public Role getMemberRole() { return memberRole; }
    public void setMemberRole(Role memberRole) { this.memberRole = memberRole; }

}
