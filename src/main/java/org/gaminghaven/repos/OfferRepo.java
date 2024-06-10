package org.gaminghaven.repos;

import org.gaminghaven.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepo extends JpaRepository<Offer, Integer> {
}
