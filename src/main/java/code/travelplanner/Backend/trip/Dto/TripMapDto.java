package code.travelplanner.Backend.trip.Dto;

import code.travelplanner.Backend.waypoint.Dto.WaypointMapDto;

import java.time.LocalDate;
import java.util.List;

public class TripMapDto {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<WaypointMapDto> waypoints;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public List<WaypointMapDto> getWaypoints() { return waypoints; }
    public void setWaypoints(List<WaypointMapDto> waypoints) { this.waypoints = waypoints; }
}
