
package com.route.nilemart.repository;

import com.route.nilemart.common.StoreException;
import com.route.nilemart.model.Category;
import com.route.nilemart.model.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final ConcurrentHashMap<String, Product> productList =
            new ConcurrentHashMap<>();

    @PostConstruct
    public void seedProducts() {

        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("products.csv")) {

            if (inputStream == null) {
                throw new IOException("products.csv not found");
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            inputStream,
                            StandardCharsets.UTF_8))) {

                reader.readLine();

                String line;

                while ((line = reader.readLine()) != null) {

                    if (line.isBlank()) {
                        continue;
                    }

                    String[] data = line.split(",", -1);

                    Product product = new Product(
                            data[0].trim(),
                            data[1].trim(),
                            new BigDecimal(data[2].trim()),
                            Category.valueOf(data[3].trim()),
                            Integer.parseInt(data[4].trim())
                    );

                    save(product);
                }
            }

            System.out.println(
                    "Catalog loaded: " + productList.size() + " products"
            );

        } catch (IOException e) {
            throw new StoreException(
                    "The catalog could not be loaded.", e
            );
        }
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(productList.values());
    }

    @Override
    public Optional<Product> findById(String SKU) {
        return Optional.ofNullable(productList.get(SKU));
    }

    @Override
    public Product save(Product product) {
        productList.put(product.getSKU(), product);
        return product;
    }
}

