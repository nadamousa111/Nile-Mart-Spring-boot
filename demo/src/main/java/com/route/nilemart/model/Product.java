package com.route.nilemart.model;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@Getter
@ToString
@Setter

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @EqualsAndHashCode.Include
    private final String SKU;

    private final String productName;
    private final BigDecimal price;
    private final Category category;
    private int stockQuantity;


}