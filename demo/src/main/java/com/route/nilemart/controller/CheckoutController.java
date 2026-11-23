package com.route.nilemart.controller;

import com.route.nilemart.cart.CartRegistry;
import com.route.nilemart.cart.ShoppingCart;
import com.example.demo.service.CheckoutService;
import com.route.nilemart.model.Governorate;
import com.route.nilemart.model.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final CartRegistry cartRegistry;
    private final CheckoutService checkoutService;

    public CheckoutController(
            CartRegistry cartRegistry,
            CheckoutService checkoutService) {

        this.cartRegistry = cartRegistry;
        this.checkoutService = checkoutService;
    }

    @GetMapping("/{cartId}")
    public String showCheckout(
            @PathVariable int cartId,
            Model model) {

        ShoppingCart cart = cartRegistry.getCart(cartId);

        model.addAttribute("cart", cart);
        model.addAttribute("governorates", Governorate.values());

        return "checkout";
    }

    @PostMapping("/{cartId}")
    public String placeOrder(
            @PathVariable int cartId,
            @ModelAttribute Customer customer,
            @RequestParam Governorate governorate) {

        ShoppingCart cart = cartRegistry.getCart(cartId);

        var order = checkoutService.placeOrder(
                cart, customer, governorate
        );

        return "redirect:/orders/" + order.orderNumber();
    }
}