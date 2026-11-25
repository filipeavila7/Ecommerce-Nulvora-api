package com.nulvora.nulvora.promotions.controller;

import com.nulvora.nulvora.promotions.dto.request.PromotionRequest;
import com.nulvora.nulvora.promotions.dto.request.PromotionUpdateRequest;
import com.nulvora.nulvora.promotions.dto.response.PromotionDetailsResponse;
import com.nulvora.nulvora.promotions.dto.response.PromotionResponse;
import com.nulvora.nulvora.promotions.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/promotions")
public class PromotionController {

    private final PromotionService promotionService;


    @GetMapping
    public ResponseEntity<Page<PromotionResponse>> getAllPromotions(
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(promotionService.getAllPromotions(pageable));
    }

    @GetMapping("/{promotionId}")
    public ResponseEntity<PromotionDetailsResponse> getPromotionDetails(
            @PathVariable Long promotionId
    ) {
        return ResponseEntity.ok(promotionService.getPromotionDetails(promotionId));
    }

    @PostMapping("/new")
    public ResponseEntity<PromotionResponse> createPromotion(
            @Valid @RequestBody PromotionRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(promotionService.createPromotion(request));
    }

    @PatchMapping("/update/{promotionId}")
    public ResponseEntity<PromotionResponse> updatePromotion(
            @Valid @RequestBody PromotionUpdateRequest request,
            @PathVariable Long promotionId
    ) {
        return ResponseEntity.ok(
                promotionService.updatePromotion(request, promotionId)
        );
    }
}
