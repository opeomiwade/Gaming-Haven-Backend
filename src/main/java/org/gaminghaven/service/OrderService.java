package org.gaminghaven.service;
import org.gaminghaven.entities.Order;
import org.gaminghaven.exceptions.OrderNotFoundException;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();

    Order getOrderById(int id) throws OrderNotFoundException;
}
