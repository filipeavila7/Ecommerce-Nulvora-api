package com.nulvora.nulvora.wishListItem.repository;


import com.nulvora.nulvora.wishListItem.entity.WishListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishListItemRepository extends JpaRepository<WishListItem, Long> {

    boolean existsByWishListIdAndProductId(Long wishListId, Long productId);

    Page<WishListItem> findByWishListId(Long wishListId, Pageable pageable);

    Optional<WishListItem> findByIdAndWishListId(Long wishListItemId, Long wishListId);

    Optional<WishListItem> findByWishListIdAndProductId(Long wishListId, Long productId);


}
