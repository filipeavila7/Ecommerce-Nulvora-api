package com.nulvora.nulvora.cartItens.service;


import com.nulvora.nulvora.cart.entity.Cart;
import com.nulvora.nulvora.cart.repository.CartRepository;
import com.nulvora.nulvora.cartItens.dto.request.CartItemRequest;
import com.nulvora.nulvora.cartItens.dto.request.CartItemUpdateRequest;
import com.nulvora.nulvora.cartItens.dto.response.CartItemPostResponse;
import com.nulvora.nulvora.cartItens.dto.response.CartItemResponse;
import com.nulvora.nulvora.cartItens.dto.response.CartItemUnavailableReason;
import com.nulvora.nulvora.cartItens.entity.CartItem;
import com.nulvora.nulvora.cartItens.mapper.CartItemMapper;
import com.nulvora.nulvora.cartItens.repository.CartItemRepository;
import com.nulvora.nulvora.helper.GlobalHelperService;
import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import com.nulvora.nulvora.productVariant.mapper.ProductVariantMapper;
import com.nulvora.nulvora.productVariant.repository.ProductVariantRepository;
import com.nulvora.nulvora.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final ProductVariantMapper productVariantMapper;



    // TODO - verificar tb antes de finalizar a compra e tb se atentar a compras simultâneas

    // adcioanar itens no carrinho
    @Transactional
    public CartItemPostResponse addItensInCart(CartItemRequest request){
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
                .multiply(BigDecimal.valueOf(newQuantity));


        return cartItemMapper.toResponse(cartItemRepository.save(cartItem), subTotal);
    }

    // get de todos os itens do carrinho
    public Page<CartItemResponse> getMyCartItems(Pageable pageable){
        User loggedUser = globalHelperService.getLoggedUser();

        // pega o carrinho do usuario
        Cart cart = cartRepository.findByUserId(loggedUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Carrinho não encontrado"));

        // pega todos os itens paginados
        Page<CartItem> cartItens = cartItemRepository.findByCartId(cart.getId(), pageable);

        // percorre eles
        return cartItens.map(item -> {
                    // pega a variante
                    ProductVariant variant = item.getProductVariant();

                    // calcula subtotal
                    BigDecimal subTotal = variant.getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));


                    // caso a variante esteja inativa mas ainda dentro do carrinho ou não tenha estoque o suficiente
                    CartItemUnavailableReason reason = checkAvailability(item);

                    boolean available = reason == null;

                     // monsta o dto
                    return new CartItemResponse(
                            item.getId(),
                            productVariantMapper.toProductVariantCartResponse(variant),
                            item.getQuantity(),
                            subTotal,
                            available,
                            reason
                    );

                });
    }


    // adiconar ou remover quantidade de variantes dentro do carrinho
    @Transactional
    public CartItemPostResponse updateCartItemQuantity(CartItemUpdateRequest request, Long cartItemId){
        User loggedUser = globalHelperService.getLoggedUser();

        // pega o carrinho do usuario
        Cart cart = cartRepository.findByUserId(loggedUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Carrinho não encontrado"));

        // acha a relação
        CartItem cartItem = cartItemRepository.findByIdAndCartId(cartItemId, cart.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Item não encontrado"));

        // pega a variante
        ProductVariant productVariant = productVariantRepository.
                findByIdAndActiveTrue(cartItem.getProductVariant().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Variante não encontrada"));

        // caso a quantidade seja maior do que a do estoque disponível da variante
        if (request.quantity() > productVariant.getStockQuantity()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Estoque insuficiente");
        }

        // seta a nova quantidade
        cartItem.setQuantity(request.quantity());

        // calcula subtotal
        BigDecimal subtotal = productVariant.getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return cartItemMapper.toResponse(cartItem, subtotal);

    }


    // checa se variante esta inativa ou não tenha estoque o suficiente mas ainda esta dentro do carrinho
    private CartItemUnavailableReason checkAvailability(CartItem cartItem) {
        ProductVariant variant = cartItem.getProductVariant();

        if (!variant.isActive()) {

            return CartItemUnavailableReason.INACTIVE;

        }

        if (variant.getStockQuantity() < cartItem.getQuantity()) {

            return CartItemUnavailableReason.OUT_OF_STOCK;
        }

        return null;
    }

}
