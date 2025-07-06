package com.mark.arqhexexample.domain.port.input;

import com.mark.arqhexexample.domain.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductUseCase {
    Product createProduct(String name, String description, BigDecimal price, String sku, String stock, String imageUrl);

    Optional<Product> updateProduct(Long id, String name, String description, BigDecimal price, String sku, String stock, String imageUrl);

    boolean deleteProduct(Long id);

    Optional<Product> getProductById(Long id);

    List<Product> getAllProducts();
}
