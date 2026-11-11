package com.nulvora.nulvora.productVariant.controller;

import com.nulvora.nulvora.productImg.dto.request.ProductImgUpdateRequest;
import com.nulvora.nulvora.productImg.dto.response.ProductImgResponse;
import com.nulvora.nulvora.productVariant.dto.request.ProductVariantRequest;
import com.nulvora.nulvora.productVariant.dto.request.ProductVariantUpdateRequest;
import com.nulvora.nulvora.productVariant.dto.response.ProductVariantResponse;
import com.nulvora.nulvora.productVariant.service.ProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product-variant")
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    @PostMapping("/{productId}/new/variants")
    public ResponseEntity<ProductVariantResponse> createVariant(
            @PathVariable Long productId,
            @Valid @RequestBody ProductVariantRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productVariantService.createVariant(productId, request));
    }


    @PutMapping("/{variantId}/update")
    public ResponseEntity<ProductVariantResponse> updateVariant(
            @PathVariable Long variantId,
            @Valid @RequestBody ProductVariantUpdateRequest request
    ) {
        return ResponseEntity.ok(productVariantService.updateVariant(variantId, request));
    }

    @PutMapping("/{variantId}/img/update")
    public ResponseEntity<ProductImgResponse> updateVariantImg(
            @PathVariable Long variantId,
            @Valid @RequestBody ProductImgUpdateRequest request
    ) {
        return ResponseEntity.ok(productVariantService.updateVariantImg(variantId, request));

    }

}
