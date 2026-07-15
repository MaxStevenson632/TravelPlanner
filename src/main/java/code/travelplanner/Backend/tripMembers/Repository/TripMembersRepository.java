package code.travelplanner.Backend.tripMembers.Repository;

import code.travelplanner.Backend.trip.Entity.TripEntity;
import code.travelplanner.Backend.tripMembers.Entity.TripMembersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripMembersRepository extends JpaRepository<TripMembersEntity, Long> {

}
