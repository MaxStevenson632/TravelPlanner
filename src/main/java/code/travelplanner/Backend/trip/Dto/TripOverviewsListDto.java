package code.travelplanner.Backend.trip.Dto;

import java.util.List;

public class TripOverviewsListDto {

    private String username;
    private List<TripOverviewsDto> tripDataDto;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public List<TripOverviewsDto> getTripDataDto() { return tripDataDto; }
    public void setTripDataDto(List<TripOverviewsDto> tripDataDto) {  this.tripDataDto = tripDataDto; }
}
