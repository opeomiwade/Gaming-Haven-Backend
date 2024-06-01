package org.gaminghaven.repos;

import org.gaminghaven.entities.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepo extends JpaRepository<Trade, Integer> {

}
