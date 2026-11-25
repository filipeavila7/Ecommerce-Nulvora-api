package com.nulvora.nulvora.product.controller;

import com.nulvora.nulvora.product.dto.request.ProductRequest;
import com.nulvora.nulvora.product.dto.request.ProductUpdateRequest;
import com.nulvora.nulvora.product.dto.response.ProductDetailsResponse;
import com.nulvora.nulvora.product.dto.response.ProductResponse;

import com.nulvora.nulvora.product.dto.response.ProductSumaryResponse;
import com.nulvora.nulvora.product.service.ProductService;
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
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping()
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @GetMapping()
    public ResponseEntity<Page<ProductSumaryResponse>> getAllProducts(
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));

    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailsResponse> getProductDetails(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(
                productService.getProductDetails(productId)
        );
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductUpdateRequest request
    ){
        return ResponseEntity.ok(productService.updateProduct(productId, request));
    }


    @GetMapping("/search")
    public ResponseEntity<Page<ProductSumaryResponse>> search(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 12) Pageable pageable
    ) {
        return ResponseEntity.ok(
                productService.search(search, pageable)
        );
    }


}
