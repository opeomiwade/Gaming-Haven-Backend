package org.gaminghaven.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingImageRepo extends JpaRepository<org.gaminghaven.entities.ListingImage, Integer> {
}
