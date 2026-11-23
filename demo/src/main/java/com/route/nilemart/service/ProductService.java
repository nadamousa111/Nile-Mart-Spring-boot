package com.route.nilemart.service;

import com.route.nilemart.common.Check;
import com.route.nilemart.common.NotFoundException;
import com.route.nilemart.common.OutOfStockException;
import com.route.nilemart.model.Category;
import com.route.nilemart.model.Product;
import com.route.nilemart.pricing.PricingService;
import com.route.nilemart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final PricingService pricingService;

    public ProductService(
            ProductRepository productRepository,
            PricingService pricingService
    ) {
        this.productRepository = productRepository;
        this.pricingService = pricingService;
    }

    public List<Product> findAll() {

        return productRepository.findAll().stream()
                .sorted(
                        Comparator.comparing(Product::getCategory)
                                .thenComparing(Product::getProductName)
                )
                .toList();
    }

    public Product findBySKU(String SKU) {

        return Check.found(
                productRepository.findBySKU(SKU),
                () -> new NotFoundException(
                        "Product " + SKU + " was not found."
                )
        );
    }

    public BigDecimal getPrice(String SKU, int quantity) {

        Product product = findBySKU(SKU);

        return pricingService.getPrice(product, quantity);
    }

    public void reduceStock(String SKU, int quantity) {

        Product product = findBySKU(SKU);

        if (product.getStockQuantity() < quantity) {
            throw new OutOfStockException(
                    "Only " + product.getStockQuantity()
                            + " available for " + product.getProductName() + "."
            );
        }

        product.setStockQuantity(
                product.getStockQuantity() - quantity
        );
    }

    public List<Product> search(Predicate<Product> predicate) {

        return findAll().stream()
                .filter(predicate)
                .toList();
    }

    public Map<Category, List<Product>> groupByCategory() {

        return findAll().stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        () -> new EnumMap<>(Category.class),
                        Collectors.toList()
                ));
    }
}