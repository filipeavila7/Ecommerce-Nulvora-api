package com.nulvora.nulvora.promotions.service;

import com.nulvora.nulvora.coupon.entity.DiscountType;
import com.nulvora.nulvora.promotions.dto.request.PromotionRequest;
import com.nulvora.nulvora.promotions.dto.request.PromotionUpdateRequest;
import com.nulvora.nulvora.promotions.dto.response.PromotionResponse;
import com.nulvora.nulvora.promotions.entity.Promotion;
import com.nulvora.nulvora.promotions.mapper.PromotionMapper;
import com.nulvora.nulvora.promotions.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    // TODO - validar valores quando for aplicar promoções
    // TODO - (o valor nunca pode ficar negativo ou 0, impedir editar o valor de variantes quando o resultado ficar
    //  menor ou 0 quando aplicar a promoção se o tipo for FIXED_AMOUNT.)

    // criar promoções
    public PromotionResponse createPromotion(PromotionRequest request){
        Promotion promotion = promotionMapper.toEntity(request);

        // se o desconto for tipo porcentagem, evita ser 100%
        if (request.discountType() == DiscountType.PERCENTAGE
                && request.discountValue().compareTo(BigDecimal.valueOf(100)) >= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"O desconto percentual deve ser inferior a 100%");
        }

        return promotionMapper.toPromotionResponse(promotionRepository.save(promotion));
    }


    // editar promoções
    public PromotionResponse updatePromotion(PromotionUpdateRequest request, Long promotionId){
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Promoção não encontrada"));

        // atualiza somente os campos não nulos
        promotionMapper.updateEntity(request, promotion);

        // verifica se a data de expiração é posterior à data de início
        if (!promotion.getExpireAt().isAfter(promotion.getStartAt())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A data de expiração deve ser posterior à data de início"
            );
        }

        return promotionMapper.toPromotionResponse(
                promotionRepository.save(promotion)
        );

    }


}
