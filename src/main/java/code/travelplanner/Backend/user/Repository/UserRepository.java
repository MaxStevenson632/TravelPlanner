package code.travelplanner.Backend.user.Repository;

import code.travelplanner.Backend.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>  {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByName(String name);

    Optional<UserEntity> findByUserId(Long userId);

    @Query("""
            SELECT user from UserEntity user
            WHERE ((user.name LIKE CONCAT('%', :query, '%'))
            OR user.email LIKE CONCAT('%', :query, '%'))
            AND user.userId <> :requesterId
                        AND user.userId NOT IN (
                        SELECT tripMember.id.userId FROM TripMembersEntity tripMember WHERE tripMember.id.tripId = :tripId)
            """)
    List<UserEntity> searchUsersExcludingSelfAndOtherMembers(@Param("query")  String query, @Param("tripId") Long tripId,
                                                             @Param("requesterId") Long requesterId);
}