package com.mark.arqhexexample.infrastructure.adapter.output.persistence;

import com.mark.arqhexexample.infrastructure.adapter.output.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSpringDataRepository extends JpaRepository<ProductEntity, Long> {

}
