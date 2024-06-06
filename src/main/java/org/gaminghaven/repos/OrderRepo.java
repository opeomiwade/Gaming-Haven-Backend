package org.gaminghaven.repos;

import org.gaminghaven.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
    @Query("select sum(totalPrice) as total_amount from Order where buyer_id = :userId ")
    BigDecimal getUserTotalExpenses(@Param("userId") int userId);

    @Query("select sum(totalPrice) as total_amount from Order where seller_id = :userId ")
    BigDecimal getUserTotalIncome(@Param("userId") int userId);

    @Query("select sum(totalPrice) as total_sales from Order")
    BigDecimal calculateTotalSales();
}
