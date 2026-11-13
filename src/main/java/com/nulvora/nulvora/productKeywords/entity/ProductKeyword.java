package com.nulvora.nulvora.productKeywords.entity;

import com.nulvora.nulvora.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
