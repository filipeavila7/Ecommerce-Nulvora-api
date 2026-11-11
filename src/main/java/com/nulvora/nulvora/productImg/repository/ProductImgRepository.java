package com.nulvora.nulvora.productImg.repository;

import com.nulvora.nulvora.productImg.entity.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductImgRepository extends JpaRepository<ProductImg, Long> {

    Optional<ProductImg> findByIdAndProductVariantId(Long imgId, Long variantId);

    @Modifying
    @Query("""
    UPDATE ProductImg img
      SET img.primary = false
      WHERE img.productVariant.id = :variantId
      AND img.id <> :imageId
      AND img.primary = true
""")
    void removePrimaryFromOthers(Long variantId, Long imageId);
}
