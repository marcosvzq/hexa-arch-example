package com.mark.arqhexexample.application;

import com.mark.arqhexexample.domain.model.Product;
import com.mark.arqhexexample.domain.port.input.ProductUseCase;
import com.mark.arqhexexample.domain.port.output.ProductRepositoryPort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ProductUseCaseImpl implements ProductUseCase {
    private final ProductRepositoryPort productRepositoryPort;

    public ProductUseCaseImpl(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }


    @Override
    public Product createProduct(String name, String description, String price, String sku, String stock, String imageUrl) {
        Product product = new Product(null, name, description, new BigDecimal(price), sku, stock, imageUrl);

        // Add validation logic here if needed

        return productRepositoryPort.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepositoryPort.getById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepositoryPort.getAll();
    }

    @Override
    public Optional<Product> updateProduct(Long id, String name, String description, String price, String sku, String stock, String imageUrl) {
        return productRepositoryPort.getById(id).
                map(ProductExists -> {
                    ProductExists.setName(name);
                    ProductExists.setDescription(description);
                    ProductExists.setPrice(new BigDecimal(price));
                    ProductExists.setSku(sku);
                    ProductExists.setStock(stock);
                    ProductExists.setImageUrl(imageUrl);
                    return productRepositoryPort.save(ProductExists);
                });
    }

    @Override
    public boolean deleteProduct(Long id) {
        if (productRepositoryPort.getById(id).isPresent()) {
            productRepositoryPort.deleteById(id);
            return true;
        }
        return false;
    }

}
