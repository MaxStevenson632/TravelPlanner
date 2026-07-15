package code.travelplanner.Backend.waypoint.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeocodingResponse {

    @JsonProperty("display_name")
    private String placeName;

    @JsonProperty("lat")
    private double latitude;

    @JsonProperty("lon")
    private double longitude;

    public String getPlaceName() { return placeName; }
    public void setPlaceName(String placeName) { this.placeName = placeName; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}
