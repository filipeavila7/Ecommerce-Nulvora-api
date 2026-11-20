package com.nulvora.nulvora.cartItens.service;

import com.nulvora.nulvora.cart.entity.Cart;
import com.nulvora.nulvora.cart.repository.CartRepository;
import com.nulvora.nulvora.cartItens.dto.request.CartItemRequest;
import com.nulvora.nulvora.cartItens.dto.response.CartItemResponse;
import com.nulvora.nulvora.cartItens.entity.CartItem;
import com.nulvora.nulvora.cartItens.mapper.CartItemMapper;
import com.nulvora.nulvora.cartItens.repository.CartItemRepository;
import com.nulvora.nulvora.helper.GlobalHelperService;
import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import com.nulvora.nulvora.productVariant.repository.ProductVariantRepository;
import com.nulvora.nulvora.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductVariantRepository productVariantRepository;
    private final GlobalHelperService globalHelperService;
    private final CartItemMapper cartItemMapper;

    // TODO - no get dos itens no carrinho, verificar se a variante esta disponivel ou se tem estoque suficiente
    // TODO - verificar tb antes de finalizar a compra e tb se atentar a compras simultâneas

    // adcioanar itens no carrinho
    @Transactional
    public CartItemResponse addItensInCart(CartItemRequest request){
        User loggedUser = globalHelperService.getLoggedUser();

        // pega o carrinho do usuario
        Cart cart = cartRepository.findByUserId(loggedUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Carrinho não encontrado"));


        // procura a variante que vai ser adcionada
        ProductVariant productVariant = productVariantRepository.findByIdAndActiveTrue(request.productVariantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto não encontrado"));


        // Procura o item no carrinho ou cria um novo
        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductVariantId(cart.getId(), productVariant.getId())
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProductVariant(productVariant);
                    newItem.setQuantity(0);
                    return newItem;
                });

        int newQuantity = cartItem.getQuantity() + request.quantity();

        if (newQuantity > productVariant.getStockQuantity()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Quantidade solicitada maior que o estoque disponível."
            );
        }

        // Atualiza a quantidade
        cartItem.setQuantity(newQuantity);

        // calcula o subtotal com o preço atual do produto
        BigDecimal subTotal = productVariant.getPrice()
                .multiply(BigDecimal.valueOf(request.quantity()));


        return cartItemMapper.toResponse(cartItemRepository.save(cartItem), subTotal);
    }

}
