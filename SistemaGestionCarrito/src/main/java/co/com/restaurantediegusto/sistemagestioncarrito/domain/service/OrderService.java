package co.com.restaurantediegusto.sistemagestioncarrito.domain.service;

import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.CartItem;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.OrderSummary;

import java.math.BigDecimal;
import java.util.List;

/**
 * Permite registrar pedidos cerrados.
 */
public interface OrderService {

    OrderSummary saveOrder(List<CartItem> items, BigDecimal subtotal, BigDecimal discount, BigDecimal total);
}
