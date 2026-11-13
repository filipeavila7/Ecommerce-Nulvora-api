package com.nulvora.nulvora.product.service;

import com.nulvora.nulvora.category.entity.Category;
import com.nulvora.nulvora.category.service.CategoryService;
import com.nulvora.nulvora.helper.GlobalHelperService;
import com.nulvora.nulvora.product.dto.request.ProductAttributeRequest;
import com.nulvora.nulvora.product.dto.request.ProductRequest;
import com.nulvora.nulvora.product.dto.request.ProductUpdateRequest;
import com.nulvora.nulvora.product.dto.response.ProductDetailsResponse;
import com.nulvora.nulvora.product.dto.response.ProductResponse;
import com.nulvora.nulvora.product.dto.response.ProductSumaryResponse;
import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.product.mapper.ProductMapper;
import com.nulvora.nulvora.product.repository.ProductRepository;
import com.nulvora.nulvora.product.specification.ProductSpecification;
import com.nulvora.nulvora.productAttribute.entity.ProductAttribute;
import com.nulvora.nulvora.productAttribute.service.ProductAttributeService;
import com.nulvora.nulvora.productAttributeOption.entity.ProductAttributeOption;
import com.nulvora.nulvora.productAttributeOption.service.ProductAttributeOptionService;
import com.nulvora.nulvora.productKeywords.service.ProductKeywordService;
import com.nulvora.nulvora.productVariant.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;
    private final ProductAttributeService productAttributeService;
    private final ProductAttributeOptionService productAttributeOptionService;
    private final ProductVariantService productVariantService;
    private final ProductKeywordService productKeywordService;
    private final GlobalHelperService globalHelperService;



    // ========== ROLE ADMIN ==========

    // criar produtos
    @Transactional
    public ProductResponse createProduct(ProductRequest request){
        // busca a categoria
        Category category = categoryService.getCategoryByIdOrThrow(request.categoryId());

        // cria o produto
        Product product = productMapper.toEntity(request);

        // relaciona com categoria
        product.setCategory(category);

        // cria os atributos e as opções para eles
        createAttributesAndOptions(product, request.attributes());

        // cria as keywords
        productKeywordService.createKeywords(product, request.keywords());

        // salva o produto
        Product productSave = productRepository.save(product);

        // cria uma variante padrão para o produto criado
        productVariantService.createStandardVariant(product);

        return productMapper.toProductResponse(productSave);

    }

    // editar produtos
    @Transactional
    public ProductResponse updateProduct(Long productId, ProductUpdateRequest request){
        Product product = globalHelperService.getActiveProductOrThrow(productId);

        // atualiza somente os campos que não são nulos
        productMapper.updateEntity(request, product);

        // edita a categoria
        if (request.categoryId() != null){
            Category category = categoryService.getCategoryByIdOrThrow(request.categoryId());
            product.setCategory(category);
        }

        return productMapper.toProductResponse(productRepository.save(product));
    }

    // criar atributos e opções para o produto
    private void createAttributesAndOptions(Product product, List<ProductAttributeRequest> request){
        for (ProductAttributeRequest attribute : request){
            ProductAttribute productAttribute = productAttributeService
                    .createProductAttribute(product, attribute.name());

            // garante o relacionamento dos 2 lados
            product.addProductAttribute(productAttribute);

            // cria as opções para cada atributo
            for (String option : attribute.options()){
                ProductAttributeOption attributeOption = productAttributeOptionService
                        .createAttributeOption(productAttribute, option);

                // garante o relacionamento dos 2 lados
                productAttribute.addOption(attributeOption);
            }
        }
    }



    // ========== ROLE USER ==========

    // get de todos os produtos ativos juntamente com o menor preço dentre as suas variantes
    public Page<ProductSumaryResponse> getAllProducts(Pageable pageable){
        return productRepository.findAllSummaries(pageable);
    }

    // get de detalhes do produto
    public ProductDetailsResponse getProductDetails(Long productId){
        Product product = globalHelperService.getActiveProductOrThrow(productId);
        return productMapper.toDetailsResponse(product);
    }


    // pesquisar produtos pelo nome, descrição, sku e keywords

    public Page<ProductSumaryResponse> search(
            String search,
            Pageable pageable
    ) {
        Specification<Product> spec = ProductSpecification.isActive();

        if (search != null && !search.isBlank()) {
            spec = spec.and(ProductSpecification.searchText(search));
        }

        Page<Product> products = productRepository.findAll(spec, pageable);

        if (products.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Long> productIds = products.getContent()
                .stream()
                .map(Product::getId)
                .toList();

        Map<Long, BigDecimal> minPrices = new HashMap<>();

        productRepository.findMinPricesByProductIds(productIds)
                .forEach(row -> minPrices.put(
                        (Long) row[0],
                        (BigDecimal) row[1]
                ));

        return products.map(product -> new ProductSumaryResponse(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                minPrices.get(product.getId())
        ));
    }





}
