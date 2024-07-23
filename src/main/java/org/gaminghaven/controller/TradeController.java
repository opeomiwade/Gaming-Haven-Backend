package org.gaminghaven.controller;

import org.gaminghaven.entities.Trade;
import org.gaminghaven.exceptions.ListingNotFoundException;
import org.gaminghaven.exceptions.TradeNotFound;
import org.gaminghaven.repos.TradeRepo;
import org.gaminghaven.requestobjects.TradeRequest;
import org.gaminghaven.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/trades")
public class TradeController {

    @Autowired
    TradeService tradeService;

    @Autowired
    TradeRepo tradeRepo;

    @GetMapping("/")
    public ResponseEntity getTrades() {
        return new ResponseEntity<>(tradeRepo.findAll(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity initiateTrade(@RequestBody TradeRequest tradeRequest) {
        try {
            return new ResponseEntity(tradeService.initiateTrade(tradeRequest.getListingId(),
                    tradeRequest.getOfferedItems()), HttpStatus.OK);
        } catch (ListingNotFoundException exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{tradeId}/items")
    public ResponseEntity getTradeOfferedItems(@PathVariable int tradeId) {
        Trade trade = tradeRepo.findById(tradeId).orElse(null);
        if (trade == null) return new ResponseEntity("Trade not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity(trade.getOfferedItems(), HttpStatus.OK);
    }

    @PutMapping("/{tradeId}/update-status")
    public ResponseEntity acceptTrade(@PathVariable int tradeId, @RequestBody Map<String, String> statusUpdate) {
        try {
            return new ResponseEntity(tradeService.updateTradeStatus(tradeId, statusUpdate.get("status")), HttpStatus.OK);
        } catch (TradeNotFound exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
