package code.travelplanner.trip.Entity;

import code.travelplanner.waypoint.Entity.WaypointEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Trips")
public class TripEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private long tripId;
    private String title;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToMany(mappedBy = "trip")
    private List<TripMembersEntity> tripMembers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip")
    private List<WaypointEntity> waypoints = new ArrayList<>();

    public TripEntity() {}

    public TripEntity(String title, LocalDate startDate, LocalDate endDate) {
        this.tripId = tripId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getTripId() { return this.tripId; }
    public void setTripId(long tripId) { this.tripId = tripId; }

    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getStartDate() { return this.startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return this.endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

}
