package com.sakai.ecommerce.cart.infra.controller;

import com.sakai.ecommerce.cart.application.dto.*;
import com.sakai.ecommerce.cart.application.commands.*;
import com.sakai.ecommerce.cart.application.handlers.*;
import com.sakai.ecommerce.cart.application.queries.GetCartQuery;
import com.sakai.ecommerce.cart.infra.requests.AddItemRequest;
import com.sakai.ecommerce.cart.infra.requests.DecreaseItemQuantityRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final AddCartItemHandler cartItemHandler;
    private final RemoveCartItemHandler removeCartItemHandler;
    private final DecreaseCartItemQuantityHandler decreaseCartItemQuantityHandler;
    private final AddItemAndCheckoutHandler addItemAndCheckoutHandler;
    private final CheckoutHandler checkoutHandler;
    private final AssignCartHandler assignCartHandler;
    private final GetCartHandler getCartHandler;

    @PostMapping("/items")
    public void addItem(
            @RequestHeader(value = "X-User-Id", required = false) UUID userId,
            HttpSession session,
            @RequestBody AddItemRequest request
    ) {
        var command = new AddItemToCartCommand(
                userId,
                session.getId(),
                request.productId(),
                request.sku(),
                request.quantity()
        );

        cartItemHandler.addItem(command);
    }

    @PostMapping("/items/checkout")
    public CartCheckoutResponse addItemAndCheckout(
            @RequestHeader(value = "X-User-Id", required = false) UUID userId,
            HttpSession session,
            @RequestBody AddItemRequest request
    ) {
        var command = new AddItemAndCheckoutCommand(
                userId,
                session.getId(),
                request.productId(),
                request.sku(),
                request.quantity()
        );

        return addItemAndCheckoutHandler.addItemAndCheckout(command);
    }

    @DeleteMapping("/items/{sku}")
    public void removeItem(
            @RequestHeader(value = "X-User-Id", required = false) UUID userId,
            HttpSession session,
            @PathVariable String sku
    ) {
        var command = new RemoveItemFromCartCommand(userId, session.getId(), sku);
        removeCartItemHandler.removeItem(command);
    }

    @PatchMapping("/items/decrease")
    public void decreaseItemQuantity(
            @RequestHeader(value = "X-User-Id", required = false) UUID userId,
            HttpSession session,
            @RequestBody DecreaseItemQuantityRequest request
    ) {
        var command = new DecreaseItemQuantityCommand(
                userId,
                session.getId(),
                request.sku(),
                request.quantity()
        );
        decreaseCartItemQuantityHandler.decreaseQuantity(command);
    }

    @PostMapping("/checkout")
    public void checkout(
            @RequestHeader(value = "X-User-Id", required = false) UUID userId
    ) {
        var command = new CheckoutCommand(userId);
        checkoutHandler.handle(command);
    }

    @PostMapping("/assign")
    public void assignCart(
            @RequestHeader(value = "X-User-Id") UUID userId,
            HttpSession session
    ) {
        var command = new AssignCartCommand(userId, session.getId());
        assignCartHandler.assign(command);
    }

    @GetMapping
    public CartResponse getCart(
            @RequestHeader(value = "X-User-Id", required = false) UUID userId,
            HttpSession session
    ) {
        var query = new GetCartQuery(userId, session.getId());
        return getCartHandler.handle(query);
    }
}