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
    @Column(name = "latitude", precision = 10)
    private double latitude;
    @Column(name = "longitude", precision = 10)
    private double longitude;
    @Column(name = "name")
    private String placeName;

    public WaypointEntity() {}

    public WaypointEntity(double latitude, double longitude, String placeName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
    }

    public long getWaypointId() { return this.waypointId; }
    public  void setWaypointId(long waypointId) { this.waypointId = waypointId; }

    public double getLatitude() { return this.latitude; }
    public  void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return this.longitude; }
    public  void setLongitude(double longitude) { this.longitude = longitude; }

    public String getPlaceName() { return this.placeName; }
    public  void setPlaceName(String placeName) { this.placeName = placeName; }
}
