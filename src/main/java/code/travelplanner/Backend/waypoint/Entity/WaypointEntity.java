package code.travelplanner.Backend.waypoint.Entity;

import code.travelplanner.Backend.trip.Entity.TripEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "Waypoints")
public class WaypointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waypoint_id")
    private long waypointId;
    private double latitude;
    private double longitude;

    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "trip_id", nullable = false)
    private TripEntity trip;
}
