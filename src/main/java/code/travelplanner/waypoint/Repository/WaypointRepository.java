package code.travelplanner.waypoint.Repository;

import code.travelplanner.waypoint.Entity.WaypointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaypointRepository extends JpaRepository<WaypointEntity, Long>  {

}
