package org.gaminghaven.service;

import org.gaminghaven.entities.Listing;
import org.gaminghaven.entities.Trade;
import org.gaminghaven.exceptions.ListingNotFoundException;
import org.gaminghaven.exceptions.TradeNotFound;

import java.util.List;

public interface TradeService {
    Trade initiateTrade(int listingId, List<Listing> offeredProducts) throws ListingNotFoundException;

    Trade updateTradeStatus(int tradeId, String status) throws TradeNotFound;
}
