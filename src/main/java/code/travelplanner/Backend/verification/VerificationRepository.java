package code.travelplanner.Backend.verification;

import code.travelplanner.Backend.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationEntity, Long> {

    Optional<VerificationEntity> findByToken(String token);

    void deleteAllByExpiryDateBefore(LocalDateTime now);
}
