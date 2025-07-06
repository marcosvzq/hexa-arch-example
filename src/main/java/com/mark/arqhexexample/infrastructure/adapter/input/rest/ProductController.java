package com.mark.arqhexexample.infrastructure.adapter.input.rest;

import com.mark.arqhexexample.domain.exception.ProductNotFoundException;
import com.mark.arqhexexample.domain.model.Product;
import com.mark.arqhexexample.domain.port.input.ProductUseCase;
import com.mark.arqhexexample.infrastructure.adapter.input.rest.dto.ProductRequestDTO;
import com.mark.arqhexexample.infrastructure.adapter.input.rest.dto.ProductResponseDTO;
import com.mark.arqhexexample.infrastructure.adapter.input.rest.mapper.ProductApiMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductUseCase productUseCase;
    private final ProductApiMapper productApiMapper;

    public ProductController(ProductUseCase productUseCase, ProductApiMapper productApiMapper) {
        this.productUseCase = productUseCase;
        this.productApiMapper = productApiMapper;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        Product createdProduct = productUseCase.createProduct(productRequestDTO.name(),
                                                              productRequestDTO.description(),
                                                              productRequestDTO.price(),
                                                              productRequestDTO.sku(),
                                                              productRequestDTO.stock(),
                                                              productRequestDTO.imageUrl());
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(productApiMapper.toResponseDto(createdProduct));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return productUseCase.getProductById(id)
                .map(productApiMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ProductNotFoundException("Product with " + id + " not found"));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<Product> products = productUseCase.getAllProducts();
        return ResponseEntity.ok(productApiMapper.toResponseDtoList(products));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO productRequestDTO) {
        return productUseCase.updateProduct(
                id,
                productRequestDTO.name(),
                productRequestDTO.description(),
                productRequestDTO.price(),
                productRequestDTO.sku(),
                productRequestDTO.stock(),
                productRequestDTO.imageUrl()
                ).map(productApiMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productUseCase.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }
    }

}
