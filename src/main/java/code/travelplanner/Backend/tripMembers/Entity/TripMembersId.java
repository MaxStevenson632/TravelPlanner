package code.travelplanner.Backend.tripMembers.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TripMembersId implements Serializable {

    @Column(name = "trip_id")
    private long tripId;

    @Column(name = "user_id")
    private long userId;

    public TripMembersId() {}

    public TripMembersId(long tripId, long userId) {
        this.tripId = tripId;
        this.userId = userId;
    }

    public long getTripId() { return this.tripId; }
    public void setTripId(long tripId) { this.tripId = tripId; }

    public long getUserId() { return this.userId; }
    public void setUserId(long userId) { this.userId = userId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TripMembersId)) return false;
        TripMembersId that = (TripMembersId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(tripId, that.tripId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, tripId);
    }
}
