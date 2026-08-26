package code.travelplanner.Backend.waypoint.Dto;

public class WaypointLinkToPlacesDto {

    private Long waypointId;
    private Long tripId;
    private int visitOrder;

    public WaypointLinkToPlacesDto(Long tripId, Long waypointId,  int visitOrder) {
        this.waypointId = waypointId;
        this.tripId = tripId;
        this.visitOrder = visitOrder;
    }

    public Long getWaypointId() { return waypointId; }
    public void setWaypointId(Long waypointId) { this.waypointId = waypointId; }

    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }

    public int getVisitOrder() { return visitOrder; }
    public void setVisitOrder(int visitOrder) { this.visitOrder = visitOrder; }
}
