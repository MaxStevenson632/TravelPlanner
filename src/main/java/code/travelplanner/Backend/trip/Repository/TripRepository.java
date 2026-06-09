package code.travelplanner.Backend.trip.Repository;

import code.travelplanner.Backend.trip.Entity.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<TripEntity, Long> {

}
