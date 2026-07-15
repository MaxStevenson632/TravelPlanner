package code.travelplanner.Backend.trip.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "trip_places")
public class TripPlacesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tripPlacesId;

    @Column(name = "trip_id")
    private long tripId;
    @Column(name = "waypoint_id")
    private long waypointId;

    @Column(name = "visit_order")
    private int visitOrder;

    public TripPlacesEntity() {}

    public TripPlacesEntity(long tripId, long waypointId, int visitOrder) {
        this.tripId = tripId;
        this.waypointId = waypointId;
        this.visitOrder = visitOrder;
    }

    public long getTripPlacesId() { return tripPlacesId; }
    public void setTripPlacesId(long tripPlacesId) {this.tripPlacesId = tripPlacesId; }

    public long getTripId() { return this.tripId; }
    public void setTripId(long tripId) { this.tripId = tripId; }

    public long getWaypointId() { return this.waypointId; }
    public void setWaypointId(long waypointId) { this.waypointId = waypointId; }

    public int getVisitOrder() { return this.visitOrder; }
    public void setVisitOrder(int visitOrder) { this.visitOrder = visitOrder; }
}
