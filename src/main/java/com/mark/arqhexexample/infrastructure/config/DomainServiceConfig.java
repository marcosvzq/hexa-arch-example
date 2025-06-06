package com.mark.arqhexexample.infrastructure.config;

import com.mark.arqhexexample.application.ProductUseCaseImpl;
import com.mark.arqhexexample.domain.port.input.ProductUseCase;
import com.mark.arqhexexample.domain.port.output.ProductRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

    @Bean
    public ProductUseCase productUseCase(ProductRepositoryPort productRepositoryPort) {
        return new ProductUseCaseImpl(productRepositoryPort);
    }
}
