package code.travelplanner.Backend.user.Repository;

import code.travelplanner.Backend.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>  {

    Optional<UserEntity> findByEmail(String email);
}
