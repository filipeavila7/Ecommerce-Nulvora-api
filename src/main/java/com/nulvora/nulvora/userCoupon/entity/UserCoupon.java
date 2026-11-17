package com.nulvora.nulvora.userCoupon.entity;


import com.nulvora.nulvora.cupom.entity.Coupon;
import com.nulvora.nulvora.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_coupon",
                        columnNames = {"user_id", "cupom_id"}
                )
        }
)
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer usageLimit;

    @Column(nullable = false)
    private Integer usageCount;

    @Column(nullable = false)
    LocalDateTime assignedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;


}
