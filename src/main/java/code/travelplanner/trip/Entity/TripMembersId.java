package code.travelplanner.trip.Entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class TripMembersId implements Serializable {

    private long tripId;
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

}
