package com.nulvora.nulvora.productImg.service;

import com.nulvora.nulvora.productImg.dto.request.ProductImgRequest;
import com.nulvora.nulvora.productImg.dto.request.ProductImgUpdateRequest;
import com.nulvora.nulvora.productImg.entity.ProductImg;
import com.nulvora.nulvora.productImg.mapper.ProductImgMapper;
import com.nulvora.nulvora.productImg.repository.ProductImgRepository;
import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ProductImgService {
    private final ProductImgRepository productImgRepository;
    private final ProductImgMapper productImgMapper;


    // criar imagens para a variante, defininindo que apenas uma imagem vai ter primary = true
    public void createVariantImgs(ProductVariant productVariant, List<ProductImgRequest> images){

        // pega todas as fotos em que o primary é true
        long primaryCount = images.stream()
                .filter(ProductImgRequest::isPrimary)
                .count();

        // se tiver mais de uma primary
        if (primaryCount > 1) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Apenas uma imagem pode ser principal"
            );
        }


        // cria as imagens, percorre pelo indice da lista
        List<ProductImg> productImgs = IntStream.range(0, images.size())
                .mapToObj(index -> {
                    ProductImgRequest img = images.get(index);

                    ProductImg productImg = new ProductImg();

                    productImg.setImageUrl(img.imageUrl());

                    // relação inversa
                    productVariant.getProductImgs().add(productImg);

                    // caso não tenha mandado nenhuma foto primária, a foto do indice 0 será automaticamente a primária
                    boolean isPrimary = primaryCount < 1 && index == 0
                            || img.isPrimary();

                    productImg.setPrimary(isPrimary);
                    productImg.setProductVariant(productVariant);



                    return productImg;
                })
                .toList();

        productImgRepository.saveAll(productImgs);



    }

    // cria uma imagem para a primeira variante padrão do produto recem criado
    public void createVariantImgs(ProductVariant productVariant, String urlImage){
        ProductImg productImg = new ProductImg();
        productImg.setImageUrl(urlImage);
        productImg.setProductVariant(productVariant);
        productImg.setPrimary(true);

    }


    // atualizar uma imagem de variant
    @Transactional
    public ProductImg updateVariantImgs(Long variantId, ProductImgUpdateRequest request){
        // encontra a imagem
        ProductImg variantImg = productImgRepository.findByIdAndProductVariantId(request.id(), variantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Imagem não encontrada"));

        // caso ela seja a nova primária, a antiga terá isPrimary como false
        if (Boolean.TRUE.equals(request.isPrimary())) {
            productImgRepository.removePrimaryFromOthers(variantId, variantImg.getId());
        }

        // atualiza
        productImgMapper.updateEntity(request, variantImg);

        return variantImg;

    }



}
