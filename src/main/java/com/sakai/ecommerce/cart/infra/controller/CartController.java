package com.sakai.ecommerce.cart.infra.controller;

import com.sakai.ecommerce.cart.application.dto.*;
import com.sakai.ecommerce.cart.application.commands.*;
import com.sakai.ecommerce.cart.application.handlers.*;
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
    private final GetCartHandler getCartHandler;

    @PostMapping("/items")
    public void addItem(@RequestBody AddItemRequest request) {
        var command = new AddItemToCartCommand(
                request.productId(),
                request.sku(),
                request.quantity()
        );

        cartItemHandler.addItem(command);
    }

    @PostMapping("/items/checkout")
    public CartCheckoutResponse addItemAndCheckout(@RequestBody AddItemRequest request) {
        var command = new AddItemAndCheckoutCommand(
                request.productId(),
                request.sku(),
                request.quantity()
        );

        return addItemAndCheckoutHandler.addItemAndCheckout(command);
    }

    @DeleteMapping("/items/{sku}")
    public void removeItem(@PathVariable String sku) {
        var command = new RemoveItemFromCartCommand(sku);
        removeCartItemHandler.removeItem(command);
    }

    @PatchMapping("/items/decrease")
    public void decreaseItemQuantity(@RequestBody DecreaseItemQuantityRequest request) {
        var command = new DecreaseItemQuantityCommand(
                request.sku(),
                request.quantity()
        );
        decreaseCartItemQuantityHandler.decreaseQuantity(command);
    }

    @PostMapping("/checkout")
    public void checkout() {
        checkoutHandler.handle();
    }

    @GetMapping
    public CartResponse getCart() {
        return getCartHandler.handle();
    }
}