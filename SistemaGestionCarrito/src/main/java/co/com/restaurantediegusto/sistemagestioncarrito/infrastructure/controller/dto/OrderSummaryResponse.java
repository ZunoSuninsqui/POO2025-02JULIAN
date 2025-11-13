package co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderSummaryResponse(Long orderId,
                                   LocalDateTime createdAt,
                                   List<OrderItemResponse> items,
                                   BigDecimal totalBeforeDiscount,
                                   BigDecimal discount,
                                   BigDecimal totalToPay) {
}
