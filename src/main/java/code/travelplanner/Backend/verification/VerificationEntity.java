package code.travelplanner.Backend.verification;

import code.travelplanner.Backend.trip.Entity.TripEntity;
import code.travelplanner.Backend.user.Entity.UserEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Verification")
public class VerificationEntity {

    @Id
    @Column(nullable = false, unique = true, name = "verification_token")
    private String token;
    @Column(unique = false, nullable = false, name = "expiry_date")
    private LocalDateTime expiryDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity user;

    public VerificationEntity() {}

    public VerificationEntity(String token, LocalDate expiryData) {
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }

    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
}
