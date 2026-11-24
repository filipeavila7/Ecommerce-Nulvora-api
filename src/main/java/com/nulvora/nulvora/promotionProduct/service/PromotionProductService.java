package com.nulvora.nulvora.promotionProduct.service;

import com.nulvora.nulvora.helper.GlobalHelperService;
import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.promotionProduct.dto.response.PromotionProductResponse;
import com.nulvora.nulvora.promotionProduct.entity.PromotionProduct;
import com.nulvora.nulvora.promotionProduct.mapper.PromotionProductMapper;
import com.nulvora.nulvora.promotionProduct.repository.PromotionProductRepository;
import com.nulvora.nulvora.promotions.entity.Promotion;
import com.nulvora.nulvora.promotions.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PromotionProductService {
    private final PromotionProductRepository promotionProductRepository;
    private final PromotionProductMapper promotionProductMapper;
    private final PromotionService promotionService;
    private final GlobalHelperService globalHelperService;

    // adcionar uma promoção a um produto
    public PromotionProductResponse crateProductPromotion(Long productId, Long promotionId){
        // pega o produto
        Product product = globalHelperService.getActiveProductOrThrow(productId);

        // pega a promoção e ja valida ela
        Promotion promotion = promotionService.validatePromotion(promotionId);

        // procura relação, se não achar, cria uma nova
        PromotionProduct association = promotionProductRepository
                .findByProduct(product)
                .orElseGet(() -> {
                    PromotionProduct newAssociation = new PromotionProduct();
                    newAssociation.setProduct(product);
                    return newAssociation;
                });

        // valida a promoção atual
        validateCurrentPromotion(association.getPromotion());

        // substitui ou cria nova relação com promoção
        association.setPromotion(promotion);

       return promotionProductMapper.toPromotionProductResponse(promotionProductRepository.save(association));
    }

    // validar promoção atual
    private void validateCurrentPromotion(Promotion currentPromotion) {

        if (currentPromotion == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        boolean isValid = currentPromotion.isActive()
                && !now.isBefore(currentPromotion.getStartAt())
                && now.isBefore(currentPromotion.getExpireAt());

        if (isValid) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Esse produto já possui uma promoção vigente"
            );
        }
    }
}
