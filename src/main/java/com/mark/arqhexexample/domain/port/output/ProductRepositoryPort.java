package com.mark.arqhexexample.domain.port.output;

import com.mark.arqhexexample.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
    Product save(Product product);
    Optional<Product> getById(Long id);
    List<Product> getAll();
    void deleteById(Long id);
}
