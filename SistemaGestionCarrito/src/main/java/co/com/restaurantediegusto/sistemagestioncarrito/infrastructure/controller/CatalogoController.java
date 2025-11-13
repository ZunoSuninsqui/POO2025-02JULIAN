package co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.controller;

import co.com.restaurantediegusto.sistemagestioncarrito.application.usecase.ProductUseCase;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.Product;
import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.controller.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogo")
public class CatalogoController {

    private final ProductUseCase productUseCase;

    public CatalogoController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listarCatalogo() {
        List<ProductResponse> response = productUseCase.getCatalog().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductResponse>> listarPorCategoria(@PathVariable String categoria) {
        List<ProductResponse> response = productUseCase.findByCategory(categoria).stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/precio")
    public ResponseEntity<List<ProductResponse>> filtrarPorPrecio(
            @RequestParam(value = "mayorA", required = false) BigDecimal mayorA,
            @RequestParam(value = "min", required = false) BigDecimal min,
            @RequestParam(value = "max", required = false) BigDecimal max) {
        List<Product> products;
        if (mayorA != null) {
            products = productUseCase.findByPriceGreaterThan(mayorA);
        } else {
            products = productUseCase.findByPriceRange(min, max);
        }
        List<ProductResponse> response = products.stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(product.id(), product.name(), product.category(), product.price());
    }
}
