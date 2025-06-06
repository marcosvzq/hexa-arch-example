package com.mark.arqhexexample.infrastructure.adapter.input.rest.dto;

import java.math.BigDecimal;

public record ProductResponseDTO(Long id, String name, String description, BigDecimal price, String sku, String stock, String imageUrl) {
}
