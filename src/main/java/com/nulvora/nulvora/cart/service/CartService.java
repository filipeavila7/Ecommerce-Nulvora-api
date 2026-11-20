package com.nulvora.nulvora.cart.service;

import com.nulvora.nulvora.cart.entity.Cart;
import com.nulvora.nulvora.cart.repository.CartRepository;
import com.nulvora.nulvora.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    // criar carrinho do usuario na hora da criação de novo usuario
    public void createCartForUser(User user){
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        cartRepository.save(cart);
    }
}
