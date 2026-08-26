package code.travelplanner.Backend.waypoint.Repository;

import code.travelplanner.Backend.waypoint.Entity.WaypointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WaypointRepository extends JpaRepository<WaypointEntity, Long>  {

    Optional<WaypointEntity> findByPlaceName(String placeName);

    Optional<WaypointEntity> findByLatitudeAndLongitude(double latitude, double longitude);
}
