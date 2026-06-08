package code.travelplanner.user.Entity;

import code.travelplanner.trip.Entity.TripMembersEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;
    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy = "User")
    private List<TripMembersEntity> tripMembers = new ArrayList<>();

    public UserEntity() {}

    public UserEntity(String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public long getUserId() { return this.userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }

}
