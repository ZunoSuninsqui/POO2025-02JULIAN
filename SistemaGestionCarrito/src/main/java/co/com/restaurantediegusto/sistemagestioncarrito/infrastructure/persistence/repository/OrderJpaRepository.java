package co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.persistence.repository;

import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
}
