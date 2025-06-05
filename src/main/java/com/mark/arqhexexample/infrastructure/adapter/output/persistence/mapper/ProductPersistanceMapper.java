package com.mark.arqhexexample.infrastructure.adapter.output.persistence.mapper;

import com.mark.arqhexexample.domain.model.Product;
import com.mark.arqhexexample.infrastructure.adapter.output.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductPersistanceMapper {
    public ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getSku(),
                product.getStock(),
                product.getImageUrl()
        );
    }

    public Product toDomain(ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getDescription(),
                productEntity.getPrice(),
                productEntity.getSku(),
                productEntity.getStock(),
                productEntity.getImageUrl()
        );
    }
}
