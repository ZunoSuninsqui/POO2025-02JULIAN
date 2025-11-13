package co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.persistence.repository;

import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.persistence.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, Long> {

    Optional<CartItemEntity> findByProductId(Long productId);
}
