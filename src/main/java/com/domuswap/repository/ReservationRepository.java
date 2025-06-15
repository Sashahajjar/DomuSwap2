package com.domuswap.repository;

import com.domuswap.model.Reservation;
import com.domuswap.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r JOIN FETCH r.user JOIN FETCH r.housing WHERE r.user.id = :userId")
    List<Reservation> findByUserId(@Param("userId") Long userId);
    @Query("SELECT r FROM Reservation r JOIN FETCH r.user JOIN FETCH r.housing WHERE r.housing.housing_id = :housingId")
    List<Reservation> findByHousingId(@Param("housingId") Long housingId);
    @Query("SELECT r FROM Reservation r JOIN FETCH r.user JOIN FETCH r.housing WHERE r.housing.owner.id = :ownerId")
    List<Reservation> findByHousingOwnerId(@Param("ownerId") Long ownerId);
    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.housing.housing_id = :housingId AND r.user.id = :userId")
    boolean existsByHousingIdAndUserId(@Param("housingId") Long housingId, @Param("userId") Long userId);
    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.housing.owner.id = :ownerId AND r.user.id = :userId AND r.status IN ('PENDING', 'ACCEPTED')")
    boolean existsByHousingOwnerIdAndUserId(@Param("ownerId") Long ownerId, @Param("userId") Long userId);
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.housing.owner.id = :ownerId AND r.status = :status")
    int countByHousingOwnerIdAndStatus(@Param("ownerId") Long ownerId, @Param("status") ReservationStatus status);
    @Query("SELECT r FROM Reservation r WHERE r.housing.owner.id = :ownerId AND r.status = :status")
    List<Reservation> findByHousingOwnerIdAndStatus(@Param("ownerId") Long ownerId, @Param("status") ReservationStatus status);
} 