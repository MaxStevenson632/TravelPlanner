package code.travelplanner.user.Repository;

import code.travelplanner.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long>  {

}
