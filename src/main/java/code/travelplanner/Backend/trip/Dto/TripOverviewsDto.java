package code.travelplanner.Backend.trip.Dto;

import code.travelplanner.Backend.tripMembers.Entity.Role;

public class TripOverviewsDto {

    private String name;
    private Role role;
    private long id;

    public TripOverviewsDto() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
