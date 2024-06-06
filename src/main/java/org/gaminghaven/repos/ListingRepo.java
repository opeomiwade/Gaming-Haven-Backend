package org.gaminghaven.repos;

import org.gaminghaven.entities.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.gaminghaven.entities.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


@Repository
public interface ListingRepo extends JpaRepository<Listing, Integer> {
    @Query("SELECT l FROM Listing l where LOWER(l.listedProduct.category.name) = :name")
    List<Listing> findByCategoryName(@Param("name") String categoryName);
}
