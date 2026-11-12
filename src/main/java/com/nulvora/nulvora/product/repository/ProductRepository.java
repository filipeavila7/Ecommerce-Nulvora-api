package com.nulvora.nulvora.product.repository;

import com.nulvora.nulvora.product.dto.response.ProductSumaryResponse;
import com.nulvora.nulvora.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByIdAndActiveTrue(Long productId);

    Page<Product> findAllByActiveTrue(Pageable pageable);

    // retorna todos os produtos ativos ja montando o dto e com seus respectivos preços minimos dentre as variantes
    @Query("""
    SELECT new com.nulvora.nulvora.product.dto.response.ProductSumaryResponse(
        p.id,
        p.name,
        p.imageUrl,
        MIN(v.price)
    )
    FROM Product p
    LEFT JOIN p.productVariants v ON v.active = true
    WHERE p.active = true
    GROUP BY p.id, p.name, p.imageUrl
""")
    Page<ProductSumaryResponse> findAllSummaries(Pageable pageable);


}
