package com.nulvora.nulvora.product.controller;

import com.nulvora.nulvora.product.dto.request.ProductRequest;
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
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/new")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @GetMapping
    public ResponseEntity<Page<ProductSumaryResponse>> getAllProducts(
            @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));

    }

}
