package com.nulvora.nulvora.wishListItem.service;


import com.nulvora.nulvora.helper.GlobalHelperService;
import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.wishList.entity.WishList;
import com.nulvora.nulvora.wishList.mapper.WishListMapper;
import com.nulvora.nulvora.wishList.repository.WishListRepository;
import com.nulvora.nulvora.wishListItem.dto.response.WishListItemResponse;
import com.nulvora.nulvora.wishListItem.entity.WishListItem;
import com.nulvora.nulvora.wishListItem.repository.WishListItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class WishListItemService {
    private final WishListItemRepository wishListItemRepository;
    private final GlobalHelperService globalHelperService;
    private final WishListRepository wishListRepository;
    private final WishListMapper wishListMapper;

    // get de todos os produtos da lista de desejos
    public Page<WishListItemResponse> getMyWishListItems(Pageable pageable){
        // pega a lista do usuario
        WishList wishList = wishListRepository.findByUserId(globalHelperService.getLoggedUser().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Lista de desejos não encontrada"));

        return wishListItemRepository.findByWishListId(wishList.getId(), pageable)
                .map(wishListMapper::toWishListItemResponse);
    }

    // adcionar produtos na lista de desejos
    public WishListItemResponse addItemInWishList(Long productId){

        // pega a lista do usuario
        WishList wishList = wishListRepository.findByUserId(globalHelperService.getLoggedUser().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Lista de desejos não encontrada"));

        // pega o produto
        Product product = globalHelperService.getActiveProductOrThrow(productId);

        // caso ja tenha adicionado
        if (wishListItemRepository.existsByWishListIdAndProductId(wishList.getId(), product.getId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esse produto ja foi adcionado à lista de desejos");
        }

        // cria a relação
        WishListItem wishListItem = new WishListItem();
        wishListItem.setProduct(product);
        wishListItem.setWishList(wishList);

        return wishListMapper.toWishListItemResponse(wishListItemRepository.save(wishListItem));
    }

    // remover produtos da lista de desejos (dentro da pagina da lista)
    public void removeItemFromWishListIn(Long wishListItemId){
        // pega a lista do usuario
        WishList wishList = wishListRepository.findByUserId(globalHelperService.getLoggedUser().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Lista de desejos não encontrada"));

        // acha a relação
        WishListItem wishListItem = wishListItemRepository.findByIdAndWishListId(wishListItemId, wishList.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto não encontrado"));

        // apaga
        wishListItemRepository.delete(wishListItem);
    }

    // remover produtos da lista de desejos (de fora da pagina da lista)
    public void removeItemFromWishList(Long productId){
        // pega a lista do usuario
        WishList wishList = wishListRepository.findByUserId(globalHelperService.getLoggedUser().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Lista de desejos não encontrada"));

        // acha a relação
        WishListItem wishListItem = wishListItemRepository.findByWishListIdAndProductId(wishList.getId(), productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto não encontrado"));

        // apaga
        wishListItemRepository.delete(wishListItem);
    }




}
