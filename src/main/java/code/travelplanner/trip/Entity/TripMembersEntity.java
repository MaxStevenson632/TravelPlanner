package code.travelplanner.trip.Entity;

import code.travelplanner.user.Entity.UserEntity;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class TripMembersEntity implements Serializable {

    @EmbeddedId
    private TripMembersId id = new TripMembersId();

    @ManyToOne
    @MapsId("userId") //Get the user's id from Users table, copy it into StudentId field inside TripMembersId
    @JoinColumn(name = "user_id") // The name of the foreign key column inside this table
    private UserEntity user;

    @ManyToOne
    @MapsId("tripId") // Get the trip's id from the Trips table, copy it into TripId field inside TripMembersId
    @JoinColumn(name = "trip_id") // The name of the other foreign key column inside this table
    private TripEntity trip;

    @Column(name = "member_role")
    private String memberRole;

    public TripMembersEntity() {}

    public TripMembersEntity(UserEntity user,TripEntity trip, String memberRole) {
        this.user = user;
        this.trip = trip;
        this.memberRole = memberRole;
    }

    public TripMembersId getId() { return id; }
    public void setId(TripMembersId id) { this.id = id; }

    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }

    public TripEntity getTrip() { return trip; }
    public void setTrip(TripEntity trip) { this.trip = trip; }

    public String getMemberRole() { return memberRole; }
    public void setMemberRole(String memberRole) { this.memberRole = memberRole; }

}
