package com.route.nilemart.controller;

import com.route.nilemart.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String showProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products";
    }

    @GetMapping("/{sku}")
    public String showProduct(
            @PathVariable String sku,
            Model model) {

        model.addAttribute("product",
                productService.findBySKU(sku));

        return "product-details";
    }
}