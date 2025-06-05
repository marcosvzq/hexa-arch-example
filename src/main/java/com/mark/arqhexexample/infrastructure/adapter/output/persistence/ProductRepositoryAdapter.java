package com.mark.arqhexexample.infrastructure.adapter.output.persistence;

import com.mark.arqhexexample.domain.model.Product;
import com.mark.arqhexexample.domain.port.output.ProductRepositoryPort;
import com.mark.arqhexexample.infrastructure.adapter.output.persistence.entity.ProductEntity;
import com.mark.arqhexexample.infrastructure.adapter.output.persistence.mapper.ProductPersistanceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductSpringDataRepository jpaRepository;
    private final ProductPersistanceMapper mapper;

    public ProductRepositoryAdapter(ProductSpringDataRepository jpaRepository, ProductPersistanceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Product save(Product product) {
        ProductEntity productEntity = mapper.toEntity(product);
        ProductEntity savedEntity = jpaRepository.save(productEntity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> getById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Product> getAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
