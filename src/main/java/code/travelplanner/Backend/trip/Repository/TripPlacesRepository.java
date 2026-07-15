package code.travelplanner.Backend.trip.Repository;

import code.travelplanner.Backend.trip.Entity.TripEntity;
import code.travelplanner.Backend.trip.Entity.TripPlacesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripPlacesRepository extends JpaRepository<TripPlacesEntity, Long> {


}
