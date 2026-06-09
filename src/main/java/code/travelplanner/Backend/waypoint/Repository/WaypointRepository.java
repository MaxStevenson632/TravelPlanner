package code.travelplanner.Backend.waypoint.Repository;

import code.travelplanner.Backend.waypoint.Entity.WaypointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaypointRepository extends JpaRepository<WaypointEntity, Long>  {

}
