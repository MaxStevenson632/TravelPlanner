package code.travelplanner.Backend.waypoint.Dto;

public class WaypointMapDto {

    private String placeName;
    private double latitude;
    private double longitude;
    private int visitOrder;
    private Long waypointId;

    public String getPlaceName() { return placeName; }
    public void setPlaceName(String placeName) { this.placeName = placeName; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public int getVisitOrder() { return visitOrder; }
    public void setVisitOrder(int visitOrder) { this.visitOrder = visitOrder; }

    public Long getWaypointId() { return waypointId; }
    public void setWaypointId(Long waypointId) { this.waypointId = waypointId; }
}
