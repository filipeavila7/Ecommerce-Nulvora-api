package com.nulvora.nulvora.wishList.repository;

import com.nulvora.nulvora.wishList.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    Optional<WishList> findByUserId(Long userId);
}
