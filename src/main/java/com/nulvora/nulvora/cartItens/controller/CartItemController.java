package com.nulvora.nulvora.cartItens.controller;

import com.nulvora.nulvora.cartItens.dto.request.CartItemRequest;
import com.nulvora.nulvora.cartItens.dto.request.CartItemUpdateRequest;
import com.nulvora.nulvora.cartItens.dto.response.CartItemPostResponse;
import com.nulvora.nulvora.cartItens.dto.response.CartItemResponse;
import com.nulvora.nulvora.cartItens.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart-item")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;


    @GetMapping("/my")
    public ResponseEntity<Page<CartItemResponse>> getMyCartItems(
            Pageable pageable
    ) {
        return ResponseEntity.ok(cartItemService.getMyCartItems(pageable));
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemPostResponse> addItemsInCart(
            @RequestBody @Valid CartItemRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartItemService.addItensInCart(request));
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<CartItemPostResponse> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestBody @Valid CartItemUpdateRequest request
    ) {
        return ResponseEntity.ok(
                cartItemService.updateCartItemQuantity(request, cartItemId)
        );
    }


}
