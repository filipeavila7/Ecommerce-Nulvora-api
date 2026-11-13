package com.nulvora.nulvora.productVariant.service;

import com.nulvora.nulvora.helper.GlobalHelperService;
import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.productAttributeOption.service.ProductAttributeOptionService;
import com.nulvora.nulvora.productImg.dto.request.ProductImgUpdateRequest;
import com.nulvora.nulvora.productImg.dto.response.ProductImgResponse;
import com.nulvora.nulvora.productImg.entity.ProductImg;
import com.nulvora.nulvora.productImg.service.ProductImgService;
import com.nulvora.nulvora.productVariant.dto.request.ProductVariantRequest;
import com.nulvora.nulvora.productVariant.dto.request.ProductVariantUpdateRequest;
import com.nulvora.nulvora.productVariant.dto.response.ProductVariantResponse;
import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import com.nulvora.nulvora.productVariant.mapper.ProductVariantMapper;
import com.nulvora.nulvora.productVariant.repository.ProductVariantRepository;
import com.nulvora.nulvora.variantAttribute.service.VariantAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductVariantService {
    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantMapper productVariantMapper;
    private final VariantAttributeService variantAttributeService;
    private final ProductAttributeOptionService productAttributeOptionService;
    private final ProductImgService productImgService;
    private final GlobalHelperService globalHelperService;

    // get de todas as variantes pelo id do produto
    public Page<ProductVariantResponse> getAllVariants(Long productId, Pageable pageable){
        globalHelperService.getActiveProductOrThrow(productId);

        return productVariantRepository.findAllByProductIdAndActiveTrue(productId, pageable)
                .map(productVariantMapper::toProductVariantResponse);

    }

    // get de uma variante pelo id
    public ProductVariantResponse getVariantById(Long variantId){
        ProductVariant productVariant = productVariantRepository.findByIdAndActiveTrue(variantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Variante não encontrada"));

        return productVariantMapper.toProductVariantResponse(productVariant);
    }

    // criar variante padrão automaticamente
    public void createStandardVariant(Product product){
        ProductVariant defaultVariant = productVariantMapper.toDefaultVariant(product);

        // cria a variante padrão
        defaultVariant.setName("Padrão");
        defaultVariant.setPrice(BigDecimal.ZERO);
        defaultVariant.setStockQuantity(0);
        defaultVariant.setSku(generateUniqueSku());

        // cria a imagem
        productImgService.createVariantImgs(defaultVariant, product.getImageUrl());

        // salva
        productVariantRepository.save(defaultVariant);
    }

    // criar variante de produtos
    @Transactional
    public ProductVariantResponse createVariant(Long productId, ProductVariantRequest request){
        Product product = globalHelperService.getActiveProductOrThrow(productId);

        // verificar se os ids das opções passadas representam uma combinação ja existente
        variantAttributeService.validateVariantCombination(request.optionsIds(), productId);

        // verifca se a variante colocou todos os atributos necessários do produto
        productAttributeOptionService.validadeAttributeQuantitiy(request.optionsIds(), product);

        // cria o objeto e relaciona como produto
        ProductVariant variant = productVariantMapper.toEntity(request);
        variant.setProduct(product);

        // salva a variante no banco
        ProductVariant variantSave = productVariantRepository.save(variant);

        // salva as imagens no banco
        productImgService.createVariantImgs(variantSave, request.images());

        // percorre as opções e salva no banco
        for (Long optionId : request.optionsIds()){
            variantAttributeService.createVariantAttribute(variant, optionId );

        }

        // salva
        return productVariantMapper.toProductVariantResponse(variantSave);

    }

    // editar variante (atributos da variante e opções)
    @Transactional
    public ProductVariantResponse updateVariant(Long variantId, ProductVariantUpdateRequest request){
        ProductVariant productVariant = productVariantRepository.findByIdAndActiveTrue(variantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Variante não encontrada"));

        // verifica se o produto a quem a variante pertence esta ativo
        globalHelperService.getActiveProductOrThrow(productVariant.getProduct().getId());

        // edita somente os campos que não são nulos
        productVariantMapper.updateEntity(request, productVariant);

        // atualiza as opções
        if (request.optionsIds() != null){
            productAttributeOptionService.validadeAttributeQuantitiy(request.optionsIds(), productVariant.getProduct());
            variantAttributeService.updateVariantAttributes(productVariant, request.optionsIds());
        }


        return productVariantMapper.toProductVariantResponse(productVariantRepository.save(productVariant));

    }

    // editar imagem de uma variante
    public ProductImgResponse updateVariantImg(Long variantId, ProductImgUpdateRequest request){
        ProductVariant productVariant = productVariantRepository.findByIdAndActiveTrue(variantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Variante não encontrada"));

        // verifica se o produto a quem a variante pertence esta ativo
        globalHelperService.getActiveProductOrThrow(productVariant.getProduct().getId());

        ProductImg variantImg = productImgService.updateVariantImgs(productVariant.getId(), request);

        return productVariantMapper.toImgResponse(variantImg);
    }

    // gerar sku aleatório
    private String generateSku() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }

    // caso o sku aleatório ja exista, cria outro
    public String generateUniqueSku() {
        String sku;

        do {
            sku = generateSku();
        } while (productVariantRepository.existsBySku(sku));

        return sku;
    }


}
