package com.route.nilemart.controller;

import com.route.nilemart.cart.CartRegistry;
import com.route.nilemart.cart.ShoppingCart;
import com.route.nilemart.controller.form.AddItemForm;
import com.route.nilemart.controller.form.NewCartForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/carts")
public class CartController {

    private final CartRegistry cartRegistry;

    public CartController(CartRegistry cartRegistry) {
        this.cartRegistry = cartRegistry;
    }

    @GetMapping("/new")
    public String newCartForm(Model model) {
        model.addAttribute("form", new NewCartForm());
        return "cart-new";
    }

    @PostMapping
    public String createCart(@ModelAttribute("form") NewCartForm form) {
        ShoppingCart cart = cartRegistry.createCart();
        return "redirect:/carts/" + cart.getId();
    }

    @GetMapping("/{cartId}")
    public String showCart(
            @PathVariable int cartId,
            Model model) {

        ShoppingCart cart = cartRegistry.getCart(cartId);

        model.addAttribute("cart", cart);
        model.addAttribute("form", new AddItemForm());

        return "cart";
    }

    @PostMapping("/{cartId}/items")
    public String addItem(
            @PathVariable int cartId,
            @ModelAttribute("form") AddItemForm form) {

        ShoppingCart cart = cartRegistry.getCart(cartId);

        cart.addItem(form.getSKU(), form.getQuantity());

        return "redirect:/carts/" + cartId;
    }
}