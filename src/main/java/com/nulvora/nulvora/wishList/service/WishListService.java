package com.nulvora.nulvora.wishList.service;

import com.nulvora.nulvora.user.entity.User;
import com.nulvora.nulvora.wishList.entity.WishList;
import com.nulvora.nulvora.wishList.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishListService {
    private final WishListRepository wishListRepository;

    // criar lista de desejos para o usuario
    public void createWishListForUser(User user){
        WishList wishList = new WishList();

        wishList.setUser(user);
        user.setWishList(wishList);

        wishListRepository.save(wishList);
    }
}
