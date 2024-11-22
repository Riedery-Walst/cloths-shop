package ru.andreev.clothsshop.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.andreev.clothsshop.exception.UnauthorizedException;
import ru.andreev.clothsshop.model.Cart;
import ru.andreev.clothsshop.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Получить корзину текущего пользователя
    @GetMapping
    public Cart getCart(Authentication authentication) {
        if (authentication == null) {
            throw new UnauthorizedException("Пользователь не аутентифицирован");
        }
        String email = authentication.getName(); // Получаем email текущего пользователя
        return cartService.getCartByUser(email);
    }

    // Добавить продукт в корзину текущего пользователя
    @PostMapping("/add")
    public Cart addProductToCart(
            @RequestParam Long productId,
            @RequestParam int quantity,
            @RequestParam(required = false) Long colorId,
            @RequestParam(required = false) Long sizeId,
            Authentication authentication) {
        String email = authentication.getName();
        return cartService.addProductToCart(email, productId, quantity, colorId, sizeId);
    }

    // Удалить продукт из корзины текущего пользователя
    @DeleteMapping("/remove/{cartItemId}")
    public Cart removeProductFromCart(
            @PathVariable Long cartItemId,
            Authentication authentication) {
        String email = authentication.getName();
        return cartService.removeProductFromCart(email, cartItemId);
    }

    @PatchMapping("/updateQuantity/{cartItemId}")
    public Cart updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestParam int quantity,
            Authentication authentication) {
        String email = authentication.getName();
        return cartService.updateCartItemQuantity(email, cartItemId, quantity);
    }

    // Очистить корзину текущего пользователя
    @DeleteMapping("/clear")
    public Cart clearCart(Authentication authentication) {
        String email = authentication.getName();
        return cartService.clearCart(email);
    }
}