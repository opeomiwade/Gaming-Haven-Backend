package org.gaminghaven.service;
import org.gaminghaven.entities.Order;
import org.gaminghaven.exceptions.OrderNotFoundException;
import org.gaminghaven.repos.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Override
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public Order getOrderById(int id) throws OrderNotFoundException {
        Order order = orderRepo.findById(id).orElse(null);
        if (order != null) {
            return order;
        } else {
            throw new OrderNotFoundException("No order with that id exists");
        }
    }
}
