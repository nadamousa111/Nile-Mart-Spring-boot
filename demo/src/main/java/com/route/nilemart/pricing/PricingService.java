package com.route.nilemart.pricing;

import com.route.nilemart.model.Product;

import java.math.BigDecimal;

public interface PricingService {

    BigDecimal getPrice(Product product, int quantity);
}