package code.travelplanner.trip.Repository;

import code.travelplanner.trip.Entity.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<TripEntity, Long> {

}
