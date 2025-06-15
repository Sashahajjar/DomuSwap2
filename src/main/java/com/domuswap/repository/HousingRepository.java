package com.domuswap.repository;

import com.domuswap.model.Housing;
import com.domuswap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HousingRepository extends JpaRepository<Housing, Long> {
    List<Housing> findByLocationContainingIgnoreCase(String location);
    List<Housing> findByOwnerId(Long ownerId);
    @Query("SELECT h FROM Housing h LEFT JOIN FETCH h.services WHERE h.housing_id = :id")
    Optional<Housing> findByIdWithServices(@Param("id") Long id);

    // Custom queries for ListingController
    @Query("SELECT h FROM Housing h WHERE (:location IS NULL OR LOWER(h.location) LIKE LOWER(CONCAT('%', :location, '%')) " +
           "AND (:minPrice IS NULL OR h.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR h.price <= :maxPrice) " +
           "AND (:minBedrooms IS NULL OR h.bedrooms >= :minBedrooms) " +
           "AND (:maxBedrooms IS NULL OR h.bedrooms <= :maxBedrooms) " +
           "AND (:amenities IS NULL OR h.amenities LIKE CONCAT('%', :amenities, '%'))")
    List<Housing> searchListings(@Param("location") String location,
                                @Param("minPrice") Double minPrice,
                                @Param("maxPrice") Double maxPrice,
                                @Param("minBedrooms") Integer minBedrooms,
                                @Param("maxBedrooms") Integer maxBedrooms,
                                @Param("amenities") String amenities);

    @Query("SELECT h FROM Housing h WHERE h.featured = true")
    List<Housing> findFeaturedListings();

    @Query("SELECT h FROM Housing h ORDER BY h.createdAt DESC")
    List<Housing> findRecentListings();

    @Query("SELECT h FROM Housing h WHERE h.location = :location AND h.price BETWEEN :price - 100 AND :price + 100 AND h.bedrooms = :bedrooms AND h.housing_id <> :listingId")
    List<Housing> findSimilarListings(@Param("location") String location,
                                      @Param("price") Double price,
                                      @Param("bedrooms") Integer bedrooms,
                                      @Param("listingId") Long listingId);

    List<Housing> findByOwner(User owner);

    @Query("SELECT AVG(h.price) FROM Housing h")
    Double getAveragePrice();

    @Query("SELECT h.location, COUNT(h) as count FROM Housing h GROUP BY h.location ORDER BY count DESC")
    List<Object[]> getPopularLocations();
} 