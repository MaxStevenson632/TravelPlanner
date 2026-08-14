package code.travelplanner.Backend.tripMembers.Repository;

import code.travelplanner.Backend.trip.Entity.TripEntity;
import code.travelplanner.Backend.tripMembers.Entity.TripMembersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripMembersRepository extends JpaRepository<TripMembersEntity, Long> {

    Optional<TripMembersEntity> findByIdTripIdAndIdUserId(long tripId, long userId);

    List<TripMembersEntity> findByIdUserId(long userId);

    List<TripMembersEntity> findByIdTripId(long tripId);
}
