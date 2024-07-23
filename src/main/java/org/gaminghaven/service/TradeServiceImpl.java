package org.gaminghaven.service;

import org.gaminghaven.entities.Listing;
import org.gaminghaven.entities.Trade;
import org.gaminghaven.exceptions.ListingNotFoundException;
import org.gaminghaven.exceptions.TradeNotFound;
import org.gaminghaven.repos.ListingRepo;
import org.gaminghaven.repos.TradeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeServiceImpl implements TradeService {
    @Autowired
    TradeRepo tradeRepo;

    @Autowired
    ListingRepo listingRepo;

    @Override
    public Trade initiateTrade(int listingId, List<Listing> offeredItems) throws ListingNotFoundException {
        Trade trade = new Trade();
        Listing requestedItem = listingRepo.findById(listingId).orElseThrow(() ->
                new ListingNotFoundException("Listing was not found"));
        trade.setOfferedItems(offeredItems);
        trade.setRequestedItem(requestedItem);
        trade.setSender(offeredItems.get(0).getSeller());
        trade.setRecipient(requestedItem.getSeller());
        tradeRepo.save(trade);
        listingRepo.save(requestedItem);
        return trade;
    }

    @Override
    public Trade updateTradeStatus(int tradeId, String status ) throws TradeNotFound {
        Trade trade = tradeRepo.findById(tradeId).orElseThrow(() -> new TradeNotFound("No trade with that id"));
        trade.setTradeStatus(status);
        tradeRepo.save(trade);
        return trade;
    }

}
