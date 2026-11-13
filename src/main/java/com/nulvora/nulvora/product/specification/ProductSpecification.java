package com.nulvora.nulvora.product.specification;

import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.productKeywords.entity.ProductKeyword;
import com.nulvora.nulvora.productVariant.entity.ProductVariant;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    // Retorna somente produtos ativos
    public static Specification<Product> isActive() {

        return (root, query, cb) ->
                cb.isTrue(root.get("active"));
    }

    // Pesquisa nome, descrição, keywords e SKU
    public static Specification<Product> searchText(String search) {

        return (root, query, cb) -> {

            String pattern = "%" + search.trim().toLowerCase() + "%";

            // Pesquisa no nome e na descrição
            Predicate nameMatch = cb.like(
                    cb.lower(root.get("name")),
                    pattern
            );

            Predicate descriptionMatch = cb.like(
                    cb.lower(root.get("description")),
                    pattern
            );

            // Subquery para pesquisar nas keywords
            Subquery<Integer> keywordSubquery =
                    query.subquery(Integer.class);

            Root<ProductKeyword> keywordRoot =
                    keywordSubquery.from(ProductKeyword.class);

            keywordSubquery.select(cb.literal(1));

            keywordSubquery.where(
                    cb.equal(
                            keywordRoot.get("product").get("id"),
                            root.get("id")
                    ),
                    cb.like(
                            cb.lower(keywordRoot.get("keyword")),
                            pattern
                    )
            );

            Predicate keywordMatch = cb.exists(keywordSubquery);

            // Subquery para pesquisar pelo SKU
            Subquery<Integer> variantSubquery =
                    query.subquery(Integer.class);

            Root<ProductVariant> variantRoot =
                    variantSubquery.from(ProductVariant.class);

            variantSubquery.select(cb.literal(1));

            variantSubquery.where(
                    cb.equal(
                            variantRoot.get("product").get("id"),
                            root.get("id")
                    ),
                    cb.isTrue(variantRoot.get("active")),
                    cb.like(
                            cb.lower(variantRoot.get("sku")),
                            pattern
                    )
            );

            Predicate skuMatch = cb.exists(variantSubquery);

            // Nome OU descrição OU keyword OU SKU
            return cb.or(
                    nameMatch,
                    descriptionMatch,
                    keywordMatch,
                    skuMatch
            );
        };
    }
}