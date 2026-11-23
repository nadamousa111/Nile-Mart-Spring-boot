package com.route.nilemart.repository;

import com.route.nilemart.model.Product;
import java.util.Optional;

public interface ProductRepository extends Repository<Product, String> {

    default Optional<Product> findBySKU(String SKU) {
        return findById(SKU);
    }
}