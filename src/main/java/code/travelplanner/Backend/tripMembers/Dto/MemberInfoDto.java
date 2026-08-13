package code.travelplanner.Backend.tripMembers.Dto;

import code.travelplanner.Backend.tripMembers.Entity.Role;

public class MemberInfoDto {

    private String name;
    private Role role;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
