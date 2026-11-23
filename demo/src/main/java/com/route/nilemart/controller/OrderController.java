package com.route.nilemart.controller;

import com.route.nilemart.checkout.OrderRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderRegistry orderRegistry;

    public OrderController(OrderRegistry orderRegistry) {
        this.orderRegistry = orderRegistry;
    }

    @GetMapping
    public String showOrders(Model model) {
        model.addAttribute("orders", orderRegistry.findAll());
        return "orders";
    }

    @GetMapping("/{orderNumber}")
    public String showOrder(
            @PathVariable String orderNumber,
            Model model) {

        model.addAttribute(
                "order",
                orderRegistry.findByNumber(orderNumber)
        );

        return "order-details";
    }
}