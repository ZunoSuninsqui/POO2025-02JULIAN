package co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.config;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(LocalDateTime timestamp, String message, List<String> details) {
}
