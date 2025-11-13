package co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.persistence.repository;

import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByCategoryIgnoreCase(String category);

    List<ProductEntity> findByPriceGreaterThan(BigDecimal price);

    List<ProductEntity> findByPriceBetween(BigDecimal min, BigDecimal max);
}
