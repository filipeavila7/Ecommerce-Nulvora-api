package com.nulvora.nulvora.productKeywords.service;

import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.productKeywords.entity.ProductKeyword;
import com.nulvora.nulvora.productKeywords.repository.ProductKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductKeywordService {
    private final ProductKeywordRepository productKeywordRepository;

    // criar keywords para produtos
    public void createKeywords(Product product, List<String> keywords){
        for (String keyword : keywords){
            ProductKeyword productKeyword = new ProductKeyword();

            productKeyword.setKeyword(keyword);

            // cria a relação dos 2 lados
            product.addProductKeyword(productKeyword);

            productKeywordRepository.save(productKeyword);
        }

    }
}
