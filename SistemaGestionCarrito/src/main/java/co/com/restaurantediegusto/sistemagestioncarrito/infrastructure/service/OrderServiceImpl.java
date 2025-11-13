package co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.service;

import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.CartItem;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.OrderItem;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.OrderSummary;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.service.OrderService;
import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.persistence.entity.OrderEntity;
import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.persistence.entity.OrderItemEntity;
import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.persistence.repository.OrderJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderJpaRepository orderRepository;

    public OrderServiceImpl(OrderJpaRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderSummary saveOrder(List<CartItem> items, BigDecimal subtotal, BigDecimal discount, BigDecimal total) {
        OrderEntity order = new OrderEntity();
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalBeforeDiscount(subtotal);
        order.setDiscount(discount);
        order.setTotalToPay(total);

        items.forEach(item -> {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setProductId(item.product().id());
            orderItem.setProductName(item.product().name());
            orderItem.setCategory(item.product().category());
            orderItem.setUnitPrice(item.product().price());
            orderItem.setQuantity(item.quantity());
            orderItem.setSubtotal(item.subtotal());
            order.addItem(orderItem);
        });

        OrderEntity saved = orderRepository.save(order);
        return toDomain(saved);
    }

    private OrderSummary toDomain(OrderEntity entity) {
        List<OrderItem> orderItems = entity.getItems().stream()
                .map(item -> new OrderItem(
                        item.getProductId(),
                        item.getProductName(),
                        item.getCategory(),
                        item.getUnitPrice(),
                        item.getQuantity(),
                        item.getSubtotal()
                ))
                .toList();
        return new OrderSummary(
                entity.getId(),
                entity.getCreatedAt(),
                orderItems,
                entity.getTotalBeforeDiscount(),
                entity.getDiscount(),
                entity.getTotalToPay()
        );
    }
}
