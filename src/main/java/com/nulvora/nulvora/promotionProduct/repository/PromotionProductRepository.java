package com.nulvora.nulvora.promotionProduct.repository;

import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.promotionProduct.entity.PromotionProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionProductRepository extends JpaRepository<PromotionProduct, Long> {

    Optional<PromotionProduct> findByProduct(Product product);

    void deleteAllByProductCategoryId(Long categoryId);

    void deleteAllByProductCategoryIdAndPromotionId(
            Long categoryId,
            Long promotionId
    );
}
