package code.travelplanner.Backend.trip.Repository;

import code.travelplanner.Backend.trip.Entity.TripEntity;
import code.travelplanner.Backend.trip.Entity.TripPlacesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripPlacesRepository extends JpaRepository<TripPlacesEntity, Long> {

    List<TripPlacesEntity> findByTripIdOrderByVisitOrder(long tripId);
}
