package com.nulvora.nulvora.wishListItem.entity;


import com.nulvora.nulvora.common.entity.BaseEntity;
import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.wishList.entity.WishList;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_wishlist_product",
                        columnNames = {"wish_list_id", "product_id"}
                )
        }
)
public class WishListItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wish-list-id")
    WishList wishList;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;


}
