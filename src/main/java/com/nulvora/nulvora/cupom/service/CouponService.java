package com.nulvora.nulvora.cupom.service;

import com.nulvora.nulvora.cupom.dto.request.CouponRequest;
import com.nulvora.nulvora.cupom.dto.request.CouponUpdateRequest;
import com.nulvora.nulvora.cupom.dto.response.CouponResponse;
import com.nulvora.nulvora.cupom.entity.Coupon;
import com.nulvora.nulvora.cupom.mapper.CouponMapper;
import com.nulvora.nulvora.cupom.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    // criar cupom de desconto
    public CouponResponse createCoupon(CouponRequest request){
        // cria o cupom
        Coupon coupon = couponMapper.toEntity(request);

        // gera o code automatico
        if (request.code().isBlank()){
            coupon.setCode(generateUniqueCode());
        }

        return couponMapper.toResponse(couponRepository.save(coupon));
    }

    // editar cupom de desconto
    @Transactional
    public CouponResponse updateCoupon(Long couponId, CouponUpdateRequest request){
        // encontra o cupom
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cupom não encontrado"));

        // verifica se o code ja exixte
        if (couponRepository.existsByCodeAndIdNot(request.code(), couponId)){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ja exixste um cupom com esse código");
        }

        // edita somente os campos não nulos
        couponMapper.updateEntity(request, coupon);

        return couponMapper.toResponse(couponRepository.save(coupon));
    }

    // gerar code aleatório
    private String generateCode() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }

    // caso o code aleatório ja exista, cria outro
    private String generateUniqueCode() {
        String code;

        do {
            code = generateCode();
        } while (couponRepository.existsByCode(code));

        return code;
    }
}
