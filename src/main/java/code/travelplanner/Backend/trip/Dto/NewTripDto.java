package code.travelplanner.Backend.trip.Dto;

import java.time.LocalDate;
import java.util.List;

public class NewTripDto {

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> waypoints;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public  List<String> getWaypoints() { return waypoints; }
    public void setWaypoints(List<String> waypoints) { this.waypoints = waypoints; }
}
