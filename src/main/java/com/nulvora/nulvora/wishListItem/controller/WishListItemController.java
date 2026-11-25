package com.nulvora.nulvora.wishListItem.controller;

import com.nulvora.nulvora.wishListItem.dto.response.WishListItemResponse;
import com.nulvora.nulvora.wishListItem.service.WishListItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wish-list-item")
@RequiredArgsConstructor
public class WishListItemController {
    private final WishListItemService wishListItemService;

    // listar produtos da lista de desejos
    @GetMapping()
    public ResponseEntity<Page<WishListItemResponse>> getMyWishListItems(
            @PageableDefault(size = 12, sort = "createdAt",
                    direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(wishListItemService.getMyWishListItems(pageable));

    }

    // adicionar produtos dentro da lista de desejos
    @PostMapping("/{productId}")
    public ResponseEntity<WishListItemResponse> addItemInWishList(
            @PathVariable Long productId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(wishListItemService.addItemInWishList(productId));
    }

    // remover dentro da lista de desejos
    @DeleteMapping("/{wishListItemId}")
    public ResponseEntity<Void> removeItemFromWishListIn(
            @PathVariable Long wishListItemId
    ) {
        wishListItemService.removeItemFromWishListIn(wishListItemId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // remover fora da lista de desejos
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> removeItemFromWishList(
            @PathVariable Long productId
    ) {
        wishListItemService.removeItemFromWishList(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
