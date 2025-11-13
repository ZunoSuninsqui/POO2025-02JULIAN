package co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.controller;

import co.com.restaurantediegusto.sistemagestioncarrito.application.usecase.CartUseCase;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.CartItem;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.CartSummary;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.OrderItem;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.OrderSummary;
import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.controller.dto.AddCartItemRequest;
import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.controller.dto.CartItemResponse;
import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.controller.dto.CartResponse;
import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.controller.dto.MessageResponse;
import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.controller.dto.OrderItemResponse;
import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.controller.dto.OrderSummaryResponse;
import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.controller.dto.UpdateCartItemRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carrito")
public class CarritoController {

    private final CartUseCase cartUseCase;

    public CarritoController(CartUseCase cartUseCase) {
        this.cartUseCase = cartUseCase;
    }

    @GetMapping
    public ResponseEntity<CartResponse> verCarrito() {
        return ResponseEntity.ok(toCartResponse(cartUseCase.viewCart()));
    }

    @PostMapping
    public ResponseEntity<CartResponse> agregarProducto(@Valid @RequestBody AddCartItemRequest request) {
        cartUseCase.addProduct(request.productId(), request.quantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(toCartResponse(cartUseCase.viewCart()));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<CartResponse> actualizarCantidad(@PathVariable Long productId,
                                                            @Valid @RequestBody UpdateCartItemRequest request) {
        cartUseCase.updateProductQuantity(productId, request.quantity());
        return ResponseEntity.ok(toCartResponse(cartUseCase.viewCart()));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CartResponse> eliminarProducto(@PathVariable Long productId) {
        cartUseCase.removeProduct(productId);
        return ResponseEntity.ok(toCartResponse(cartUseCase.viewCart()));
    }

    @PostMapping("/cerrar")
    public ResponseEntity<OrderSummaryResponse> cerrarPedido() {
        OrderSummary summary = cartUseCase.checkout();
        return ResponseEntity.ok(toOrderResponse(summary));
    }

    @PostMapping("/cancelar")
    public ResponseEntity<MessageResponse> cancelarPedido() {
        cartUseCase.cancelOrder();
        return ResponseEntity.ok(new MessageResponse("Pedido cancelado y carrito vaciado correctamente"));
    }

    private CartResponse toCartResponse(CartSummary summary) {
        List<CartItemResponse> items = summary.items().stream()
                .map(this::toCartItemResponse)
                .toList();
        return new CartResponse(items, summary.total());
    }

    private CartItemResponse toCartItemResponse(CartItem item) {
        return new CartItemResponse(
                item.product().id(),
                item.product().name(),
                item.product().category(),
                item.product().price(),
                item.quantity(),
                item.subtotal()
        );
    }

    private OrderSummaryResponse toOrderResponse(OrderSummary summary) {
        List<OrderItemResponse> items = summary.items().stream()
                .map(this::toOrderItemResponse)
                .toList();
        return new OrderSummaryResponse(
                summary.orderId(),
                summary.createdAt(),
                items,
                summary.totalBeforeDiscount(),
                summary.discount(),
                summary.totalToPay()
        );
    }

    private OrderItemResponse toOrderItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.productId(),
                item.productName(),
                item.category(),
                item.unitPrice(),
                item.quantity(),
                item.subtotal()
        );
    }
}
