package org.gaminghaven.repos;

import org.gaminghaven.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.gaminghaven.entities.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface ListingRepo extends JpaRepository<Listing, Integer> {
    @Query("SELECT l FROM Listing l where LOWER(l.listedProduct.category.name) = :name")
    List<Listing> findByCategoryName(@Param("name") String categoryName);

    List<Listing> findBySeller(User seller);

    @Query("select sum(price) as total_amount from Listing l where l.seller.userId = :userId and status = 'sold' ")
    BigDecimal getUserTotalIncome(@Param("userId") int userId);

    @Query("select l from Listing l where l.seller.userId = :userId  and l.status = 'sold'")
    List<Listing> getUserSoldListings(@Param("userId") int userId);
}
