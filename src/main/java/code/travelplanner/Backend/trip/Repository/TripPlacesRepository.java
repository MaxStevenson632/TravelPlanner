package code.travelplanner.Backend.trip.Repository;

import code.travelplanner.Backend.trip.Entity.TripEntity;
import code.travelplanner.Backend.trip.Entity.TripPlacesEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripPlacesRepository extends JpaRepository<TripPlacesEntity, Long> {

    List<TripPlacesEntity> findByTripIdOrderByVisitOrder(long tripId);

    @Modifying
    @Query("UPDATE TripPlacesEntity tripPlaces SET tripPlaces.visitOrder = tripPlaces.visitOrder + 1 " +
    "WHERE tripPlaces.tripId = :tripId AND tripPlaces.visitOrder >= :newOrder")
    void incrementOrders(@Param("tripId") Long tripId, @Param("newOrder") Integer newOrder);

    @Transactional
    void deleteByWaypointId(Long waypointId, Long tripId);
}
